package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ActivityBeginWork extends AppCompatActivity {

    private Button back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);
        back = findViewById(R.id.back_to_the_search);
        back.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, ActivityMain.class));
        });
    }
}