package org.locawork.payment;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.EventUserFailedToRegister;
import org.locawork.model.User;
import org.locawork.model.payment.Pay;
import org.locawork.util.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerEmpheralKey implements Callback<String> {
    public void post(Pay pay) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(PayService.class).pay(pay).enqueue(this);
    }

    public void onResponse(Call<String> call, Response<String> response) {
        EventBus.getDefault().post(new EventEmpheralKey(response));
    }

    public void onFailure(Call<String> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToRegister(t));
    }
}

