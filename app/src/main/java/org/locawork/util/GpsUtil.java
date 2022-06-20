package org.locawork.util;

import android.content.Context;
import android.location.LocationManager;

public class GpsUtil {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public static boolean isGpsAvailable(Context context) {
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps");
    }
}
