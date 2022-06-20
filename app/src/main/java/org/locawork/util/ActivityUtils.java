package org.locawork.util;

import android.app.Activity;
import android.content.Intent;

public class ActivityUtils {
    public static void restartActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, activity.getClass());
        activity.startActivity(intent);
        activity.finish();
    }
}
