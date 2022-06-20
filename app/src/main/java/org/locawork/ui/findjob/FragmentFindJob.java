package org.locawork.ui.findjob;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.MarkerManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.sharewire.googlemapsclustering.Cluster;
import net.sharewire.googlemapsclustering.ClusterManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.ActivitySuccessfullyApplied;
import org.locawork.ControllerUpdateUserRole;
import org.locawork.EventRoleClear;
import org.locawork.EventRoleFailedToSelect;
import org.locawork.EventRoleSelected;
import org.locawork.LocationPermissionActivity;
import org.locawork.MainActivity;
import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.pushnotification.NotificationRequestDto;
import org.locawork.ui.addjobs.EventAddJobNetSuccess;
import org.locawork.ui.findjob.alert.AlertPermission;
import org.locawork.ui.findjob.alert.AlertViewJobInMap;
import org.locawork.ui.findjob.alert.EventEditJob;
import org.locawork.ui.pushnotification.ControllerGetJobOfferToken;
import org.locawork.ui.pushnotification.ControllerSendOffererPushNotification;
import org.locawork.ui.pushnotification.EventJobOffererToken;
import org.locawork.ui.pushnotification.EventSendOffererPushNotification;
import org.locawork.util.AnimationUtil;
import org.locawork.util.AppConstants;
import org.locawork.util.FragmentUtils;
import org.locawork.util.GooglemapUtil;
import org.locawork.util.LocationUtil;
import org.locawork.util.NetworkUtil;
import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static org.locawork.util.PrefConstants.ROLE;
import static org.locawork.util.PrefConstants.TAG_RADIUS;
import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentFindJob extends Fragment implements OnMapReadyCallback {
    public static final int ALL_PERMISSIONS_RESULT = 101;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 30;
    private static final float LOCATION_REFRESH_DISTANCE = 3.6f;
    private static final long LOCATION_REFRESH_TIME = 50000;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final String RADIUS = "radius";
    public Activity activity;
    private ImageButton btnApplyToJob;
    private ImageButton btnDeleteJob;
    private ImageButton btnUpdateJob;
    boolean canGetLocation = true;
    private ClusterManager<Job> clusterManager;
    public Context context;
    private ControllerFindJob controllerFindJob = new ControllerFindJob();
    private ControllerFindMyJobs controllerFindMyJobs = new ControllerFindMyJobs();
    private GoogleMap googleMap;
    private AlertDialog gpsDialog;
    private boolean isEventBusRegistred;
    private boolean isGPS;
    private boolean isNetOn;
    private ImageButton retry;
    private boolean isNetworkProvider;
    public TextView jobTitle;
    private List<Job> jobsList;
    private RelativeLayout loadingView;
    private Button btnSelectRole;
    private LocationManager locationManager;
    public RecyclerView multipleJobs;
    public SlidingUpPanelLayout multiplejobsPanelLayout;
    private AlertDialog networkDialog;
    private TextView noJobsFoundInfo;
    private LinearLayout noJobsFoundLayout;
    private ArrayList<String> permissions = new ArrayList<>();
    public ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private View root;
    private LinearLayout serverErrorView;
    private LocationUtil locationUtil;
    private GooglemapUtil googlemapUtil;
    private LinearLayout roleNotSelected, findJobLayout, offerJobLayout;
    private NavigationView navigationView;
    private View headerView;
    private TextView navRole;
    private ImageButton addJob;

    public Location loc;
    private LocationListener locationChangeListener = new LocationListener() {
        public void onLocationChanged(Location l) {
            if (l != null) {
                loc = l;
            }
        }

        public void onProviderEnabled(String p) {
        }

        public void onProviderDisabled(String p) {
        }

        public void onStatusChanged(String p, int status, Bundle extras) {
        }
    };
    private ControllerUpdateUserRole controllerUpdateUserRole = new ControllerUpdateUserRole();
    private TextView tvrole;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkProvider = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(this.permissions);


        googlemapUtil = new GooglemapUtil(locationUtil);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getContext();
        setInitialTilte();
        LocationUtil locationUtil = new LocationUtil(getActivity(), context);
        locationUtil.init();
        isNetOn = NetworkUtil.isNetworkAvailable(context);
        networkDialog = new AlertDialog.Builder(getContext()).create();
        gpsDialog = new AlertDialog.Builder(this.context).create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_find_jobs, container, false);
        this.root = inflate;
        this.multiplejobsPanelLayout = inflate.findViewById(R.id.sliding_layout);
        this.btnDeleteJob = this.root.findViewById(R.id.cancel_application);
        this.jobTitle = root.findViewById(R.id.job_title);
        this.retry = root.findViewById(R.id.retry);
        this.multipleJobs = root.findViewById(R.id.multiple_jobs);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.noJobsFoundLayout = root.findViewById(R.id.no_data_found_layout);
        this.noJobsFoundInfo = root.findViewById(R.id.no_jobs_found_info);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.btnSelectRole = root.findViewById(R.id.clear_role);
        roleNotSelected = root.findViewById(R.id.role_not_selected);
        findJobLayout = root.findViewById(R.id.find_job);
        offerJobLayout = root.findViewById(R.id.offer_job);
        this.navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        this.addJob = (ImageButton) getActivity().findViewById(R.id.add_job);
        this.headerView = navigationView.getHeaderView(0);
        tvrole = headerView.findViewById(R.id.nav_role);

        this.multipleJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        this.multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        setListenersToView();
        return this.root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.VISIBLE);

        this.noJobsFoundInfo = view.findViewById(R.id.no_jobs_found_info);
        if (PreferencesUtil.readString(context, ROLE, "").equals(AppConstants.ROLE_JOB_OFFER)) {
            roleNotSelected.setVisibility(View.GONE);
        } else if (PreferencesUtil.readString(context, ROLE, "").equals(AppConstants.ROLE_JOB_SEEKER)) {
            roleNotSelected.setVisibility(View.GONE);
        } else {

        }
        String role = PreferencesUtil.readString(getContext(), PrefConstants.ROLE, "");
        MenuItem navigate = this.navigationView.getMenu().findItem(R.id.nav_find_job);
        if (role.equals(AppConstants.ROLE_JOB_SEEKER)) {
            navigate.setTitle(getResources().getString(R.string.find_work));
            btnSelectRole.setText(getResources().getString(R.string.find_work));
            tvrole.setText(getResources().getString(R.string.find_work));
            addJob.setVisibility(View.GONE);
        } else if (role.equals(AppConstants.ROLE_JOB_OFFER)) {
            navigate.setTitle(getResources().getString(R.string.offer_work));
            btnSelectRole.setText(getResources().getString(R.string.offer_work));
            tvrole.setText(getResources().getString(R.string.offer_work));
            addJob.setVisibility(View.VISIBLE);
        } else {
            roleNotSelected.setVisibility(View.VISIBLE);
        }
        addMap();
    }

    @Subscribe
    public void eventRoleSelected(EventRoleSelected eventRoleSelected) {
        FragmentUtils.restartFragment(this);
        roleNotSelected.setVisibility(View.GONE);
        if (headerView != null) {
            MenuItem navFindJob = this.navigationView.getMenu().findItem(R.id.nav_work_seeker);
            MenuItem navOfferWork = this.navigationView.getMenu().findItem(R.id.nav_work_offer);
            MenuItem navigate = this.navigationView.getMenu().findItem(R.id.nav_find_job);

            String role = eventRoleSelected.getRole();
            if (role.equals(AppConstants.ROLE_JOB_OFFER)) {

                btnSelectRole.setText(getResources().getString(R.string.work_offer));
                tvrole.setText(getResources().getString(R.string.work_offer));
                addJob.setVisibility(View.VISIBLE);
                navFindJob.setVisible(false);
                navOfferWork.setVisible(true);
                navigate.setTitle(getResources().getString(R.string.find_work));
                PreferencesUtil.save(getContext(), PrefConstants.ROLE, AppConstants.ROLE_JOB_SEEKER);
                btnSelectRole.setText(getResources().getString(R.string.work_seeker));
                addJob.setVisibility(View.GONE);
            } else {
                btnSelectRole.setText(getResources().getString(R.string.work_seeker));
                tvrole.setText(getResources().getString(R.string.work_seeker));
                addJob.setVisibility(View.GONE);
                navFindJob.setVisible(false);
                navOfferWork.setVisible(false);
                navigate.setTitle(getResources().getString(R.string.offer_work));
                PreferencesUtil.save(getActivity(), PrefConstants.ROLE, AppConstants.ROLE_JOB_OFFER);
                btnSelectRole.setText(getResources().getString(R.string.work_offer));
                addJob.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe
    public void eventRoleFailedToSelect(EventRoleFailedToSelect eventRoleFailedToSelect) {
        Throwable throwable = eventRoleFailedToSelect.getT();
        Toast.makeText(getContext(), getResources().getString(R.string.role_failed_to_select_please_try_again), Toast.LENGTH_LONG).show();
    }

    private void setInitialTilte() {
        String myJobs = PreferencesUtil.readString(getContext(), PrefConstants.ROLE, "");

        if (myJobs.equals(PrefConstants.FILTER_JOB_SEEKER)) {
            ((MainActivity) activity).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.work_seeker));
        } else if (myJobs.equals(PrefConstants.FILTER_JOB_OFFER)) {
            ((MainActivity) activity).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.work_offer));
        }
    }

    private void requestPermissions() {
        if (this.permissionsToRequest.size() > 0) {
            ArrayList<String> arrayList = this.permissionsToRequest;
            requestPermissions(arrayList.toArray(new String[arrayList.size()]), 101);
            if (!isGPS && !isNetworkProvider) {
                getLastLocationPermissionCheck();
            } else if (Build.VERSION.SDK_INT >= 23) {
                canGetLocation = false;
            }
            getLocation();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions2, int[] grantResults) {
        if (requestCode == 101) {
            initData();
            Iterator<String> it = this.permissionsToRequest.iterator();
            while (it.hasNext()) {
                String perms = it.next();
                if (hasPermission(perms)) {
                    this.permissionsRejected.add(perms);
                }
            }
            if (this.permissionsRejected.size() <= 0) {
                canGetLocation = true;
            } else if (Build.VERSION.SDK_INT >= 23 && shouldShowRequestPermissionRationale(this.permissionsRejected.get(0))) {
                AlertPermission.init(getActivity().getResources().getString(R.string.cannot_fetch_current_location), (dialog, which) -> {
                    FragmentFindJob fragmentFindJob = FragmentFindJob.this;
                    fragmentFindJob.requestPermissions(fragmentFindJob.permissionsRejected.toArray(new String[FragmentFindJob.this.permissionsRejected.size()]), 101);
                }, context);
                return;
            }
            getLastLocationPermissionCheck();
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();
        Iterator<String> it = wanted.iterator();
        while (it.hasNext()) {
            String perm = it.next();
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (!canAskPermission() || Build.VERSION.SDK_INT < 23 || getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        return true;
    }

    private boolean canAskPermission() {
        return Build.VERSION.SDK_INT > 22;
    }

    private void getLocation() {
        try {
            if (!canGetLocation) {
                return;
            }
            if (isGPS) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10.0f, locationChangeListener);
                if (locationManager != null) {
                    this.loc = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            } else if (this.isNetworkProvider) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10.0f, locationChangeListener);
                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } else {
                loc.setLatitude(0.0d);
                loc.setLongitude(0.0d);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            this.loc = this.locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void setListenersToView() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentFindJob.this);
        });

        this.btnSelectRole.setOnClickListener(v -> EventBus.getDefault().post(new EventRoleClear()));
    }


    private void addMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                this.googleMap = googleMap;

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    getActivity().finish();
                    startActivity(new Intent(getContext(), LocationPermissionActivity.class));
                }

                if (locationUtil.getLocationClass() != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(locationUtil.getLocationClass().getLatitude(), locationUtil.getLocationClass().getLongitude()), 15);
                    googleMap.moveCamera(cameraUpdate);
                }
            });
        }
    }

    private void initData() {
        String role = PreferencesUtil.readString(context, PrefConstants.ROLE, "");
        if (!(googleMap == null || getActivity() == null)) {
            getActivity().runOnUiThread(() -> googleMap.clear());
        }
        if (locationUtil.getLocationClass() != null) {
            if (role.equals(AppConstants.ROLE_JOB_SEEKER)) {
                getAvailableJobsData();

            } else if (role.equals(AppConstants.ROLE_JOB_OFFER)) {
                getPostedJobsData();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        locationUtil = new LocationUtil(getActivity(), getContext());
        locationUtil.init();
        registerRecievers();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        findJobLayout.setOnClickListener(v -> {
            controllerUpdateUserRole.postData(getContext(), AppConstants.ROLE_JOB_SEEKER, PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        offerJobLayout.setOnClickListener(v -> controllerUpdateUserRole.postData(getContext(), AppConstants.ROLE_JOB_OFFER, PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0)));
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        initData();
    }

    private void registerRecievers() {
        this.isEventBusRegistred = true;
    }

    private void getAvailableJobsData() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                double radiusD = PreferencesUtil.readDouble(context, TAG_RADIUS, 1) * 100.0;
                int radius = (int) radiusD * 100;
                int userId = PreferencesUtil.readInt(context, KEY_USER_ID, 0);
                if (multiplejobsPanelLayout != null) {
                    multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                }

                if (networkDialog != null) {
                    networkDialog.cancel();
                    if (locationUtil.lococation == null) {
                        loadingView.setVisibility(View.VISIBLE);
                        return;
                    }
                    loadingView.setVisibility(View.VISIBLE);
                    controllerFindJob.getData(context, locationUtil.lococation.getLatitude(), locationUtil.lococation.getLongitude(), radius, userId);
                    return;
                }
                controllerFindJob.getData(context, locationUtil.lococation.getLatitude(), locationUtil.lococation.getLongitude(), radius, userId);
                getLastLocationPermissionCheck();
            });
        }
    }

    private void collapseSlidingBar() {
        TextView textView = this.jobTitle;
        if (textView != null) {
            textView.setText(getActivity().getResources().getString(R.string.no_job_selected));
        }
        RecyclerView recyclerView = this.multipleJobs;
        if (recyclerView != null) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        SlidingUpPanelLayout slidingUpPanelLayout = this.multiplejobsPanelLayout;
        if (slidingUpPanelLayout != null) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    private void getPostedJobsData() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                collapseSlidingBar();
                if (networkDialog != null) {
                    networkDialog.cancel();
                    LocationUtil locationUtil = new LocationUtil(activity, context);
                    locationUtil.init();
                    if (locationUtil.lococation != null) {
                        loadingView.setVisibility(View.GONE);
                        controllerFindMyJobs.getData(getContext(), PreferencesUtil.readInt(context, KEY_USER_ID, 0));
                        return;
                    }
                    controllerFindMyJobs.getData(getContext(), PreferencesUtil.readInt(context, KEY_USER_ID, 0));
                    loadingView.setVisibility(View.VISIBLE);
                    getLastLocationPermissionCheck();
                }
            });
        }
    }

    @Subscribe
    public void eventFindJobNetworkError(EventFindJobNetErr eventNetConnection) {
        serverErrorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Subscribe
    public void eventEditJob(EventEditJob eventEditJob) {
        initData();
        eventEditJob.getAlertDialog().cancel();
    }

    @Subscribe
    public void eventRoleSelected(EventRoleClear eventRoleClear) {
        roleNotSelected.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void eventEditJob(EventAddJobNetSuccess eventEditJob) {
        initData();
    }

    @Subscribe
    public void eventApplied(EventApplied eventApplied) {
        initData();
        if (eventApplied.getResponseModel().getMessage() != null) {
            Toast.makeText(context, eventApplied.getResponseModel().getMessage(), Toast.LENGTH_LONG).show();
        }
        new ControllerGetJobOfferToken(context, eventApplied.getOffererId());

    }

    @Subscribe
    public void eventOffererToken(EventJobOffererToken eventJobOffererToken) {
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        String lol = eventJobOffererToken.getResponse().body().getMessage();
        notificationRequestDto.setTarget(lol);
        notificationRequestDto.setBody(getResources().getString(R.string.you_have_new_candidate));
        notificationRequestDto.setTitle(getResources().getString(R.string.app_name));
        new ControllerSendOffererPushNotification(context, notificationRequestDto);
    }

    @Subscribe
    public void goToSuccess(EventSendOffererPushNotification eventSendPushNotificationSuccess) {
        startActivity(new Intent(getContext(), ActivitySuccessfullyApplied.class));
    }

    @Subscribe
    public void networkConnected(EventApplyFailure eventApplyFailure) {
        Toast.makeText(context, getResources().getString(R.string.there_was_problem_with_the_server_please_try_again), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void gpsConnected(EventGPSuccess eventGPSuccess) {
        AlertDialog alertDialog = this.gpsDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        this.isGPS = true;
        restartFragment();
    }

    private void restartFragment() {
        if (getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }

    @Subscribe
    public void gpsFailure(EventGPSFailure eventGPSFailure) {
        AlertDialog alertDialog = this.gpsDialog;
        if (alertDialog != null) {
            alertDialog.setTitle(getActivity().getResources().getString(R.string.gps_is_turned_off));
            this.gpsDialog.setMessage(getActivity().getResources().getString(R.string.please_turn_on_your_gps));
            this.gpsDialog.show();
            this.gpsDialog.setCancelable(false);
        }
        this.isGPS = false;
    }

    @Subscribe
    public void findJobNetSuccess(EventFindJobNetSuccess eventFindJobNetSuccess) {

        this.jobsList = eventFindJobNetSuccess.getJobDTO().body();
        this.loadingView.setVisibility(View.GONE);
        this.serverErrorView.setVisibility(View.GONE);
        if (eventFindJobNetSuccess.getJobDTO().body().size() < 1) {
            this.noJobsFoundLayout.setVisibility(View.VISIBLE);
            this.noJobsFoundInfo.setVisibility(View.VISIBLE);
            this.noJobsFoundInfo.setText(getActivity().getResources().getString(R.string.unfortunately_in_your_radius) + " " + ((int) (PreferencesUtil.readDouble(getContext(), TAG_RADIUS, 1) * 100)) + " " + getActivity().getResources().getString(R.string.there_are_no_jobs_available));
        } else {
            this.noJobsFoundLayout.setVisibility(View.GONE);
            this.noJobsFoundInfo.setVisibility(View.GONE);
        }
        if (googleMap != null) {
            googleMap.clear();

            addMarkers();
        }
    }

    @Subscribe
    public void eventJobDeleteSuccess(EventDeleteJob eventDeleteJob) {
        multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        eventDeleteJob.getAlertDialog().dismiss();
        initData();
    }

    @Subscribe
    public void eventDeleteJobSuccess(EventDeleteJob eventDeleteJob) {
        multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        if (eventDeleteJob.getAlertDialog() == null) {
            eventDeleteJob.getAlertDialog().dismiss();
        } else {

        }
        initData();
    }

    @Subscribe
    public void eventJobDeleteFail(EventDeleteJobNetErr eventDeleteJob) {
        Toast.makeText(this.context, eventDeleteJob.getT().toString(), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void applyedJob(EventApplyedJob eventApplyedJob) {
        this.multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        initData();
    }

    @Subscribe
    public void applyedJob(EventApplyedJobNetErr eventApplyedJob) {
        Toast.makeText(this.context, getActivity().getResources().getString(R.string.submit_unsuccessful), Toast.LENGTH_LONG).show();
    }

    private void addMarkers() {
        ArrayList<Marker> mMarkerArray = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (this.jobsList != null) {
            for (int i = 0; i < this.jobsList.size(); i++) {
                LatLng jobPosition = new LatLng(this.jobsList.get(i).getLatitude(), this.jobsList.get(i).getLongitude());
                MarkerOptions title = new MarkerOptions().position(jobPosition).title(this.jobsList.get(i).getTitle());
                mMarkerArray.add(this.googleMap.addMarker(title.snippet("Salary: " + this.jobsList.get(i).getSalary())));
            }

            this.googleMap.clear();

            List<Job> jobWithCategory1 = this.jobsList;
            if (this.jobsList.size() < 1) {
                this.noJobsFoundLayout.setVisibility(View.VISIBLE);
                this.noJobsFoundInfo.setText(getActivity().getResources().getString(R.string.unfortunately_in_your_radius) + " " + ((int) (PreferencesUtil.readDouble(getContext(), TAG_RADIUS, 1) * 100)) + " " + getActivity().getResources().getString(R.string.there_are_no_jobs_available));
            } else {
                this.noJobsFoundLayout.setVisibility(View.GONE);
            }
            MarkerManager markerManager = new MarkerManager(this.googleMap);

            ClusterManager<Job> clusterManager = new ClusterManager<>(getContext(), this.googleMap);
            googleMap.setOnCameraIdleListener(clusterManager);

            clusterManager.setItems(jobsList);
            googleMap.setOnInfoWindowClickListener(markerManager);
            clusterManager.setCallbacks(new ClusterManager.Callbacks<Job>() {
                public boolean onClusterClick(Cluster<Job> cluster) {
                    multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    jobTitle.setText(getActivity().getResources().getString(R.string.jobs_was_found, new Object[]{Integer.valueOf(cluster.getItems().size())}));
                    multipleJobs.setAdapter(new AdapterFoundJobs(cluster.getItems(), getActivity(), getContext()));
                    multipleJobs.setVisibility(View.VISIBLE);
                    //googlemapUtil.centerCamera(googleMap, new LatLng(cluster.getLatitude(), cluster.getLongitude()));
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getLatitude(), cluster.getLongitude()), 17.0f));
                    return true;
                }

                public boolean onClusterItemClick(Job job) {
                    multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    AlertViewJobInMap.init(activity, context, job);
                    FragmentFindJob.this.multipleJobs.setVisibility(View.GONE);
                    return true;
                }
            });
            googleMap.setOnMapClickListener(latLng -> {
                multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            });


            Iterator<Marker> it = mMarkerArray.iterator();
            while (it.hasNext()) {
                builder.include(it.next().getPosition());
            }
            builder.include(new LatLng(locationUtil.lococation.getLatitude(), locationUtil.lococation.getLongitude()));

            LatLngBounds bounds = builder.build();
            int padding = 150; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cu);
        } else {
            this.noJobsFoundInfo.setText(getResources().getString(R.string.unfortunately_in_your_radius) + " " + ((int) (PreferencesUtil.readDouble(getContext(), TAG_RADIUS, 1) * 100)) + " " + getActivity().getResources().getString(R.string.there_are_no_jobs_available));
        }
    }

    private void closeMenuOnMapMove() {
    }

    public void onMapReady(GoogleMap googleMap) {
        requestPermissions();

        assert getContext() != null;

        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == 0) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        if (loc == null) {
            getLastLocationPermissionCheck();
            return;
        }
        googleMap.setOnMapClickListener(latLng -> multiplejobsPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN));
        closeMenuOnMapMove();
    }

    private void getLastLocationPermissionCheck() {

        assert getContext() != null;
        assert getActivity() != null;

        if (ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != 0) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 30);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onResume() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }
}
