package org.locawork.ui.myupcomingjob;

import android.content.Context;

import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerUpcomingWork implements Callback<List<JobDTO>> {
    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceUpcomingWork.class).getMyUpcomingWork(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
        EventBus.getDefault().post(new EventUpcomingWorkNetSuccess(response.body()));
    }

    public void onFailure(Call<List<JobDTO>> call, Throwable t) {
        EventBus.getDefault().post(new EventUpcomingWorkNetFailure());
    }
}
