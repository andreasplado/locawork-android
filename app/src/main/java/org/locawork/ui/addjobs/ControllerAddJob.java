package org.locawork.ui.addjobs;

import android.content.Context;

import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ControllerAddJob implements Callback<Job> {
    public void postData(Context context, Job job) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceAddJob.class).addJob(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), job).enqueue(this);
    }

    public void onResponse(Call<Job> call, Response<Job> response) {
        EventBus.getDefault().post(new EventAddJobNetSuccess(response.body()));
    }

    public void onFailure(Call<Job> call, Throwable t) {
        EventBus.getDefault().post(new EventAddJobNetErr(t));
    }
}
