package ee.locawork;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ee.locawork.util.TimerUtils;

public class ActivitySuccessfullyStartWork extends AppCompatActivity {

    private Button startWork;
    private Button endWork;

    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_start_work);
        startWork = findViewById(R.id.start_work);
        endWork = findViewById(R.id.end_work);
        time = findViewById(R.id.time);

        startWork.setOnClickListener(v -> {
            startWork.setVisibility(View.GONE);
            endWork.setVisibility(View.VISIBLE);
            TimerUtils.startCountout(time);
        });
        endWork.setOnClickListener(v -> {
            startActivity(new Intent(this, ActivityEndWork.class));
        });
    }
}