package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScannerView;

public class ActivityEndWork extends AppCompatActivity {

    TextView workTitle, workDescription,
            workStartTime, workEndTime, earnedSalary;
    CodeScannerView codeScannerView;
    Button endWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_work);
        workTitle = findViewById(R.id.work_title);
        workDescription = findViewById(R.id.work_description);
        workStartTime = findViewById(R.id.work_start_time);
        workEndTime = findViewById(R.id.work_end_time);
        earnedSalary = findViewById(R.id.earned_salary);
        codeScannerView = findViewById(R.id.scanner_view);
        endWork = findViewById(R.id.end_work);
        setComponents();
    }

    private void setComponents() {
        workTitle.setText("");
    }
}