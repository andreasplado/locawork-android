package org.locawork;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class ActivitySuccessfullyApplied extends AppCompatActivity {

    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_applied);
        goBack = findViewById(R.id.go_back_to_main);

        goBack.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}