package org.locawork;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.model.User;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUpdateFirebaseToken implements Callback<User> {

    public ControllerUpdateFirebaseToken(Context context, int userId, String firebaseToken){
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory
                .create(new GsonBuilder().setLenient().create())).build().create(ServiceUpdateUserFirebaseToken.class)
                .updateUserFirebaseToken(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), firebaseToken, userId).enqueue(this);
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        EventBus.getDefault().post(new EventUpdateFirebaseTokenSuccess());
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        EventBus.getDefault().post(new EventUpdateFirebaseTokenFailure());
    }
}