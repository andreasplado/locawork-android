package ee.locawork.ui.pushnotification;


import android.content.Context;

import com.google.gson.GsonBuilder;

import ee.locawork.model.pushnotification.SubscriptionRequestDto;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUnsubscribe implements Callback<Void> {

    public ControllerUnsubscribe(Context context, SubscriptionRequestDto subscriptionRequestDto){
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServicePushNotification.class).unsubscribeFromTopic(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), subscriptionRequestDto).enqueue(this);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {

    }
}
