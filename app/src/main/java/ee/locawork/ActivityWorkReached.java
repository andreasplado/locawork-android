package ee.locawork;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.maps.model.LatLng;
import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.EventStartWorkFailure;

import ee.locawork.alert.AlertError;
import ee.locawork.model.dto.StartTimeDTO;
import ee.locawork.services.ServiceReachedJob;
import ee.locawork.util.ActivityUtils;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;

import androidx.appcompat.app.AppCompatActivity;

import static ee.locawork.services.ServiceReachedJob.KEY_JOB_ID;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

import java.util.Date;

public class ActivityWorkReached extends AppCompatActivity {
    private ImageButton confirm;
    private TextView jobDescription;
    private TextView jobSalary;
    private TextView jobTitle;
    private ImageButton retry;
    private TextView locationName;
    private LinearLayout serverErrorView;
    private LinearLayout successView;
    private PackageManager pm;

    private CodeScannerView codeScannerView;

    private CodeScanner startWorkScanner;
    boolean doubleBackToExitPressedOnce = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work);
        this.jobTitle = findViewById(R.id.job_title);
        this.pm = getPackageManager();
        this.jobDescription = findViewById(R.id.job_description);
        this.jobSalary = findViewById(R.id.job_salary);
        this.serverErrorView = findViewById(R.id.server_error_view);
        this.confirm = findViewById(R.id.start_work);
        this.successView = findViewById(R.id.success_view);
        this.retry = findViewById(R.id.retry);
        this.locationName = findViewById(R.id.location_name);
        this.jobTitle.setText(PreferencesUtil.readString(getApplicationContext(), ServiceReachedJob.KEY_JOB_TITLE, ""));
        this.jobDescription.setText(PreferencesUtil.readString(getApplicationContext(), ServiceReachedJob.KEY_JOB_DESCRIPTION, ""));
        double latitude = Double.parseDouble(PreferencesUtil.readString(getApplicationContext(), ServiceReachedJob.KEY_JOB_LATITUDE, "0.1"));
        double longitude = Double.parseDouble(PreferencesUtil.readString(getApplicationContext(), ServiceReachedJob.KEY_JOB_LONGITUDE, "0.1"));
        this.locationName.setText(LocationUtil.fetchLocationData(this, new LatLng(latitude, longitude)));
        this.jobSalary.setText(PreferencesUtil.readString(getApplicationContext(), ServiceReachedJob.KEY_JOB_SALARY, FluidSlider.TEXT_START));
        this.codeScannerView = findViewById(R.id.scanner_view);
        this.startWorkScanner = new CodeScanner(this, codeScannerView);
        startWorkScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            int jobId = PreferencesUtil.readInt(this, KEY_JOB_ID, 0);
            new ControllerCheckIfWorkExists().getData(jobId);
        }));
    }

    @Subscribe
    private void eventRegister(EventCheckIfUserExists eventCheckIfUserExists) {
        startActivity(new Intent(this, ActivityMain.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        startWorkScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startWorkScanner.releaseResources();
    }

    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        StartTimeDTO startTimeDTO = new StartTimeDTO();
        String startTime = new Date().getTime() + "";
        startTimeDTO.setStartTime(startTime);
        startTimeDTO.setApplyerId(PreferencesUtil.readInt(ActivityWorkReached.this, KEY_USER_ID, 0));
        startTimeDTO.setJobId(PreferencesUtil.readInt(ActivityWorkReached.this, KEY_JOB_ID, 0));
        this.confirm.setOnClickListener(view -> {
            new ControllerStartWork().postData(this, startTimeDTO);
        });
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            ActivityUtils.restartActivity(ActivityWorkReached.this);
        });


    }

    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

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
    public void workStarted(EventStartWork eventStartWork) {
        if(eventStartWork.getResponse().code() == 200) {
            this.serverErrorView.setVisibility(View.GONE);
            this.successView.setVisibility(View.VISIBLE);
            getApplicationContext().stopService(new Intent(ActivityWorkReached.this, ServiceReachedJob.class));
            PreferencesUtil.save(ActivityWorkReached.this, ServiceReachedJob.KEY_HAVE_REACHED, 0);
            PreferencesUtil.save(ActivityWorkReached.this, PreferencesUtil.KEY_HAVE_STARTED, 1);
            PreferencesUtil.save(ActivityWorkReached.this, PreferencesUtil.KEY_WORK_START_TIME, new Date().getTime());
            startActivity(new Intent(this, ActivitySuccessfullyStartWork.class));
        }
        if(eventStartWork.getResponse().code() == 400){
            AlertError.init(this, getApplicationContext(), getApplicationContext().getString(R.string.server_error));
        }
    }

    @Subscribe
    public void workStarted(EventCheckIfWorkExists eventCheckIfWorkExists) {
        StartTimeDTO startTimeDTO = new StartTimeDTO();
        String currentTime = new Date().getTime() + "";
        startTimeDTO.setStartTime(currentTime);
        startTimeDTO.setApplyerId(PreferencesUtil.readInt(ActivityWorkReached.this, KEY_USER_ID, 0));
        startTimeDTO.setJobId(PreferencesUtil.readInt(ActivityWorkReached.this, KEY_JOB_ID, 0));
        new ControllerStartWork().postData(ActivityWorkReached.this, startTimeDTO);
    }

    @Subscribe
    public void workStartedFailure(EventStartWorkFailure eventStartWorkFailure) {
        this.successView.setVisibility(View.GONE);
        this.serverErrorView.setVisibility(View.VISIBLE);
    }
}
