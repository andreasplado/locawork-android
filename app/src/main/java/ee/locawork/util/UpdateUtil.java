package ee.locawork.util;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import ee.locawork.R;

public class UpdateUtil {

    public static final int APP_UPDATE_REQUEST_CODE = 215;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;

    public void init(Activity activity, Context context){
        appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            activity,
                            // Include a request code to later monitor this update request.
                            APP_UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    //e.printStackTrace();
                    Toast.makeText(context, context.getResources().getString(R.string.update_failed), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
