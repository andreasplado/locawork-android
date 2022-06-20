package org.locawork.ui.pushnotification;


import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.model.Note;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerGetJobOfferToken implements Callback<Note> {

    private Context context;

    public ControllerGetJobOfferToken(Context context, Integer jobOfferId){
        this.context = context;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServicePushNotification.class).getOffererFirebaseToken(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), jobOfferId).enqueue(this);
    }

    @Override
    public void onResponse(Call<Note> call, Response<Note> response) {
        Log.i("TAG", response.body().getMessage());
        EventBus.getDefault().post(new EventJobOffererToken(response));
    }

    @Override
    public void onFailure(Call<Note> call, Throwable t) {
        EventBus.getDefault().post(new EventOffererTokenFailure());
    }
}
