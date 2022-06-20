package org.locawork.ui.settings;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.model.Message;
import org.locawork.model.Settings;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerLogout implements Callback<Message> {
    public void postData(Context context, Integer userId) {
        Gson gson = new GsonBuilder().setLenient().create();
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build().create(ServiceSettings.class).logout(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void onResponse(Call<Message> call, Response<Message> response) {
        EventBus.getDefault().post(new EventLogoutSuccess(response));
    }

    public void onFailure(Call<Message> call, Throwable t) {
        EventBus.getDefault().post(new EventLogoutFailure(t));
    }
}
