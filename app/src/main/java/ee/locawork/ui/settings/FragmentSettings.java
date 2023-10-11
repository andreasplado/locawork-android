package ee.locawork.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ee.locawork.EventRoleSelected;
import ee.locawork.ActivityMain;
import ee.locawork.R;
import ee.locawork.ui.login.LoginActivity;
import ee.locawork.model.Settings;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.AppConstants;
import ee.locawork.util.BiometricUtil;
import ee.locawork.util.ClickUtils;
import ee.locawork.util.FragmentUtils;
import ee.locawork.util.PrefConstants;
import ee.locawork.util.PreferencesUtil;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;
import static ee.locawork.util.PreferencesUtil.KEY_EMAIL;
import static ee.locawork.util.PreferencesUtil.KEY_RADIUS;
import static ee.locawork.util.PreferencesUtil.KEY_TOKEN;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentSettings extends Fragment {

    private CheckBox cbAskPermissionBeforeDeletingJob;
    private CheckBox cbShowInformationOnStartup;
    private RadioGroup filterGroup;
    private FluidSlider radiusSlider;
    private ImageButton logout, retry;
    private TextView email, contact, name, role;
    private LinearLayout noSettingsView;
    private CircleImageView profileImageView;
    private LinearLayout settingsView, noDataLayout;
    private ControllerUpdateRadius controllerUpdateRadius = new ControllerUpdateRadius();
    private View headerView;
    private TextView radiusText;
    private NavigationView navigationView;
    private CheckBox cbEnableBiometric;
    private BiometricUtil biometricUtil;

    private TextView customerId;
    private LinearLayout customerIdLayout;

    private TextView tvNoCustomer;

    private ImageButton copyCustomerId;
    private RelativeLayout loadingView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        assert getActivity() != null;

        navigationView = getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        radiusText = headerView.findViewById(R.id.nav_radius);
        logout = root.findViewById(R.id.log_out);
        radiusSlider = root.findViewById(R.id.radius_slider);
        cbEnableBiometric = root.findViewById(R.id.biometric_auth);
        role = headerView.findViewById(R.id.nav_role);
        this.cbShowInformationOnStartup = root.findViewById(R.id.show_information_in_startup);
        this.cbAskPermissionBeforeDeletingJob = root.findViewById(R.id.ask_permission_before_deleting_job);
        this.settingsView = root.findViewById(R.id.settings_view);
        profileImageView = root.findViewById(R.id.profile_image);
        this.copyCustomerId = root.findViewById(R.id.copy_customer_id);
        this.name = root.findViewById(R.id.name);
        this.email = root.findViewById(R.id.email);
        this.customerIdLayout = root.findViewById(R.id.customer_id_layout);
        this.retry = root.findViewById(R.id.retry);
        this.contact = root.findViewById(R.id.contact);
        this.customerId = root.findViewById(R.id.customer_id);
        noSettingsView = root.findViewById(R.id.no_data_found_layout);
        tvNoCustomer = root.findViewById(R.id.no_active_customer_id);
        this.loadingView = root.findViewById(R.id.loading_view);

        return root;
    }

    @Override
    public void onStart() {
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        retry.setOnClickListener(v -> {
            AnimationUtil.animateBubble(v);
            FragmentUtils.restartFragment(FragmentSettings.this);
        });

        new ControllerGetSettings().getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));

        cbAskPermissionBeforeDeletingJob.setOnClickListener(v -> {
            ControllerUpdateAskPermissionBeforeDeletingWork controllerUpdateAskPermissionBeforeDeletingWork = new ControllerUpdateAskPermissionBeforeDeletingWork();
            if (cbAskPermissionBeforeDeletingJob.isChecked()) {
                controllerUpdateAskPermissionBeforeDeletingWork.update(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)), true);
                PreferencesUtil.save(getContext(), PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, "1");

                return;
            }
            controllerUpdateAskPermissionBeforeDeletingWork.update(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)), false);
            PreferencesUtil.save(getContext(), PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START);
        });

        cbEnableBiometric.setOnClickListener(v -> {
            ControllerUpdateBiometric controllerUpdateBiometric = new ControllerUpdateBiometric();
            controllerUpdateBiometric.update(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)), cbEnableBiometric.isChecked());
        });
        copyCustomerId.setOnClickListener(v -> {
            ClickUtils.copyText(getContext(), AppConstants.APP_NAME, customerId.getText().toString());
        });
        cbShowInformationOnStartup.setOnClickListener(v -> {
            ControllerUpdateShowInformationOnStartup controllerUpdateShowInformationOnStartup = new ControllerUpdateShowInformationOnStartup();
            if (cbShowInformationOnStartup.isChecked()) {
                controllerUpdateShowInformationOnStartup.update(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)), true);
                PreferencesUtil.save(getContext(), PrefConstants.TAG_IS_NOTIFICATION, "1");

                return;
            }
        });

        this.logout.setOnClickListener(v -> {
            new ControllerLogout().postData(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)));
        });

        radiusSlider.setEndTrackingListener(() -> {
            double converted = radiusSlider.getPosition();
            PreferencesUtil.save(getContext(), KEY_RADIUS, converted);

            double convertedResult = radiusSlider.getPosition() * 100;
            controllerUpdateRadius.update(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0), convertedResult);

            return Unit.INSTANCE;
        });

        super.onStart();
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void eventRoleSelected(EventRoleSelected eventRoleSelected){
        PreferencesUtil.save(getContext(), PrefConstants.KEY_LOCAWORK_PREFS, "");
        startActivity(new Intent(getContext(), ActivityMain.class));
    }

    private void setFilters() {
    }

    @Subscribe
    public void eventSettingsNotSet(EventSettingsNotSet addedJobsNetSuccess) {
        this.noSettingsView.setVisibility(View.VISIBLE);
        this.settingsView.setVisibility(View.GONE);
    }

    @Subscribe
    public void updateBiometricSuccess(EventUpdateBiometricSuccess eventUpdateBiometricSuccess){
        startActivity(new Intent(getContext(), ActivityMain.class));
        Settings settings = eventUpdateBiometricSuccess.getSettingsResponse().body();
        if(settings.isBiometric()){
            Toast.makeText(getContext(), getResources().getString(R.string.biometric_security_has_been_enabled), Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), getResources().getString(R.string.biometric_security_has_been_disabled), Toast.LENGTH_LONG).show();
        }

    }

    @Subscribe
    public void updateBiometricFailure(EventUpdateBiometricFailure eventUpdateBiometricFailure){
        Toast.makeText(getContext(), getResources().getString(R.string.biometric_not_enabled_please_try_again), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventSettingsSuccess(EventSettingsEditSuccess eventSettingsEditSuccess) {
        loadingView.setVisibility(View.GONE);
        Settings settings = eventSettingsEditSuccess.getSettings().body();
        this.noSettingsView.setVisibility(View.GONE);
        this.settingsView.setVisibility(View.VISIBLE);
        this.name.setText(settings.getFullname());
        this.email.setText(settings.getEmail());
        this.contact.setText(settings.getContact());
        Float radiusPosition = settings.getRadius().floatValue() / 100;
        this.radiusSlider.setPosition(radiusPosition);
        if(settings.getCustomerId() == null || settings.getCustomerId().equals("")){
            customerIdLayout.setVisibility(View.GONE);
            tvNoCustomer.setVisibility(View.VISIBLE);
            copyCustomerId.setVisibility(View.GONE);
        }else{
            tvNoCustomer.setVisibility(View.GONE);
            customerIdLayout.setVisibility(View.VISIBLE);
            copyCustomerId.setVisibility(View.VISIBLE);
        }




        if (settings.isAskPermissionsBeforeDeletingAJob()) {
            this.cbAskPermissionBeforeDeletingJob.setChecked(false);
        } else {
            this.cbAskPermissionBeforeDeletingJob.setChecked(true);
        }
        if (settings.isShowInformationOnStartup()) {
            this.cbShowInformationOnStartup.setChecked(true);
        } else {
            this.cbShowInformationOnStartup.setChecked(false);
        }
        if(settings.isBiometric()){
            this.cbEnableBiometric.setChecked(true);
        }else{
            this.cbEnableBiometric.setChecked(false);
        }
    }

    @Subscribe
    public void eventSettingsFailure(EventSettingsEditFailure eventSettingsSuccess) {
        this.settingsView.setVisibility(View.GONE);
        this.noSettingsView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void eventLogout(EventLogoutSuccess addedJobsNetSuccess) {
        PreferencesUtil.save(getContext(), KEY_TOKEN, "");
        PreferencesUtil.save(getContext(), KEY_EMAIL, "");
        PreferencesUtil.save(getContext(), KEY_USER_ID, 0);
        PreferencesUtil.save(getContext(), KEY_LOCAWORK_PREFS, "");
        getActivity().finish();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }


    @Subscribe
    public void eventLogoutFailure(EventLogoutFailure logoutFailure) {
        Toast.makeText(getContext(), getResources().getString(R.string.logout_error_please_try_again), Toast.LENGTH_LONG).show();
    }
    @Subscribe
    public void eventUpdateRadiusSuccess(EventUpdateRadiusSuccess eventUpdateRadiusSuccess){
        int radiusRounded = new Double(eventUpdateRadiusSuccess.getResponse().body().getRadius()).intValue();
        radiusText.setText(String.valueOf(radiusRounded));
        PreferencesUtil.save(getContext(), KEY_RADIUS, eventUpdateRadiusSuccess.getResponse().body().getRadius());
        Toast.makeText(getContext(), getResources().getString(R.string.radius_not_successfully_updated), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventUpdateRadiusError(EventUpdateRadiusFailure eventUpdateRadiusNotValid) {
        Toast.makeText(getContext(), getResources().getString(R.string.event_updated_radius_error), Toast.LENGTH_LONG).show();
    }


    private void saveViewByDefault() {
        assert getContext() != null;
    }
}