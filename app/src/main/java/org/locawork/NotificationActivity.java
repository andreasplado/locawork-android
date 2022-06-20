package org.locawork;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.locawork.R;

import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;

public class NotificationActivity extends AppCompatActivity {
    private ImageButton accept;
    private boolean doubleBackToExitPressedOnce = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_notification);
        ImageButton imageButton = findViewById(R.id.accept);
        this.accept = imageButton;
        imageButton.setOnClickListener(view -> {
            PreferencesUtil.save(getApplicationContext(), PrefConstants.TAG_IS_NOTIFICATION, "1");
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
