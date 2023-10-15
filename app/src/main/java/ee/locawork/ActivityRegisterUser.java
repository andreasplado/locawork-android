package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ee.locawork.model.User;
import ee.locawork.ui.login.ActivityLogin;
import ee.locawork.util.StringUtil;

public class ActivityRegisterUser extends AppCompatActivity {

    private EditText email, password, passwordConfirm, contact, fullname, idCode, companyName, companyRegistrationNumber;
    private CheckBox iWantToOfferWork;
    private LinearLayout companyForm;
    private ControllerRegisterUser  controllerRegisterUser= new ControllerRegisterUser();
    private Button register, login;

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
        companyForm = findViewById(R.id.company_form);
        companyName = findViewById(R.id.company_name);
        companyRegistrationNumber = findViewById(R.id.company_registration_number);
        iWantToOfferWork = findViewById(R.id.i_want_to_offer_work);
        login = findViewById(R.id.login);
        idCode = findViewById(R.id.id_code);

        login.setOnClickListener(view -> {
            startActivity(new Intent(this, ActivityLogin.class));
        });
        iWantToOfferWork.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                companyForm.setVisibility(View.VISIBLE);
            }else{
                companyForm.setVisibility(View.GONE);
            }
        });

        register.setOnClickListener(v -> {
            String pass = password.getText().toString();
            String passConfirm = passwordConfirm.getText().toString();

            User user = new User();
            if(fullname.getText().toString().equals("")){
                fullname.setError(getResources().getString(R.string.please_enter_your_fullname));
            }
            if(email.getText().toString().equals("")){
                email.setError(getResources().getString(R.string.please_enter_your_email));
            }
            if(password.getText().toString().equals("")){
                password.setError(getResources().getString(R.string.please_enter_your_password));
            }
            if(passwordConfirm.getText().toString().equals("")){
                passwordConfirm.setError(getResources().getString(R.string.please_enter_your_password_confirmation));
            }
            if(contact.getText().toString().equals("")){
                contact.setError(getResources().getString(R.string.please_enter_your_contact));
            }
            if(idCode.getText().toString().equals("")){
                idCode.setError(getResources().getString(R.string.please_enter_your_id_code));
            }
            if(password.getText().toString().equals(passwordConfirm.getText().toString())&& !fullname.getText().toString().equals("")){
                if(StringUtil.isEmailValid(email.getText().toString())){
                    user.setFullname(fullname.getText().toString());
                    user.setEmail(email.getText().toString().toLowerCase());
                    user.setPassword(password.getText().toString());
                    user.setContact(contact.getText().toString());
                    user.setCompanyRegNumber(companyRegistrationNumber.getText().toString());
                    user.setCompanyName(companyName.getText().toString());
                    user.setIdCode(idCode.getText().toString());
                    controllerRegisterUser.signUp(user);
                }else{
                    email.setError(getResources().getString(R.string.email_is_not_in_correct_format));
                }

            }else{
                Toast.makeText(ActivityRegisterUser.this, getResources().getString(R.string.password_doesnt_match), Toast.LENGTH_LONG).show();
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