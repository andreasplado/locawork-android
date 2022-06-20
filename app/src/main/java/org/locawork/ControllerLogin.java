package org.locawork;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.model.User;
import org.locawork.model.UserLogin;
import org.locawork.util.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerLogin implements Callback<Void> {
    public void login(UserLogin user) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSignIn.class).login(user).enqueue(this);
    }

    public void onResponse(Call<Void> call, Response<Void> response) {
        EventBus.getDefault().post(new EventUserLoggedIn(response));
    }

    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToLogin(t));
    }
}
