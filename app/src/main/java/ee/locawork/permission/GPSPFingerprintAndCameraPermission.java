package ee.locawork.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPSPFingerprintAndCameraPermission {

    public final static int PERMISSION_TAG_GPS_AND_CAMERA = 321;
    private static final String TAG = "DENIED";
    private Activity activity;
    private Context context;

    public GPSPFingerprintAndCameraPermission(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void init(Context context){
        makeRequest(activity);
        int locationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        int fingerprintPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.USE_BIOMETRIC);

        if (locationPermission != PackageManager.PERMISSION_GRANTED && cameraPermission != PackageManager.PERMISSION_GRANTED && fingerprintPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void makeRequest(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.USE_BIOMETRIC},
                PERMISSION_TAG_GPS_AND_CAMERA);
    }
}
