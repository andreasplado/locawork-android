package ee.locawork;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.model.Settings;
import ee.locawork.ui.settings.EventSettingsFailure;
import ee.locawork.ui.settings.EventSettingsNotSet;
import ee.locawork.ui.settings.EventSettingsSuccess;
import ee.locawork.ui.settings.ServiceSettings;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerGetSettings implements Callback<Settings> {

    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSettings.class)
                .getUserSettings(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    @Override
    public void onResponse(Call<Settings> call, Response<Settings> response) {
        Response<Settings> response1= response;
        int i = 0;
        if (response1.body() == null) {
            EventBus.getDefault().post(new EventSettingsNotSet());
        } else {
            EventBus.getDefault().post(new EventSettingsSuccess(response));
        }
    }

    @Override
    public void onFailure(Call<Settings> call, Throwable t) {
        EventBus.getDefault().post(new EventSettingsFailure(t));
    }
}
