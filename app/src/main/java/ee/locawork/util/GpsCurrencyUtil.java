package ee.locawork.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;

public class GpsCurrencyUtil extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 60000;
    boolean canGetLocation = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    private final Context mContext;

    public GpsCurrencyUtil(Context mContext2) {
        this.mContext = mContext2;
        getLocation();
    }

    public Location getLocation() {
        try {
            LocationManager locationManager2 = (LocationManager) this.mContext.getSystemService(LOCATION_SERVICE);
            this.locationManager = locationManager2;
            this.isGPSEnabled = locationManager2.isProviderEnabled("gps");
            boolean isProviderEnabled = this.locationManager.isProviderEnabled("network");
            this.isNetworkEnabled = isProviderEnabled;
            if (!this.isGPSEnabled && !isProviderEnabled) {
                return this.location;
            }
            this.canGetLocation = true;
            if (this.isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
                    ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
                }
                this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    Location lastKnownLocation = this.locationManager.getLastKnownLocation("network");
                    this.location = lastKnownLocation;
                    if (lastKnownLocation != null) {
                        this.latitude = lastKnownLocation.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }
            if (this.isGPSEnabled && this.location == null) {
                if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
                    ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION");
                }
                this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                if (this.locationManager != null) {
                    Location lastKnownLocation2 = this.locationManager.getLastKnownLocation("gps");
                    this.location = lastKnownLocation2;
                    if (lastKnownLocation2 != null) {
                        this.latitude = lastKnownLocation2.getLatitude();
                        this.longitude = this.location.getLongitude();
                    }
                }
            }
            return this.location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double getLatitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.latitude = location2.getLatitude();
        }
        return this.latitude;
    }

    public double getLongitude() {
        Location location2 = this.location;
        if (location2 != null) {
            this.longitude = location2.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", (dialogInterface, i) -> GpsCurrencyUtil.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS")));
        alertDialog.setNegativeButton("Cancel", (dialog, which) -> {

        });
        alertDialog.show();
    }

    public void onLocationChanged(Location location2) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
