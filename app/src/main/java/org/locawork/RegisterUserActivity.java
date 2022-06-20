package org.locawork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.model.User;
import org.locawork.util.StringUtil;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText username;
    private EditText email, password, passwordConfirm, contact, fullname;
    private ControllerRegisterUser  controllerRegisterUser= new ControllerRegisterUser();
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.password_confirm);
        contact = findViewById(R.id.contact);
        register = findViewById(R.id.register);
        fullname = findViewById(R.id.fullname);

        register.setOnClickListener(v -> {
            String pass = password.getText().toString();
            String passConfirm = passwordConfirm.getText().toString();

            User user = new User();
            if(fullname.getText().toString().equals("")){
                fullname.setError(getResources().getString(R.string.please_enter_your_fullname));
            }
            if(password.getText().toString().equals(passwordConfirm.getText().toString()) && !fullname.getText().toString().equals("")){
                if(StringUtil.isEmailValid(email.getText().toString())){
                    user.setFullname(fullname.getText().toString());
                    user.setEmail(email.getText().toString().toLowerCase());
                    user.setPassword(password.getText().toString());
                    user.setContact(contact.getText().toString());
                    controllerRegisterUser.signUp(user);
                }else{
                    email.setError(getResources().getString(R.string.email_is_not_in_correct_format));
                }

            }else{
                Toast.makeText(RegisterUserActivity.this, getResources().getString(R.string.password_doesnt_match), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Subscribe
    public void userRegistered(EventUserRegistred eventUserRegistred){
        if(eventUserRegistred.getResponse().body().getEmail() == null){
            Toast.makeText(this, getResources().getString(R.string.user_already_exists), Toast.LENGTH_LONG).show();
        }else{
            Intent i = new Intent(this, ActivityUserRegistred.class);
            startActivity(i);

        }
    }

    @Subscribe
    public void userRegistered(EventUserFailedToRegister eventUserFailedToRegister){
        Toast.makeText(this, getResources().getString(R.string.user_failed_to_register), Toast.LENGTH_LONG).show();
    }


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



}