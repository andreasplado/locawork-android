package org.locawork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.locawork.model.User;
import org.locawork.model.UserLogin;
import org.locawork.util.PreferencesUtil;
import org.locawork.util.URLUtils;

import retrofit2.Response;

import static org.locawork.util.PrefConstants.TAG_RADIUS;
import static org.locawork.util.PreferencesUtil.KEY_EMAIL;
import static org.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;
import static org.locawork.util.PreferencesUtil.KEY_TOKEN;
import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class SignInActivity extends Activity {


    GoogleSignInAccount account;
    private ControllerRegisterUser controllerRegisterUser = new ControllerRegisterUser();
    private ControllerLogin controllerLogin = new ControllerLogin();
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInBtn;
    private LoginButton facebookSignInButton;
    private CallbackManager callbackManager;
    private static int GOOGLE_REQUEST_CODE = 103;
    private Button registerAccount, login;
    private EditText email, password;
    private Button privacyPolicy;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
        this.googleSignInBtn = findViewById(R.id.log_in_with_google);
        this.facebookSignInButton = findViewById(R.id.log_in_with_facebook);
        this.registerAccount = findViewById(R.id.register);
        this.email = findViewById(R.id.email);
        this.privacyPolicy = findViewById(R.id.privacy_policy);
        this.password = findViewById(R.id.password);
        this.login = findViewById(R.id.login);
        this.facebookSignInButton.setPermissions("public_profile email");
        callbackManager = CallbackManager.Factory.create();
        this.googleSignInBtn.setOnClickListener(view -> authenticateGoogle());
        this.facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                authenticateFacebook(loginResult);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignInActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        this.login.setOnClickListener(v -> {

            if(this.email.getText().toString().equals("")){
                this.email.setError(getResources().getString(R.string.please_enter_your_email));
            }
            if(this.password.getText().toString().equals("")){
                this.password.setError(getResources().getString(R.string.please_enter_your_password));
            }
            if(!this.password.getText().toString().equals("") && !this.email.getText().toString().equals("")){
                UserLogin user = new UserLogin();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                new ControllerLogin().login(user);
            }

        });
        this.registerAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });
        privacyPolicy.setOnClickListener(v -> {
            URLUtils.goToUrl(getResources().getString(R.string.privacy_policy_url), this);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_REQUEST_CODE) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void authenticateGoogle() {
        Intent signInIntent = this.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount result = completedTask.getResult(ApiException.class);
            this.account = result;
            handleGoogleauthentication();
        } catch (ApiException e) {
            if (e.getMessage().equals("7:")) {
                Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void handleGoogleauthentication() {
        User user = new User();
        user.setEmail(this.account.getEmail());
        user.setFullname(this.account.getId());
        user.setContact("+372 112");
        this.controllerRegisterUser.signUp(user);
    }

    private void authenticateFacebook(LoginResult loginResult) {
        requestData(loginResult);
    }

    private void requestData(LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {

            final JSONObject json = response.getJSONObject();


            try {
                if (json != null) {
                    User user = new User();
                    user.setEmail(json.getString("email"));
                    user.setFullname(json.getString("id"));
                    user.setContact("+372 112");
                    this.controllerRegisterUser.signUp(user);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Subscribe
    public void loginSuccess(EventUserLoggedIn eventUserLoggedIn){
        Response<Void> response = eventUserLoggedIn.getResponse();
        String userid= response.headers().get("User_id");
        String token = response.headers().get("Authorization");
        String email = response.headers().get("Email");
        String firebaseToken = response.headers().get("Firebase_token");
        String tagRadius = response.headers().get("Radius");

        PreferencesUtil.save(this, KEY_PUSH_NOTIFICATION_TOKEN, "");

        if(response.code() == 200) {
            finish();
            PreferencesUtil.save(this, KEY_USER_ID, Integer.parseInt(userid));
            PreferencesUtil.save(this, KEY_TOKEN, token );
            PreferencesUtil.save(this, KEY_EMAIL, email);
            PreferencesUtil.save(this, KEY_EMAIL, email);
            float radius = Float.parseFloat(tagRadius);
            if( radius == 0.0){
                finish();
                Intent i2 = new Intent(this, SetRadiusActivity.class);
                startActivity(i2);

            }else {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
        if(response.code() == 403){
            this.email.getText().clear();
            this.password.getText().clear();
            Toast.makeText(this, getResources().getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
        }
        if(response.code() == 408){
            Toast.makeText(this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
    }
    @Subscribe
    public void loginFailure(EventUserFailedToLogin eventUserLoggedIn){
        Toast.makeText(this, eventUserLoggedIn.getT().getMessage(), Toast.LENGTH_LONG).show();
    }

    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
