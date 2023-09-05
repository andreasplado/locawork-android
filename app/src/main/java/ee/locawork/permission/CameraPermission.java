package ee.locawork.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraPermission {

    public final static int PERMISSION_TAG_CAMERA = 421;

    private static final String TAG = "DENIED";
    private Activity activity;
    private Context context;

    public CameraPermission(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void init(Context context){
        makeRequest(activity);
        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
        }
    }
    private void makeRequest(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_TAG_CAMERA);
    }
}
