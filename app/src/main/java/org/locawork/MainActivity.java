package org.locawork;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.android.preloader.PreLoader;
import com.billy.android.preloader.interfaces.DataListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ramotion.fluidslider.FluidSlider;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.model.PaymentMethod;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.alert.AlertAddJob;
import org.locawork.alert.AlertLocationOff;
import org.locawork.event.LocationAdd;
import org.locawork.model.pushnotification.NotificationRequestDto;
import org.locawork.model.pushnotification.SubscriptionRequestDto;
import org.locawork.services.EventRetrieveToken;
import org.locawork.services.EventRetrieveTokenFail;
import org.locawork.services.LocaworkFirebaseMessagingService;
import org.locawork.services.ServiceReachedJob;
import org.locawork.ui.findjob.EventGPSFailure;
import org.locawork.ui.findjob.EventGPSuccess;
import org.locawork.ui.pushnotification.ControllerSendPushNotification;
import org.locawork.ui.pushnotification.ControllerSubscribe;
import org.locawork.ui.pushnotification.EventSendPushNotificationSuccess;
import org.locawork.ui.pushnotification.EventSusbscribeSuccess;
import org.locawork.ui.settings.ControllerLogout;
import org.locawork.ui.settings.EventLogoutFailure;
import org.locawork.ui.settings.EventLogoutSuccess;
import org.locawork.ui.settings.EventSettingsFailure;
import org.locawork.ui.settings.EventSettingsNotSet;
import org.locawork.ui.settings.EventSettingsSuccess;
import org.locawork.util.AddsUtil;
import org.locawork.util.AnimationUtil;
import org.locawork.util.AppConstants;
import org.locawork.util.BillyPreloader;
import org.locawork.util.BiometricUtil;
import org.locawork.util.Keyboard;
import org.locawork.util.LoaderListener;
import org.locawork.util.LocationUtil;
import org.locawork.util.PaymentUtil;
import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;
import org.locawork.util.UpdateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static org.locawork.permission.GPSPFingerprintAndCameraPermission.PERMISSION_TAG_GPS_AND_CAMERA;
import static org.locawork.util.PrefConstants.ROLE;
import static org.locawork.util.PreferencesUtil.KEY_EMAIL;
import static org.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;
import static org.locawork.util.PreferencesUtil.KEY_TOKEN;
import static org.locawork.util.PreferencesUtil.KEY_USER_ID;
import static org.locawork.util.UpdateUtil.APP_UPDATE_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private static final int LOCK_REQUEST_CODE = 317;
    private static Snackbar snackbar = null;

    public NavigationView navigationView;
    private int biometricTries = 0;
    private TextView tvEmail;
    private TextView tvRadius, tvrole;
    private LinearLayout findJobButton, offerJoButton, pleaseRetryToGetSettings;
    private ImageButton retry;
    private UpdateUtil updateUtil = new UpdateUtil();
    private String firebaseToken = "";
    private ImageButton addJob, logout;
    private AppBarConfiguration appBarConfiguration;
    private TextView cannotFetchCurrentLocation;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavController navController;
    private boolean doubleBackToExitPressedOnce = false;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricUtil biometricUtil;
    private AdView adView;
    private LinearLayout adLayout;
    private Toolbar toolbar;
    private RelativeLayout container;
    private KeyguardManager keyGuardManager;
    private boolean isUnlocked = false;
    private PaymentUtil paymentUtil;
    private PaymentSession paymentSession;
    private ControllerCheckStatus controllerCheckStatus = new ControllerCheckStatus();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        processHaveReachedJobIntent(getIntent());

        adView = findViewById(R.id.adView);
        adLayout = findViewById(R.id.ad_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        cannotFetchCurrentLocation = findViewById(R.id.cannot_fetch_current_location);
        addJob = findViewById(R.id.add_job);
        pleaseRetryToGetSettings = findViewById(R.id.please_retry_to_get_settings);
        retry = findViewById(R.id.retry);
        biometricUtil = new BiometricUtil(this);
        container = findViewById(R.id.main_container);
        keyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
            }
        });

        addJob.setOnClickListener(v -> AlertAddJob.init(MainActivity.this, getApplicationContext()));
        AddsUtil.initialzeAdd(this, adView);
        retry.setOnClickListener(v -> {
            AnimationUtil.animateBubble(v);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        updateUtil.init(this, getApplicationContext());

        if (navigationView.getHeaderView(0) != null) {
            View headerView = navigationView.getHeaderView(0);

            tvEmail = headerView.findViewById(R.id.nav_email);
            tvRadius = headerView.findViewById(R.id.nav_radius);
            tvrole = headerView.findViewById(R.id.nav_role);
            logout = headerView.findViewById(R.id.logout);

            logout.setOnClickListener(v -> {
                new ControllerLogout().postData(this, PreferencesUtil.readInt(this, KEY_USER_ID, 0));
            });
            tvEmail.setText(PreferencesUtil.readString(this, KEY_EMAIL, getResources().getString(R.string.undefined_email)));
        }
        drawer = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_find_job, R.id.nav_view_added_jobs, R.id.nav_my_applications, R.id.nav_my_done_work, R.id.nav_my_upcoming_work, R.id.nav_my_candidates, R.id.nav_settings, R.id.nav_report, R.id.nav_about, R.id.nav_subscribe).setOpenableLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mDrawerToggle = new ActionBarDrawerToggle(this, this.drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
        };

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {


            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            public void onDrawerOpened(View drawerView) {
                if (((InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).isAcceptingText()) {
                    Keyboard.hideKeyboard(MainActivity.this);
                }
            }

            public void onDrawerClosed(View drawerView) {
            }

            public void onDrawerStateChanged(int newState) {
            }
        });
        LocationUtil locationUtil = new LocationUtil(this, getApplicationContext());
        locationUtil.init();

        if (locationUtil.lococation == null) {
            this.addJob.setVisibility(View.GONE);
            this.cannotFetchCurrentLocation.setVisibility(View.VISIBLE);
            return;
        }
        this.cannotFetchCurrentLocation.setVisibility(View.GONE);

    }

    private void loadData() {

        int preLoaderId = PreLoader.preLoad(new BillyPreloader());
        Intent intent = new Intent(this, ActivityPreloader.class);
        intent.putExtra("preLoaderId", preLoaderId);
        startActivity(intent);
    }

    private void showBiometricPrompt() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.login_to_locawork))
                .setSubtitle(getResources().getString(R.string.login_using_your_biometric_credentials))
                .setNegativeButtonText(getResources().getString(R.string.enter_phone_password))
                .setConfirmationRequired(true)
                .build();
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    loginWithPassword();
                } else if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    finish();
                }
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                biometricTries++;
                if (biometricTries > 3) {
                    startActivity(new Intent(MainActivity.this, ActivityBiometricAuthenticationError.class));
                }

            }
        });

        biometricPrompt.authenticate(promptInfo);
    }

    private void loginWithPassword() {
        Intent i = keyGuardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.locawork_protection_is_on), getResources().getString(R.string.please_log_in_with_phone_password));
        try {
            //Start activity for result
            startActivityForResult(i, LOCK_REQUEST_CODE);
        } catch (Exception e) {
            //If some exception occurs means Screen lock is not set up please set screen lock

        }
    }

    private void postToken(String token) {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(task -> {
                    String msg = "topic weather";
                    if (!task.isSuccessful()) {
                        msg = "no topic subscribed";
                    }
                    SubscriptionRequestDto subscriptionRequestDto = new SubscriptionRequestDto();
                    ArrayList<String> tokens = new ArrayList<>();
                    tokens.add(token);
                    subscriptionRequestDto.setTokens(tokens);
                    subscriptionRequestDto.setTopicName("weather");
                    new ControllerSubscribe(getApplicationContext(), subscriptionRequestDto, tokens);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            startActivity(new Intent(this, ActivityAppUpdated.class));
        }
        if (requestCode == LOCK_REQUEST_CODE && resultCode == RESULT_OK) {
            isUnlocked = true;
        }else{
            isUnlocked = false;
        }

        if (data != null) {
            paymentSession.handlePaymentData(requestCode, resultCode, data);
        }

    }

    @NonNull
    private PaymentSessionConfig createPaymentSessionConfig() {
        return new PaymentSessionConfig.Builder()

                // collect shipping information
                .setShippingInfoRequired(false)

                // collect shipping method
                .setShippingMethodsRequired(false)

                // specify the payment method types that the customer can use;
                // defaults to PaymentMethod.Type.Card
                .setPaymentMethodTypes(
                        Arrays.asList(PaymentMethod.Type.Card)
                )

                // only allow US and Canada shipping addresses
                .setAllowedShippingCountryCodes(new HashSet<>(
                        Arrays.asList("US", "CA", "EE")
                ))

                // specify a layout to display under the payment collection form
                .setAddPaymentMethodFooter(R.layout.add_payment_method_footer)

                // if `true`, will show "Google Pay" as an option on the
                // Payment Methods selection screen
                .setShouldShowGooglePay(true)
                .build();
    }




    @Subscribe
    public void onMessage(EventSendPushNotificationSuccess eventSendPushNotificationSuccess) {
        String content = eventSendPushNotificationSuccess.getBody();
        Toast.makeText(this, eventSendPushNotificationSuccess.getBody(), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void sendPushNotification(EventSusbscribeSuccess eventSusbscribeSuccess) {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setTarget(PreferencesUtil.readString(this, KEY_PUSH_NOTIFICATION_TOKEN, ""));
        notificationRequestDto.setBody("Lol");
        notificationRequestDto.setTitle("LOL");
        new ControllerSendPushNotification(getApplicationContext(), notificationRequestDto);
    }

    @Subscribe
    public void setAddLocation(LocationAdd locationAdd) {
    }

    @Subscribe
    public void retrievePushNotificationToken(EventRetrieveToken eventRetrieveToken) {
        //Toast.makeText(this, "Token retrieved", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void retrievePushNotificationTokenFailure(EventRetrieveTokenFail eventRetrieveTokenFail) {
        //Toast.makeText(this, "Token failed to retrieve", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_TAG_GPS_AND_CAMERA) {
            int len = grantResults.length;
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.addJob.setVisibility(View.VISIBLE);
                this.cannotFetchCurrentLocation.setVisibility(View.GONE);

            } else {
                AlertLocationOff.init(this, this);
            }
        }
    }

    private void processHaveReachedJobIntent(Intent intent) {
        boolean haveReachedJob = false;
        if (intent.getExtras() != null) {
            haveReachedJob = intent.getExtras().getBoolean(ServiceReachedJob.KEY_APPLY_JOB, false);
        }
        if (haveReachedJob) {
            startActivity(new Intent(this, StartJobActivity.class));
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processHaveReachedJobIntent(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), this.appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        new ControllerGetSettings().getData(this, PreferencesUtil.readInt(this, KEY_USER_ID, 0));
        super.onStart();
    }

    @Subscribe
    public void settingsNotSet(EventSettingsNotSet eventSettingsNotSet) {
        if (PreferencesUtil.readString(this, KEY_TOKEN, "").equals("")) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    @Subscribe
    public void settingsSuccess(EventSettingsSuccess eventSettingsSuccess) {
        double radiusConverted = eventSettingsSuccess.getSettings().body().getRadius();
        String radius = String.valueOf((int) radiusConverted);
        tvRadius.setText(radius);
        pleaseRetryToGetSettings.setVisibility(View.GONE);
        int hasGPSFineAllowed = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int hasGPSCoarseAllowed = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        assert eventSettingsSuccess.getSettings().body() != null;

        if (eventSettingsSuccess.getSettings().body().isBiometric()) {
            if (biometricUtil.isBiometricAvailable(this, this)) {
                if(!isUnlocked) {
                    executor = ContextCompat.getMainExecutor(this);
                    showBiometricPrompt();
                }
            }
        }
        if (eventSettingsSuccess.getSettings().body().getRole() == null) {

        } else {
            if (navigationView.getHeaderView(0) != null) {
                MenuItem navFindJob = this.navigationView.getMenu().findItem(R.id.nav_work_seeker);
                MenuItem navOfferWork = this.navigationView.getMenu().findItem(R.id.nav_work_offer);
                if (eventSettingsSuccess.getSettings().body().getRole().equals(AppConstants.ROLE_JOB_SEEKER)) {
                    tvrole.setText(getResources().getString(R.string.work_seeker));
                    navFindJob.setVisible(true);
                    navOfferWork.setVisible(false);
                    addJob.setVisibility(View.GONE);
                    PreferencesUtil.save(this, ROLE, AppConstants.ROLE_JOB_SEEKER);
                } else if (eventSettingsSuccess.getSettings().body().getRole().equals(AppConstants.ROLE_JOB_OFFER)) {
                    tvrole.setText(getResources().getString(R.string.work_offer));
                    addJob.setVisibility(View.VISIBLE);
                    navFindJob.setVisible(false);
                    navOfferWork.setVisible(true);
                    PreferencesUtil.save(this, ROLE, AppConstants.ROLE_JOB_OFFER);
                }
            }
        }

        if (eventSettingsSuccess.getSettings().body().getRadius() == null) {
            finish();
            startActivity(new Intent(this, SetRadiusActivity.class));
        }
        if (PreferencesUtil.readString(this, PrefConstants.TAG_IS_NOTIFICATION, FluidSlider.TEXT_START).equals(FluidSlider.TEXT_START)) {
            finish();
            startActivity(new Intent(this, NotificationActivity.class));
        }
        if (PreferencesUtil.readString(this, KEY_TOKEN, "").equals("")) {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        } else {
            if (hasGPSFineAllowed != 0 && hasGPSCoarseAllowed != 0) {
                finish();
                startActivity(new Intent(this, LocationPermissionActivity.class));
            }
        }
        startService(new Intent(this, LocaworkFirebaseMessagingService.class));

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String token = instanceIdResult.getToken();
            if (!PreferencesUtil.readString(this, KEY_PUSH_NOTIFICATION_TOKEN, "").equals(token)) {
                PreferencesUtil.save(this, KEY_PUSH_NOTIFICATION_TOKEN, token);
                new ControllerUpdateFirebaseToken(this, PreferencesUtil.readInt(this, KEY_USER_ID, 0), token);
            } else {
                PreferencesUtil.save(this, KEY_PUSH_NOTIFICATION_TOKEN, token);
            }
        });

    }

    @Subscribe
    public void eventUpdateFirebaseTokenSuccess(EventUpdateFirebaseTokenSuccess eventUpdateFirebaseTokenSuccess) {
        Toast.makeText(this, "Uuendati push notificationi tokenit", Toast.LENGTH_LONG).show();
    }


    @Subscribe
    public void settingsFailure(EventSettingsFailure eventSettingsFailure) {
        pleaseRetryToGetSettings.setVisibility(View.VISIBLE);
    }

    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (PreferencesUtil.readInt(this, ServiceReachedJob.KEY_HAVE_REACHED, 0) == 1) {
            startActivity(new Intent(this, StartJobActivity.class));
        }
    }

    @Override
    public void onPause() {
        isUnlocked = false;
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        }
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Subscribe
    public void gpsSuccess(EventGPSuccess eventGPSuccess) {

    }

    @Subscribe
    public void gpsFailure(EventGPSFailure eventGPSFailure) {
        AlertLocationOff.init(this, this);
    }

    @Subscribe
    public void setMainData(EventGetMainData eventGetMainData) {
        /*MenuItem myUpcomingWorkCounter = this.navigationView.getMenu().findItem(R.id.nav_my_upcoming_work);
        MenuItem myCandidatesCounter = this.navigationView.getMenu().findItem(R.id.nav_my_candidates);

        if (eventGetMainData.getResponse().body().getMyUpcomingWorkNumber() > 1) {
            myUpcomingWorkCounter.setTitle(eventGetMainData.getResponse().body().getMyUpcomingWorkNumber() + " " + getResources().getString(R.string.upcoming_work));
        }
        if (eventGetMainData.getResponse().body().getMyCandidatesNumber() > 1) {
            myCandidatesCounter.setTitle(eventGetMainData.getResponse().body().getMyCandidatesNumber() + " " + getResources().getString(R.string.candidates));
        }
        if (eventGetMainData.getResponse().body().getMyCandidatesNumber() == 1) {
            myCandidatesCounter.setTitle(eventGetMainData.getResponse().body().getMyCandidatesNumber() + " " + getResources().getString(R.string.candidate));
        }
        if (eventGetMainData.getResponse().body().getMyUpcomingWorkNumber() == 1) {
            myUpcomingWorkCounter.setTitle(eventGetMainData.getResponse().body().getMyUpcomingWorkNumber() + " " + getResources().getString(R.string.upcoming_work));
        }*/

    }

    @Subscribe
    public void eventCheckIfUserExists(EventCheckIfUserExists eventCheckIfUserExists) {
        if (eventCheckIfUserExists.getBody() != null && !eventCheckIfUserExists.getBody().booleanValue()) {
            startActivity(new Intent(this, SignInActivity.class));
        }
        if (!PreferencesUtil.readString(this, ServiceReachedJob.KEY_JOB_TITLE, "").equals("")) {
            startActivity(new Intent(this, StartJobActivity.class));
        }

    }

    @Subscribe
    public void eventLogout(EventLogoutSuccess addedJobsNetSuccess) {
        PreferencesUtil.save(this, KEY_TOKEN, "");
        PreferencesUtil.save(this, KEY_EMAIL, "");
        PreferencesUtil.save(this, KEY_USER_ID, 0);
        PreferencesUtil.save(this, ROLE, "");
        finish();
        startActivity(new Intent(this, SignInActivity.class));
    }

    @Subscribe
    public void eventLogoutFailure(EventLogoutFailure logoutFailure) {
        Toast.makeText(this, getResources().getString(R.string.logout_error_please_try_again), Toast.LENGTH_LONG).show();
    }

    public static void snack(HashMap<String, View.OnClickListener> actions, String message, Activity context, View view) {
        snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}