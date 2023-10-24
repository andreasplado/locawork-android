package ee.locawork.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import ee.locawork.event.EventNetOff;
import ee.locawork.event.EventNetOn;
import org.greenrobot.eventbus.EventBus;

public class NetworkRecieverOnMain extends BroadcastReceiver {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            checkConnectivity(context);
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
