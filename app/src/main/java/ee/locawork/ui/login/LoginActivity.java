package ee.locawork.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import ee.locawork.ControllerRegisterUser;
import ee.locawork.EventUserLoggedIn;
import ee.locawork.ActivityMain;
import ee.locawork.R;
import ee.locawork.RegisterUserActivity;
import ee.locawork.ActivitySetRadius;
import ee.locawork.alert.AlertPrivacyPolicy;
import ee.locawork.model.User;
import ee.locawork.model.UserLogin;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Response;

import static ee.locawork.util.PreferencesUtil.KEY_EMAIL;
import static ee.locawork.util.PreferencesUtil.KEY_IS_WITHOUT_ADDS;
import static ee.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;
import static ee.locawork.util.PreferencesUtil.KEY_RADIUS;
import static ee.locawork.util.PreferencesUtil.KEY_TOKEN;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class LoginActivity extends Activity {


    GoogleSignInAccount account;
    private ControllerRegisterUser controllerRegisterUser = new ControllerRegisterUser();
    private ControllerLogin controllerLogin = new ControllerLogin();
    private static int GOOGLE_REQUEST_CODE = 103;
    private Button registerAccount, login;
    private EditText email, password;
    private Button privacyPolicy;
    private RelativeLayout loadingLogin;
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
        this.registerAccount = findViewById(R.id.register);
        this.email = findViewById(R.id.email);
        this.privacyPolicy = findViewById(R.id.privacy_policy);
        this.password = findViewById(R.id.password);
        this.login = findViewById(R.id.login);
        this.loadingLogin = findViewById(R.id.loading_view);
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
                loadingLogin.setVisibility(View.VISIBLE);
                String plainPassword = password.getText().toString();
                user.setPassword(plainPassword);
                new ControllerLogin().login(user, this);
            }

        });
        this.registerAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterUserActivity.class));
        });
        privacyPolicy.setOnClickListener(v -> {
            AlertPrivacyPolicy.init(LoginActivity.this, getApplicationContext());
        });
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


    @Subscribe
    public void loginSuccess(EventUserLoggedIn eventUserLoggedIn){
        loadingLogin.setVisibility(View.GONE);
        Response<Void> response = eventUserLoggedIn.getResponse();
        String userid= response.headers().get("User_id");
        String token = response.headers().get("Authorization");
        String email = response.headers().get("Email");
        String firebaseToken = response.headers().get("Firebase_token");
        String radius = response.headers().get("Radius");
        String isWithoutAdds = response.headers().get("Is_without_adds");



        PreferencesUtil.save(this, KEY_PUSH_NOTIFICATION_TOKEN, "");

        if(response.code() == 200) {
            PreferencesUtil.save(this, KEY_USER_ID, Integer.parseInt(userid));
            PreferencesUtil.save(this, KEY_TOKEN, "Bearer " + token );
            PreferencesUtil.save(this, KEY_EMAIL, email);
            PreferencesUtil.save(this, KEY_IS_WITHOUT_ADDS, isWithoutAdds.equals("1"));
            PreferencesUtil.save(this, KEY_RADIUS, Double.parseDouble(radius));


            double radiusConverted =  Double.parseDouble(radius);
            if( radiusConverted == 0.0){
                Intent i2 = new Intent(this, ActivitySetRadius.class);
                startActivity(i2);
            }else {
                startActivity(new Intent(this, ActivityMain.class));
            }
        }
        if(response.code() == 401){
            this.email.getText().clear();
            this.password.getText().clear();
            this.email.setError(getResources().getString(R.string.invalid_email));
            this.password.setError(getResources().getString(R.string.invalid_password));
            Toast.makeText(this, getResources().getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
        }
        if(response.code() == 408){
            Toast.makeText(this, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
        }
    }
    @Subscribe
    public void loginFailure(EventUserFailedToLogin eventUserLoggedIn){
        loadingLogin.setVisibility(View.GONE);
        Toast.makeText(this, eventUserLoggedIn.getT().getMessage(), Toast.LENGTH_LONG).show();
    }

    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
