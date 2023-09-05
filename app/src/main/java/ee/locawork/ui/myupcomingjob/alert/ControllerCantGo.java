package ee.locawork.ui.myupcomingjob.alert;

import android.content.Context;

import ee.locawork.model.Job;
import ee.locawork.ui.myupcomingjob.EventUpcomingWorkNetFailure;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCantGo implements Callback<Job> {
    public void postData(Context context, Integer jobApplicationId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceCantGo.class).postCantGo(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), jobApplicationId).enqueue(this);
    }

    public void onResponse(Call<Job> call, Response<Job> response) {
        EventBus.getDefault().post(new EventCantGoNetSuccess(response.body()));
    }

    public void onFailure(Call<Job> call, Throwable t) {
        EventBus.getDefault().post(new EventUpcomingWorkNetFailure());
    }
}
