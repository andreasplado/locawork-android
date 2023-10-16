package ee.locawork.ui.settings;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ee.locawork.ActivityNotification;
import ee.locawork.ActivitySetRadius;
import ee.locawork.ControllerUpdateFirebaseToken;
import ee.locawork.EventRoleSelected;
import ee.locawork.ActivityMain;
import ee.locawork.R;
import ee.locawork.event.EventNetOn;
import ee.locawork.services.LocaworkFirebaseMessagingService;
import ee.locawork.ui.login.ActivityLogin;
import ee.locawork.model.Settings;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.AppConstants;
import ee.locawork.util.BiometricUtil;
import ee.locawork.util.ClickUtils;
import ee.locawork.util.FragmentUtils;
import ee.locawork.util.PrefConstants;
import ee.locawork.util.PreferencesUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import retrofit2.Response;

import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;
import static ee.locawork.util.PreferencesUtil.KEY_CARD_PARAMS;
import static ee.locawork.util.PreferencesUtil.KEY_COMPANY_NAME;
import static ee.locawork.util.PreferencesUtil.KEY_COMPANY_REG_NUMBER;
import static ee.locawork.util.PreferencesUtil.KEY_EMAIL;
import static ee.locawork.util.PreferencesUtil.KEY_ID_CODE;
import static ee.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;
import static ee.locawork.util.PreferencesUtil.KEY_RADIUS;
import static ee.locawork.util.PreferencesUtil.KEY_ROLE;
import static ee.locawork.util.PreferencesUtil.KEY_TOKEN;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentSettings extends Fragment {

    private CheckBox cbAskPermissionBeforeDeletingJob;
    private CheckBox cbShowInformationOnStartup;
    private RadioGroup filterGroup;
    private FluidSlider radiusSlider;
    private ImageButton logout, retry;
    private TextView email, contact, name, role;
    private LinearLayout noSettingsView, yourUserDontHaveCompanyView;
    private CircleImageView profileImageView;
    private LinearLayout settingsView, noDataLayout;
    private ControllerUpdateRadius controllerUpdateRadius = new ControllerUpdateRadius();
    private View headerView;
    private TextView radiusText;
    private NavigationView navigationView;
    private CheckBox cbEnableBiometric;
    private BiometricUtil biometricUtil;
    private TextView customerId;
    private LinearLayout customerIdLayout, companySettingsView;
    private TextView tvNoCustomer, idCode;
    private ImageButton copyCustomerId;
    private RelativeLayout loadingView;
    private TextView companyRegNumber, companyName;
    private LinearLayout serverErrorView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        assert getActivity() != null;

        navigationView = getActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        radiusText = headerView.findViewById(R.id.nav_radius);
        logout = root.findViewById(R.id.log_out);
        companyRegNumber = root.findViewById(R.id.company_registration_number);
        companyName = root.findViewById(R.id.company_name);
        radiusSlider = root.findViewById(R.id.radius_slider);
        cbEnableBiometric = root.findViewById(R.id.biometric_auth);
        companySettingsView = root.findViewById(R.id.company_settings_view);
        serverErrorView = root.findViewById(R.id.server_error_view);
        yourUserDontHaveCompanyView = root.findViewById(R.id.your_user_dont_have_company_view);
        idCode = root.findViewById(R.id.id_code);
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
        new ControllerGetSettings().getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        return root;
    }

    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Subscribe
    public void settingsSuccess(EventGetSettingsSuccess eventSettingsSuccess) {
        Settings settings = eventSettingsSuccess.getSettings().body();
        this.noSettingsView.setVisibility(View.GONE);
        this.settingsView.setVisibility(View.VISIBLE);
        this.name.setText(settings.getFullname());

        String idCodeText = PreferencesUtil.readString(getContext(), KEY_ID_CODE, "");
        this.idCode.setText(idCodeText);

        String companyNameText = PreferencesUtil.readString(getContext(), KEY_COMPANY_NAME, "");
        String companyRegNumberText = PreferencesUtil.readString(getContext(), KEY_COMPANY_REG_NUMBER, "");
        if(!companyNameText.equals("")){
            companyRegNumber.setText(companyRegNumberText);
            companyName.setText(companyNameText);
            companySettingsView.setVisibility(View.VISIBLE);
            yourUserDontHaveCompanyView.setVisibility(View.GONE);
        }else{
            companySettingsView.setVisibility(View.GONE);
            yourUserDontHaveCompanyView.setVisibility(View.VISIBLE);
        }
        String idCode = PreferencesUtil.readString(getContext(), KEY_ID_CODE, "");
        this.idCode.setText(idCode);
        this.email.setText(settings.getEmail());
        this.contact.setText(settings.getContact());
        Float radiusPosition = settings.getRadius().floatValue() / 100;
        this.radiusSlider.setPosition(radiusPosition);
        if(settings.getCustomerId() == null || settings.getCustomerId().equals("")){
            customerIdLayout.setVisibility(View.GONE);
            tvNoCustomer.setVisibility(View.VISIBLE);
            copyCustomerId.setVisibility(View.GONE);
            customerId.setText(settings.getCustomerId());
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
        loadingView.setVisibility(View.GONE);
    }


    @Subscribe
    public void settingsFailure(EventSettingsFailure eventSettingsFailure) {
        serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void eventNetOn(EventNetOn eventNetOn) {
        new ControllerGetSettings().getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        serverErrorView.setVisibility(View.GONE);
    }



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
    public void eventGetSettingsFailure(EventGetSettingsFailure eventGetSettingsFailure) {
        serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void eventSettingsFailure(EventSettingsEditFailure eventSettingsEditFailure) {
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
        startActivity(new Intent(getContext(), ActivityLogin.class));
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
