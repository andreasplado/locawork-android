package org.locawork.ui.pushnotification;


import android.content.Context;

import com.google.gson.GsonBuilder;

import org.locawork.model.pushnotification.NotificationRequestDto;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerSendPushNotificationToTopic implements Callback<String> {

    public ControllerSendPushNotificationToTopic(Context context, NotificationRequestDto notificationRequestDto){
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServicePushNotification.class).sendPnsToTopic(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), notificationRequestDto).enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

    }
}
