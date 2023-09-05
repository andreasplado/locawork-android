package ee.locawork.ui.settings;

import android.content.Context;

import ee.locawork.model.ResponseModel;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUpdateRadius implements Callback<ResponseModel> {

    public void update(Context context, Integer userId, Double radius) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSettings.class).updateRadius(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId, radius).enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        if (response.body().isValid()) {
            EventBus.getDefault().post(new EventUpdateRadiusNotValid());
        } else {
            EventBus.getDefault().post(new EventUpdateRadiusSuccess(response));
        }
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new EventUpdateRadiusFailure(t));
    }
}
