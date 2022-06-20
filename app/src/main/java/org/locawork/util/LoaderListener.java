package org.locawork.util;

import android.app.Activity;
import android.widget.Toast;

import com.billy.android.preloader.interfaces.DataListener;

public class LoaderListener implements DataListener<String> {
    private Activity activity;

    LoaderListener(Activity activity){
        this.activity = activity;
    }
    @Override
    public void onDataArrived(String data) {
        // this method runs on main thread, Handler is not required
        Toast.makeText(activity, data, Toast.LENGTH_SHORT).show();
    }
}