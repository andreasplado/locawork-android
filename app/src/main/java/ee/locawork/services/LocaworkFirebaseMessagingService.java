package ee.locawork.services;


import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;
import static ee.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;
import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;
import static ee.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;

public class LocaworkFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        android.os.Debug.waitForDebugger();
        getSharedPreferences(KEY_LOCAWORK_PREFS, MODE_PRIVATE).edit().putString(KEY_PUSH_NOTIFICATION_TOKEN, s).commit();
        super.onNewToken(s);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(KEY_LOCAWORK_PREFS, MODE_PRIVATE).getString(KEY_PUSH_NOTIFICATION_TOKEN, "");
    }

}
