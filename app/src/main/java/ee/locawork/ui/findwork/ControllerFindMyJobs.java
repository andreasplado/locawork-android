package ee.locawork.ui.findwork;

import android.content.Context;

import ee.locawork.model.Job;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerFindMyJobs implements Callback<List<Job>> {

    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceFindJob.class).loadMyJobsWithCategories(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
        EventBus.getDefault().post(new EventNetConnection());
    }

    public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
        EventBus.getDefault().post(new EventFindJobNetSuccess(response));
    }

    public void onFailure(Call<List<Job>> call, Throwable t) {
        EventBus.getDefault().post(new EventFindJobNetErr(t));
    }
}
