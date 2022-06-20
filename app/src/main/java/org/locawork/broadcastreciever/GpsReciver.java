package org.locawork.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import org.locawork.ui.findjob.EventGPSFailure;
import org.locawork.ui.findjob.EventGPSuccess;
import org.greenrobot.eventbus.EventBus;

public class GpsReciver extends BroadcastReceiver {


    public void onReceive(Context context, Intent intent) {
        int i=0;
        if ("android.location.PROVIDERS_CHANGED".equals(intent.getAction())) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled("gps");
            boolean isProviderEnabled = locationManager.isProviderEnabled("network");
            if (isGpsEnabled) {
                EventBus.getDefault().post(new EventGPSuccess());
            } else {
                EventBus.getDefault().post(new EventGPSFailure());
            }
        }
    }

    private boolean isGpsEnabled(Context context) {
        if (Settings.Secure.getInt(context.getContentResolver(), "location_mode", 0) != 0) {
            EventBus.getDefault().post(new EventGPSuccess());
            return true;
        }
        EventBus.getDefault().post(new EventGPSFailure());
        return false;
    }
}
