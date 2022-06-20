package org.locawork.services;


import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

import static org.locawork.util.PrefConstants.ROLE;
import static org.locawork.util.PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN;

public class LocaworkFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        android.os.Debug.waitForDebugger();
        getSharedPreferences(ROLE, MODE_PRIVATE).edit().putString(KEY_PUSH_NOTIFICATION_TOKEN, s).commit();
        super.onNewToken(s);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(ROLE, MODE_PRIVATE).getString(KEY_PUSH_NOTIFICATION_TOKEN, "");
    }

}
