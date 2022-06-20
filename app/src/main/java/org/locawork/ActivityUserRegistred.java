package org.locawork;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ActivityUserRegistred extends AppCompatActivity {

    private Button login;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registred);
        login = findViewById(R.id.login);

        login.setOnClickListener(v -> {
          startActivity(new Intent(this, SignInActivity.class));
        });
    }

}