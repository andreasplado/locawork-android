package org.locawork.ui.myaddedjob;

import android.content.Context;

import org.locawork.model.Job;
import org.locawork.model.JobWithCategory;
import org.locawork.ui.findjob.EventApplied;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerDeleteJob implements Callback<Job> {
    public void deleteJob(Context context, int jobId) {
        int lol = jobId;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceAddedJobs.class).deleleteJob(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), jobId).enqueue(this);
    }

    public void onResponse(Call<Job> call, Response<Job> response) {
        EventBus.getDefault().post(new EventMyAddedWorkDeleteSuccess(response.body()));
    }

    public void onFailure(Call<Job> call, Throwable t) {
        EventBus.getDefault().post(new EventMyAddedWorkDeleteFailure(t));
    }
}
