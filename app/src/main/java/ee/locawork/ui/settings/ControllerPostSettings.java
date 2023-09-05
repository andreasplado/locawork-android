package ee.locawork.ui.settings;

import android.content.Context;

import ee.locawork.model.Settings;
import ee.locawork.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerPostSettings implements Callback<Settings> {
    public void postData(Context context, Integer userId) {
        Gson gson = new GsonBuilder().setLenient().create();
        Settings settings = new Settings();
        settings.setRadius(1.0d);
        settings.setUserId(userId);
        settings.setAskPermissionsBeforeDeletingAJob(true);
        settings.setShowInformationOnStartup(true);
        settings.setViewByDefault("available");
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build().create(ServiceSettings.class).postUserSettings(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), settings).enqueue(this);
    }

    public void onResponse(Call<Settings> call, Response<Settings> response) {
        EventBus.getDefault().post(new EventSettingsSuccess(response));
    }

    public void onFailure(Call<Settings> call, Throwable t) {
        EventBus.getDefault().post(new EventSettingsFailure(t));
    }
}
