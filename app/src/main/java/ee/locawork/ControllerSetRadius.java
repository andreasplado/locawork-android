package ee.locawork;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.model.ResponseModel;
import ee.locawork.ui.settings.EventUpdateRadiusFailure;
import ee.locawork.ui.settings.EventUpdateRadiusNotValid;
import ee.locawork.ui.settings.EventUpdateRadiusSuccess;
import ee.locawork.ui.settings.ServiceSettings;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerSetRadius implements Callback<ResponseModel> {

    public void set(Context context, Integer userId, Double radius) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSettings.class).updateRadius(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId, radius).enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new EventSetRadiusSuccess(response));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new EventSetRadiusFailure(t));
    }
}
