package ee.locawork;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ee.locawork.ui.login.LoginActivity;

public class ActivityUserRegistred extends AppCompatActivity {

    private Button login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registred);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
          startActivity(new Intent(this, LoginActivity.class));
        });
    }

}