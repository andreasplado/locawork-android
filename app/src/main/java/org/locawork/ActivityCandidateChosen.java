package org.locawork;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityCandidateChosen extends AppCompatActivity {

    private Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_chosen);
        back = findViewById(R.id.back_to_the_search);
        back.setOnClickListener(v -> {
            finish();
           startActivity(new Intent(this, MainActivity.class));
        });
    }
}