package org.locawork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.locawork.MainActivity;
import org.locawork.R;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityAppUpdated extends AppCompatActivity {

    private Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_updated);
        back = findViewById(R.id.back_to_the_search);
        back.setOnClickListener(v -> {
            finish();
           startActivity(new Intent(this, MainActivity.class));
        });
    }
}