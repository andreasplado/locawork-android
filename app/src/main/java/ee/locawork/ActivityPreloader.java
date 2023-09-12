package ee.locawork;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.billy.android.preloader.PreLoader;
import com.billy.android.preloader.interfaces.DataListener;

import ee.locawork.ui.login.LoginActivity;
import ee.locawork.util.BillyPreloader;
import ee.locawork.util.PreferencesUtil;

public class ActivityPreloader extends AppCompatActivity {
    int preLoaderId = PreLoader.preLoad(new BillyPreloader());

    class Listener implements DataListener<String> {
        @Override
        public void onDataArrived(String data) {
            if(PreferencesUtil.readInt(getApplicationContext(), PreferencesUtil.KEY_USER_ID, 0) != 0){
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
            }else{
                PreferencesUtil.flushDataOnLogout(getApplicationContext());
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

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