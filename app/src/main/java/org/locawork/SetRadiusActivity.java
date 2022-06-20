package org.locawork;

import androidx.appcompat.app.AppCompatActivity;
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
import org.locawork.model.Settings;
import org.locawork.ui.settings.ControllerUpdateRadius;
import org.locawork.ui.settings.EventSettingsFailure;
import org.locawork.ui.settings.EventSettingsSuccess;
import org.locawork.ui.settings.EventUpdateRadiusFailure;
import org.locawork.ui.settings.EventUpdateRadiusNotValid;
import org.locawork.ui.settings.EventUpdateRadiusSuccess;
import org.locawork.util.PreferencesUtil;
import static org.locawork.util.PrefConstants.TAG_RADIUS;
import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class SetRadiusActivity extends AppCompatActivity {

    private FluidSlider radiusSlider;
    private ImageButton accept;
    private ControllerUpdateRadius controllerUpdateRadius = new ControllerUpdateRadius();
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
            double converted = radiusSlider.getPosition();

            PreferencesUtil.save(this, TAG_RADIUS, converted);

            accept.setVisibility(View.VISIBLE);
            return Unit.INSTANCE;
        });

        accept.setOnClickListener(v -> {
            controllerUpdateRadius.update(this, PreferencesUtil.readInt(this, KEY_USER_ID, 0), (double) radiusSlider.getPosition());
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
    public void eventUpdateRadiusSuccess(EventUpdateRadiusSuccess eventUpdateRadiusSuccess){
        finish();
        startActivity(new Intent(SetRadiusActivity.this, MainActivity.class));
    }
    @Subscribe
    public void eventUpdateRadiusSettingsNotSet(EventUpdateRadiusNotValid eventUpdateRadiusSuccess){
        finish();
        startActivity(new Intent(SetRadiusActivity.this, MainActivity.class));
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
        this.radiusSlider.setPosition((float) PreferencesUtil.readDouble(this, TAG_RADIUS, 1));
        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(v -> {
            finish();
            Intent i = new Intent(SetRadiusActivity.this, MainActivity.class);
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