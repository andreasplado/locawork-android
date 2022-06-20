package org.locawork;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import org.locawork.services.ServiceReachedJob;
import org.locawork.util.ActivityUtils;
import org.locawork.util.AnimationUtil;
import org.locawork.util.LocationUtil;
import org.locawork.util.PreferencesUtil;

import androidx.appcompat.app.AppCompatActivity;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class StartJobActivity extends AppCompatActivity {
    private ImageButton confirm;
    private TextView jobDescription;
    private TextView jobSalary;
    private TextView jobTitle;
    private ImageButton retry;
    private TextView locationName;
    private LinearLayout serverErrorView;
    private LinearLayout successView;
    private PackageManager pm;
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
    }

    @Subscribe
    private void eventRegister(EventCheckIfUserExists eventCheckIfUserExists) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        this.confirm.setOnClickListener(view -> {
            new ControllerStartWork().postData(this, PreferencesUtil.readInt(StartJobActivity.this, KEY_USER_ID, 0));
        });
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            ActivityUtils.restartActivity(StartJobActivity.this);
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
        startActivity(new Intent(this, ActivitySuccessfullyApplied.class));
        this.serverErrorView.setVisibility(View.GONE);
        this.successView.setVisibility(View.VISIBLE);
        PreferencesUtil.save(StartJobActivity.this, ServiceReachedJob.KEY_HAVE_REACHED, 0);
        getApplicationContext().stopService(new Intent(StartJobActivity.this, ServiceReachedJob.class));
    }

    @Subscribe
    public void workStartedFailure(EventStartWorkFailure eventStartWorkFailure) {
        this.successView.setVisibility(View.GONE);
        this.serverErrorView.setVisibility(View.VISIBLE);
    }
}
