package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScannerView;

public class ActivityEndWork extends AppCompatActivity {
    CodeScannerView codeScannerView;
    Button exitToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_work);
        exitToMain = findViewById(R.id.exit_to_main);
        exitToMain.setOnClickListener(view -> startActivity(new Intent(ActivityEndWork.this, ActivityMain.class)));


    }
}