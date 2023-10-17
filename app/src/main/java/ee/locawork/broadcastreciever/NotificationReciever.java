package ee.locawork.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import org.greenrobot.eventbus.EventBus;

import ee.locawork.event.EventNetOff;
import ee.locawork.event.EventNetOn;
import ee.locawork.event.EventNotificationOff;
import ee.locawork.event.EventNotificationOn;
import ee.locawork.util.NotificationUtils;

public class NotificationReciever extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        boolean isNotificationOn = NotificationUtils.isNotificationEnabled(context);
        if (isNotificationOn) {
            EventBus.getDefault().post(new EventNotificationOn());
        }else{
            EventBus.getDefault().post(new EventNotificationOff());
        }
    }

    private void checkConnectivity(Context context) {
        registerNetworkCallback(context);
    }

    public void registerNetworkCallback(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                public void onAvailable(Network network) {
                    EventBus.getDefault().post(new EventNetOn());
                }

                public void onLost(Network network) {
                    EventBus.getDefault().post(new EventNetOff());
                }
            });
            connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
            });
        } catch (Exception e) {
        }
    }
}
