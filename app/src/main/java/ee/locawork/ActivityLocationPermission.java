package ee.locawork;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ee.locawork.permission.GPSPFingerprintAndCameraPermission;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static ee.locawork.permission.GPSPFingerprintAndCameraPermission.PERMISSION_TAG_GPS_AND_CAMERA;

public class ActivityLocationPermission extends AppCompatActivity {

    private Button allowLocationAndCamera, allowCameraPermission;
    private TextView accessDenied;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_permission);

        allowLocationAndCamera = findViewById(R.id.allow_location_and_camera);
        accessDenied = findViewById(R.id.access_gps_denied_text);

        allowLocationAndCamera.setOnClickListener(v -> {
            GPSPFingerprintAndCameraPermission GPSPFingerprintAndCameraPermission = new GPSPFingerprintAndCameraPermission(this, this);
            GPSPFingerprintAndCameraPermission.init(this);

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_TAG_GPS_AND_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
                startActivity(new Intent(ActivityLocationPermission.this, ActivityMain.class));
            } else {
                accessDenied.setVisibility(View.VISIBLE);
            }
        }
    }
}