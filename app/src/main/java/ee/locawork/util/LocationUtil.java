package ee.locawork.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import ee.locawork.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.core.content.ContextCompat;

public class LocationUtil {
    private static final int ALL_PERMISSIONS_RESULT = 101;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 30;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private Activity activity;
    private boolean canGetLocation = true;
    private Context context;
    private boolean isGPS;
    private boolean isNetworkProvider;
    public Location location;

    private LocationListener locationChangeListener = new LocationListener() {
        public void onLocationChanged(Location l) {
            if (l != null) {
                LocationUtil.this.location = l;
            }
        }

        public void onProviderEnabled(String p) {
        }

        public void onProviderDisabled(String p) {
        }

        public void onStatusChanged(String p, int status, Bundle extras) {
        }
    };
    private LocationManager locationManager;
    private ArrayList<String> permissionsToRequest;

    public LocationUtil(Activity activity, Context context) {
        this.context = context;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        this.isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        this.isNetworkProvider = this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static String fetchLocationData(Activity activity2, LatLng latLng) {
        List<Address> addresses = null;
        String location = activity2.getResources().getString(R.string.undefined_location);
        try {
            addresses = new Geocoder(activity2, Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() != 0) {
                location = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
        }
        if (addresses != null) {
            addresses.isEmpty();
        }
        return location;
    }

    public void init() {
        if (this.isGPS || this.isNetworkProvider) {
            this.canGetLocation = true;
        } else {
            this.canGetLocation = false;
        }
        getLocation();
    }

    private void getLocation() {
        try {
            if (!this.canGetLocation) {
                return;
            }
            if (this.isGPS) {
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10.0f, this.locationChangeListener);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            } else if (this.isNetworkProvider) {
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10.0f, this.locationChangeListener);
                if (this.locationManager != null) {
                    this.location = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } else {
                this.location.setLatitude(0.0d);
                this.location.setLongitude(0.0d);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void getLastLocation() {
        try {
            this.location = this.locationManager.getLastKnownLocation(Objects.requireNonNull(this.locationManager.getBestProvider(new Criteria(), false)));
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this.context, permission) == 0;
    }

    public static boolean isInRadius(Location currentLocation, Location workLocation, float radius) {
        return currentLocation.distanceTo(workLocation) <= radius;
    }

    public LatLng getLatLng(){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public Location getLocationClass(){
        return location;
    }
}
