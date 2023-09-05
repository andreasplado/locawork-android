package ee.locawork.ui.settings;

import android.content.Context;

import ee.locawork.model.Settings;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUpdateShowInformationOnStartup implements Callback<Settings> {

    public void update(Context context, Integer userId, Boolean value) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSettings.class).updateShowInformationOnStartup(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId, value).enqueue(this);
    }

    public void onResponse(Call<Settings> call, Response<Settings> response) {
        EventBus.getDefault().post(new EventSettingsSuccess(response));
    }

    public void onFailure(Call<Settings> call, Throwable t) {
        EventBus.getDefault().post(new EventSettingsFailure(t));
    }
}
