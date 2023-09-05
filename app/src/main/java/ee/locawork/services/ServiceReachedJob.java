package ee.locawork.services;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import ee.locawork.R;
import ee.locawork.util.LocationUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ee.locawork.ActivityMain;
import ee.locawork.util.PreferencesUtil;

public class ServiceReachedJob extends IntentService {

    public static final String KEY_APPLY_JOB = "apply_job";
    public static final String KEY_JOB_DESCRIPTION = "job_description";
    public static final String KEY_JOB_ID = "job_id";
    public static final String KEY_JOB_LATITUDE = "job_latitude";
    public static final String KEY_JOB_LONGITUDE = "job_longitude";
    public static final String KEY_JOB_SALARY = "job_salary";
    public static final String KEY_JOB_TITLE = "job_title";
    private static final float LOCATION_DISTANCE = 10.0f;
    private static final int LOCATION_INTERVAL = 1000;
    private static final String TAG = "MyLocationService";
    public static final String KEY_HAVE_REACHED = "have_reached";
    boolean isNotified;
    private String jobDescription;
    public Location jobLocation = new Location(LocationManager.GPS_PROVIDER);
    private String jobSalary;
    private String jobTitle;
    private double latitude;
    private double longitude;
    private LocationManager mLocationManager;

    public ServiceReachedJob() {
        super("ServiceReachedJob");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Bundle extras = intent.getExtras();
        this.jobLocation.setLongitude(extras.getDouble(KEY_JOB_LONGITUDE, 0));
        this.jobLocation.setLatitude(extras.getDouble(KEY_JOB_LATITUDE, 0));
        initializeLocationManager();
        // this line is key
        try {
            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, LOCATION_DISTANCE, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (LocationUtil.isInRadius(location, ServiceReachedJob.this.jobLocation, 10000.0f)) {
                        if (!ServiceReachedJob.this.isNotified) {
                            ServiceReachedJob.this.showNotification();
                            PreferencesUtil.save(ServiceReachedJob.this, KEY_HAVE_REACHED, 1);
                        }
                        ServiceReachedJob.this.isNotified = true;
                    }
                    Log.e(TAG, "Location changed: " + location.toString());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex2) {
            Log.d(TAG, "network provider does not exist, " + ex2.getMessage());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: 1000 LOCATION_DISTANCE: 10.0");
        if (this.mLocationManager == null) {
            this.mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void showNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplication());
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(new NotificationChannel("MESSAGE", "MESSAGE", NotificationManager.IMPORTANCE_HIGH));
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify(getRandomNumber(),
                new NotificationCompat.Builder(getApplicationContext(), "MESSAGE")
                        .setSmallIcon(R.drawable.ic_aim).setContentTitle(
                                getApplicationContext().getResources().getString(R.string.message))
                        .setContentText(getApplicationContext().getResources()
                                .getString(R.string.you_reachecd_your_working_destination))
                        .setContentIntent(PendingIntent
                                .getActivity(this, 0,
                                        new Intent(this, ActivityMain.class),
                                        PendingIntent.FLAG_MUTABLE)).build());
    }

    private static int getRandomNumber() {
        return Integer.parseInt(new SimpleDateFormat("mmssSS").format(new Date()));
    }
}