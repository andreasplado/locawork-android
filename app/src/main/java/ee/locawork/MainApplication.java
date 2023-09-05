package ee.locawork;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.os.Bundle;

import com.stripe.android.PaymentConfiguration;

import ee.locawork.broadcastreciever.GpsReciver;
import ee.locawork.broadcastreciever.NetworkReciever;

public class MainApplication extends Application {
    static Activity mActivity;
    public IntentFilter gpsIntentFilter = new IntentFilter("android.location.PROVIDERS_CHANGED");
    public GpsReciver gpsReciver = new GpsReciver();
    public IntentFilter networkIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    public NetworkReciever networkReciever = new NetworkReciever();

    public void onCreate() {
        super.onCreate();

        registerStripe();
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                MainApplication.mActivity = activity;
                MainApplication mainApplication = MainApplication.this;
                mainApplication.registerReceiver(mainApplication.networkReciever, MainApplication.this.networkIntentFilter);
                mainApplication.registerReceiver(mainApplication.gpsReciver, MainApplication.this.gpsIntentFilter);

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
                mainApplication.unregisterReceiver(mainApplication.gpsReciver);
            }
        });
    }

    private void registerStripe() {
        PaymentConfiguration.init(
                getApplicationContext(),
                getResources().getString(R.string.stripe_publishable_key)
        );
    }
}
