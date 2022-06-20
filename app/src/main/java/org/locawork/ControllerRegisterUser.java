package org.locawork;

import org.locawork.model.User;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerRegisterUser implements Callback<User> {
    public void signUp(User user) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSignup.class).signup(user).enqueue(this);
    }

    public void onResponse(Call<User> call, Response<User> response) {
        EventBus.getDefault().post(new EventUserRegistred(response));
    }

    public void onFailure(Call<User> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToRegister(t));
    }
}
