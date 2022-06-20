package org.locawork;

import android.content.Context;

import org.locawork.model.MainData;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerGetMainData implements Callback<MainData> {
    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceMainData.class).getMainData(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void onResponse(Call<MainData> call, Response<MainData> response) {
        EventBus.getDefault().post(new EventGetMainData(response));
    }

    public void onFailure(Call<MainData> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToGetMainData(t));
    }
}
