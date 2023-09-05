package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;

import ee.locawork.ActivityMain;
import ee.locawork.EventSetRadiusFailure;
import ee.locawork.EventSetRadiusSuccess;
import kotlin.Unit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ee.locawork.model.Settings;
import ee.locawork.ui.settings.EventSettingsFailure;
import ee.locawork.ui.settings.EventSettingsSuccess;
import ee.locawork.ui.settings.EventUpdateRadiusFailure;
import ee.locawork.util.PreferencesUtil;

import static ee.locawork.util.PreferencesUtil.KEY_RADIUS;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class ActivitySetRadius extends AppCompatActivity {

    private FluidSlider radiusSlider;
    private ImageButton accept;
    private ControllerSetRadius controllerSetRadius = new ControllerSetRadius();
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_radius);
        radiusSlider = findViewById(R.id.radius_slider);
        accept = findViewById(R.id.accept);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        radiusSlider.setEndTrackingListener(() -> {
            double converted = radiusSlider.getPosition() * 100;

            PreferencesUtil.save(this, KEY_RADIUS, converted);

            accept.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        accept.setOnClickListener(v -> {
            controllerSetRadius.set(this, PreferencesUtil.readInt(this, KEY_USER_ID, 0), PreferencesUtil.readDouble(this, KEY_RADIUS, 0));
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void eventUpdateRadiusSuccess(EventSetRadiusSuccess eventUpdateRadiusSuccess){
        startActivity(new Intent(ActivitySetRadius.this, ActivityMain.class));
    }
    @Subscribe
    public void eventUpdateRadiusSettingsNotSet(EventSetRadiusFailure eventUpdateRadiusSuccess){
        finish();
        startActivity(new Intent(ActivitySetRadius.this, ActivityMain.class));
    }
    @Subscribe
    public void eventUpdateRadiusSettingsNotSet(EventUpdateRadiusFailure eventUpdateRadiusFailure){
        Toast.makeText(this, getResources().getString(R.string.radius_update_failed), Toast.LENGTH_LONG).show();
    }



    @Subscribe
    public void eventSettingsSuccess(EventSettingsSuccess eventSettingsSuccess) {
        Settings settings = eventSettingsSuccess.getSettings().body();
        double radiusRounded = (int)(radiusSlider.getPosition() * 100);
        ((TextView) findViewById(R.id.nav_radius)).setText(radiusRounded + " " + getResources().getString(R.string.kilometers));
        this.radiusSlider.setPosition((float) PreferencesUtil.readDouble(this, KEY_RADIUS, 1));
        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(v -> {
            finish();
            Intent i = new Intent(ActivitySetRadius.this, ActivityMain.class);
            startActivity(i);
        });
    }

    @Subscribe
    public void eventSettingsFailure(EventSettingsFailure eventSettingsFailure) {
        Toast.makeText(this, getResources().getString(R.string.settings_not_set), Toast.LENGTH_LONG).show();
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
}