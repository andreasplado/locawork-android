package ee.locawork;

import android.content.Context;
import android.view.View;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import ee.locawork.model.User;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerGetEmployerData implements Callback<User> {

    private View view;

    public void getData(Context context, Integer userId) {
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
        EventGetEmployerData eventGetEmployerData = new EventGetEmployerData(response);

        EventBus.getDefault().post(eventGetEmployerData);
    }

    public void onFailure(Call<User> call, Throwable t) {
        EventBus.getDefault().post(new EventFailedToGetEmployerData(t));
    }
}
