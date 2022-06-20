package org.locawork;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.billy.android.preloader.PreLoader;
import com.billy.android.preloader.interfaces.DataListener;
import org.locawork.util.BillyPreloader;

public class ActivityPreloader extends AppCompatActivity {
    int preLoaderId = PreLoader.preLoad(new BillyPreloader());

    class Listener implements DataListener<String> {
        @Override
        public void onDataArrived(String data) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreLoader.listenData(preLoaderId, new Listener());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}