package org.locawork;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;

import org.locawork.broadcastreciever.GpsReciver;
import org.locawork.broadcastreciever.NetworkReciever;

public class MainApplication extends Application {
    static Activity mActivity;
    public IntentFilter gpsIntentFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
    public GpsReciver gpsReciver = new GpsReciver();
    public IntentFilter networkIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    public NetworkReciever networkReciever = new NetworkReciever();

    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                MainApplication.mActivity = activity;
                MainApplication mainApplication = MainApplication.this;
                mainApplication.registerReceiver(mainApplication.networkReciever, MainApplication.this.networkIntentFilter);
                MainApplication mainApplication2 = MainApplication.this;
                mainApplication2.registerReceiver(mainApplication2.gpsReciver, MainApplication.this.gpsIntentFilter);
            }

            public void onActivityStarted(Activity activity) {
                MainApplication.mActivity = activity;
            }

            public void onActivityResumed(Activity activity) {

            }

            public void onActivityPaused(Activity activity) {
                MainApplication.mActivity = null;
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
                MainApplication mainApplication = MainApplication.this;
                mainApplication.unregisterReceiver(mainApplication.networkReciever);
                MainApplication mainApplication2 = MainApplication.this;
                mainApplication2.unregisterReceiver(mainApplication2.gpsReciver);
            }
        });
    }
}
