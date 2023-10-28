package ee.locawork;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;
import static ee.locawork.util.PreferencesUtil.KEY_WORK_START_TIME;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import ee.locawork.alert.AlertError;
import ee.locawork.model.dto.EndTimeDTO;
import ee.locawork.services.ServiceReachedJob;
import ee.locawork.util.PreferencesUtil;
import ee.locawork.util.TimerUtils;

public class ActivityWorkInProgress extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private TextView timeView;
    private boolean isRunning = false;
    private Button callEmployer, btnEndWork;
    private TextView jobTitle;
    private TextView jobDescription, salary, expectedSalary;

    private CodeScannerView codeScannerView;
    private CodeScanner endWorkScanner;

    private FrameLayout scannerLayout;

    private int hoursToWorkInMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_in_progress);

        jobTitle = findViewById(R.id.job_title);
        jobDescription = findViewById(R.id.job_description);
        salary = findViewById(R.id.job_salary);
        expectedSalary = findViewById(R.id.expected_salary);
        callEmployer = findViewById(R.id.call_employer);
        btnEndWork = findViewById(R.id.end_work);
        codeScannerView = findViewById(R.id.scanner_view);
        scannerLayout = findViewById(R.id.scanner_layout);
        timeView = findViewById(R.id.time);

        salary.setText(PreferencesUtil.readString(this, ServiceReachedJob.KEY_JOB_SALARY, ""));
        jobTitle.setText(PreferencesUtil.readString(this, ServiceReachedJob.KEY_JOB_TITLE, ""));
        jobDescription.setText(PreferencesUtil.readString(this, ServiceReachedJob.KEY_JOB_DESCRIPTION, ""));

        this.endWorkScanner = new CodeScanner(this, codeScannerView);
        endWorkScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            int workId = Integer. parseInt(result.getText());
            new ControllerCheckIfWorkExists().getData(workId);
            String endTime = new Date().getTime() + "";
            EndTimeDTO endTimeDTO = new EndTimeDTO();
            endTimeDTO.setEndTime(endTime);
            endTimeDTO.setApplyerId(PreferencesUtil
                    .readInt(ActivityWorkInProgress.this, KEY_USER_ID, 0));
            endTimeDTO.setJobId(PreferencesUtil
                    .readInt(ActivityWorkInProgress.this, ServiceReachedJob.KEY_JOB_ID, 0));

            new ControllerEndWork().postData(ActivityWorkInProgress.this, endTimeDTO);
        }));
        btnEndWork.setOnClickListener(v -> {
            String endTime = new Date().getTime() + "";
            EndTimeDTO endTimeDTO = new EndTimeDTO();
            endTimeDTO.setEndTime(endTime);
            endTimeDTO.setApplyerId(PreferencesUtil
                    .readInt(ActivityWorkInProgress.this, KEY_USER_ID, 0));
            endTimeDTO.setJobId(PreferencesUtil
                    .readInt(ActivityWorkInProgress.this, ServiceReachedJob.KEY_JOB_ID, 0));

            new ControllerEndWork().postData(ActivityWorkInProgress.this, endTimeDTO);
        });

        if(!isRunning){
            long workStartTime = PreferencesUtil
                    .readLong(ActivityWorkInProgress.this, KEY_WORK_START_TIME,0);

            long hoursToWork = PreferencesUtil
                    .readLong(ActivityWorkInProgress.this, ServiceReachedJob.KEY_HOURS_TO_WORK,0);
            long expectedEndTime = workStartTime + hoursToWork;

            TimerUtils.startCount(this, timeView, codeScannerView, expectedEndTime, expectedSalary, endWorkScanner, btnEndWork, scannerLayout);
            isRunning = true;
        }

        ControllerGetEmployerData controllerGetEmployerData = new ControllerGetEmployerData();
        controllerGetEmployerData.getData(getApplicationContext(), PreferencesUtil.readInt(getApplicationContext(), KEY_USER_ID, 0));

    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(!isRunning){
            long workStartTime = PreferencesUtil
                    .readLong(ActivityWorkInProgress.this, KEY_WORK_START_TIME,0);

            long hoursToWork = PreferencesUtil
                    .readLong(ActivityWorkInProgress.this, ServiceReachedJob.KEY_HOURS_TO_WORK,0) + 1;

            long expectedEndTime = workStartTime + hoursToWork;

            TimerUtils.startCount(this, timeView, codeScannerView, expectedEndTime, expectedSalary);
        }
        endWorkScanner.startPreview();*/
    }
    @Override
    protected void onPause() {
        super.onPause();
        endWorkScanner.releaseResources();

    }

    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Subscribe
    public void endWork(EventEndWork eventEndWork){
        if(eventEndWork.getResponse().code() == 200){
            startActivity(new Intent(this, ActivityEndWork.class));
            PreferencesUtil.flushJobProcess(getApplicationContext());
            new Intent(this, ActivityEndWork.class);
        }
    }

    @Subscribe
    public void getEmployerData(EventGetEmployerData eventGetEmployerData){
        callEmployer.setVisibility(View.VISIBLE);
        this.callEmployer.setText(getResources().getString(R.string.call_employer));
        this.callEmployer.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + eventGetEmployerData.getResponse().body().getContact()));
            startActivity(intent);
        });
    }

    @Subscribe
    public void failedToGetEmployerData(EventFailedToGetEmployerData eventGetEmployerData){
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_to_fetch_employer_data), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void endWork(EventEndWorkFailure eventEndWorkFailure){
        AlertError.init(this, getApplicationContext(), eventEndWorkFailure.getT().getLocalizedMessage());
    }
}