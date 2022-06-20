package org.locawork.ui.payformemeber;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCreatePayment implements Callback<String> {

    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServicePayForMember.class).getMemberRole(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void onResponse(Call<String> call, Response<String> response) {
        EventBus.getDefault().post(new IsMemberSuccess(response.body()));
    }

    public void onFailure(Call<String> call, Throwable t) {
        EventBus.getDefault().post(new IsMemberFailure(t));
    }
}