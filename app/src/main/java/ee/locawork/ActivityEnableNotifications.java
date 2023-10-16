package ee.locawork;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import ee.locawork.broadcastreciever.NotificationReciever;
import ee.locawork.util.NotificationUtils;

public class ActivityEnableNotifications extends AppCompatActivity {

    private Button enableNotifications;

    private BroadcastReceiver notificationReceiver = new NotificationReciever();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_notifications);
        enableNotifications = findViewById(R.id.enable_notifications);
        enableNotifications.setOnClickListener(v -> {
            finish();
            Intent settingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(settingsIntent);
        });
    }

    private void registerRecievers() {
        new IntentFilter("android.location.PROVIDERS_CHANGED").addAction("android.intent.action.PROVIDER_CHANGED");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(notificationReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(notificationReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NotificationUtils.isNotificationEnabled(getApplicationContext())){
            finish();;
        }
    }

    @Override
    public void onStart() {
        registerRecievers();
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}