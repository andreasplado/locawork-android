package org.locawork.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.ControllerUpdateUserRole;
import org.locawork.EventRoleSelected;
import org.locawork.MainActivity;
import org.locawork.R;
import org.locawork.SignInActivity;
import org.locawork.model.Settings;
import org.locawork.util.AnimationUtil;
import org.locawork.util.AppConstants;
import org.locawork.util.BiometricUtil;
import org.locawork.util.ConverterUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;

import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

import static org.locawork.util.PrefConstants.FILTER_JOB_OFFER;
import static org.locawork.util.PrefConstants.ROLE;
import static org.locawork.util.PrefConstants.TAG_RADIUS;
import static org.locawork.util.PreferencesUtil.KEY_EMAIL;
import static org.locawork.util.PreferencesUtil.KEY_TOKEN;
import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

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
    private Button changeRole;
    private NavigationView navigationView;
    private CheckBox cbEnableBiometric;
    private BiometricUtil biometricUtil;

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
        filterGroup = root.findViewById(R.id.rg_job_filter_group);
        role = headerView.findViewById(R.id.nav_role);
        this.cbShowInformationOnStartup = root.findViewById(R.id.show_information_in_startup);
        this.cbAskPermissionBeforeDeletingJob = root.findViewById(R.id.ask_permission_before_deleting_job);
        this.settingsView = root.findViewById(R.id.settings_view);
        profileImageView = root.findViewById(R.id.profile_image);
        this.changeRole = root.findViewById(R.id.change_role);
        this.name = root.findViewById(R.id.name);
        this.email = root.findViewById(R.id.email);
        this.retry = root.findViewById(R.id.retry);
        this.contact = root.findViewById(R.id.contact);
        noSettingsView = root.findViewById(R.id.no_data_found_layout);

        if (PreferencesUtil.readString(getContext(), PrefConstants.ROLE, "").equals(AppConstants.ROLE_JOB_OFFER)) {
            role.setText(getResources().getString(R.string.work_offer));
        }else{
            role.setText(getResources().getString(R.string.work_seeker));
        }
        this.changeRole.setOnClickListener(v -> {
            ControllerUpdateUserRole controllerUpdateUserRole = new ControllerUpdateUserRole();
            controllerUpdateUserRole.postData(getContext(), PreferencesUtil.readString(getContext(), FILTER_JOB_OFFER, "" ),
                    PreferencesUtil.readInt(getContext(), FILTER_JOB_OFFER, 0 ));
        });

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
        cbShowInformationOnStartup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerUpdateShowInformationOnStartup controllerUpdateShowInformationOnStartup = new ControllerUpdateShowInformationOnStartup();
                if (cbShowInformationOnStartup.isChecked()) {
                    controllerUpdateShowInformationOnStartup.update(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)), true);
                    PreferencesUtil.save(getContext(), PrefConstants.TAG_IS_NOTIFICATION, "1");

                    return;
                }
            }
        });

        this.logout.setOnClickListener(v -> {
            new ControllerLogout().postData(getContext(), Integer.valueOf(PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)));
        });

        radiusSlider.setEndTrackingListener(() -> {
            double converted = radiusSlider.getPosition();
            PreferencesUtil.save(getContext(), TAG_RADIUS, converted);

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
        PreferencesUtil.save(getContext(), PrefConstants.ROLE, "");
        startActivity(new Intent(getContext(), MainActivity.class));
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
        startActivity(new Intent(getContext(), MainActivity.class));
        Toast.makeText(getContext(), getResources().getString(R.string.biometric_security_has_been_enabled), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void updateBiometricFailure(EventUpdateBiometricFailure eventUpdateBiometricFailure){
        Toast.makeText(getContext(), getResources().getString(R.string.biometric_not_enabled_please_try_again), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventSettingsSuccess(EventSettingsEditSuccess eventSettingsEditSuccess) {
        this.noSettingsView.setVisibility(View.GONE);
        this.settingsView.setVisibility(View.VISIBLE);
        Settings settings = eventSettingsEditSuccess.getSettings().body();
        double radiusRounded = (int) (radiusSlider.getPosition() * 100);
        this.name.setText(eventSettingsEditSuccess.getSettings().body().getFullname());
        this.email.setText(eventSettingsEditSuccess.getSettings().body().getEmail());
        this.contact.setText(eventSettingsEditSuccess.getSettings().body().getContact());
        Float lol = eventSettingsEditSuccess.getSettings().body().getRadius().floatValue() / 100;
        this.radiusSlider.setPosition(lol);


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
        PreferencesUtil.save(getContext(), ROLE, "");
        getActivity().finish();
        startActivity(new Intent(getContext(), SignInActivity.class));
    }

    @Subscribe
    public void eventUpdateRadiusNotValid(EventUpdateRadiusNotValid eventUpdateRadiusNotValid) {
        Toast.makeText(getContext(), getResources().getString(R.string.the_query_is_not_valid), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventUpdateRadiusSuccess(EventUpdateRadiusSuccess eventUpdateRadiusSuccess){
        int radiusRounded = new Double(eventUpdateRadiusSuccess.getResponse().body().getRadius()).intValue();
        radiusText.setText(String.valueOf(radiusRounded));
        Toast.makeText(getContext(), getResources().getString(R.string.radius_not_successfully_updated), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventUpdateRadiusError(EventUpdateRadiusNotValid eventUpdateRadiusNotValid) {
        Toast.makeText(getContext(), getResources().getString(R.string.event_updated_radius_error), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void eventUpdateRadiusError(EventUpdateRadiusFailure eventUpdateRadiusNotValid) {
        Toast.makeText(getContext(), getResources().getString(R.string.radius_update_failed), Toast.LENGTH_LONG).show();
    }


    @Subscribe
    public void eventLogoutFailure(EventLogoutFailure logoutFailure) {
        Toast.makeText(getContext(), getResources().getString(R.string.logout_error_please_try_again), Toast.LENGTH_LONG).show();
    }

    private void saveViewByDefault() {
        assert getContext() != null;
    }
}
