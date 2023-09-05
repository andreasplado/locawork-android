package ee.locawork.util;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import ee.locawork.R;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;

import static android.content.Context.KEYGUARD_SERVICE;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class BiometricUtil {

    private Context context;


    public BiometricUtil(Context context){
        this.context = context;
    }

    public BiometricPrompt.PromptInfo getPromptInfo(String title, String subtitle){
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                // Can't call setNegativeButtonText() and
                // setAllowedAuthenticators(...|DEVICE_CREDENTIAL) at the same time.
                // .setNegativeButtonText("Use account password")
                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
                .build();
    }

    public boolean isBiometricAvailable(Activity activity, Context context){
        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);

        PackageManager packageManager = activity.getPackageManager();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Toast.makeText(context, activity.getResources().getString(R.string.this_android_version_does_not_support_fingerprint_authentication), Toast.LENGTH_LONG).show();
            return false;
        }

        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
        {
            Toast.makeText(context, activity.getResources().getString(R.string.fingerprint_sensor_not_supported), Toast.LENGTH_LONG).show();
            return false;
        }

        if (!keyguardManager.isKeyguardSecure()) {
            Toast.makeText(context, activity.getResources().getString(R.string.lock_screen_security_not_enabled_in_settings), Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }
}
