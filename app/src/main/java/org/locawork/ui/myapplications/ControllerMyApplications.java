package org.locawork.ui.myapplications;

import android.content.Context;

import org.locawork.model.MyApplications;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerMyApplications implements Callback<MyApplications> {
    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceMyApplications.class).getMyApplications(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void onResponse(Call<MyApplications> call, Response<MyApplications> response) {
        EventBus.getDefault().post(new EventMyApplicationsNetSuccess(response.body()));
    }

    public void onFailure(Call<MyApplications> call, Throwable t) {
        EventBus.getDefault().post(new EventMyApplicationsNetFailure(t));
    }
}
