package ee.locawork.ui.myaddedjob;

import android.content.Context;
import android.view.View;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import ee.locawork.EventFailedToGetUserData;
import ee.locawork.EventGetUserData;
import ee.locawork.ServiceGetUser;
import ee.locawork.model.User;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerGetCurrentlyWorkingPersonData implements Callback<User> {

    private View view;

    public void getData(Context context, Integer userId, View view) {
        this.view = view;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient().create()))
                .build().create(ServiceGetUser.class)
                .getuser(PreferencesUtil
                        .readString(context, PreferencesUtil.KEY_TOKEN, ""), userId)
                .enqueue(this);
    }

    public void onResponse(Call<User> call, Response<User> response) {
        EventGetUserData eventGetUserData = new EventGetUserData(response, view);

        EventBus.getDefault().post(eventGetUserData);
    }

    public void onFailure(Call<User> call, Throwable t) {
        EventBus.getDefault().post(new EventFailedToGetUserData(t));
    }
}
