package org.locawork.ui.findjob;

import android.app.AlertDialog;
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

public class ControllerDeleteJob implements Callback<Job> {
    private AlertDialog alertDialog;

    public void deleteData(Context context, Job job, AlertDialog alertDialog2) {
        this.alertDialog = alertDialog2;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceFindJob.class).deleteJob(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), job.getId()).enqueue(this);
        EventBus.getDefault().post(new EventNetConnection());
    }

    public void deleteData(Context context, Job job) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceFindJob.class).deleteJob(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), job.getId()).enqueue(this);
        EventBus.getDefault().post(new EventDeleteJob(job));
    }


    public void onResponse(Call<Job> call, Response<Job> response) {
        EventBus.getDefault().post(new EventDeleteJob(response.body(), this.alertDialog));
    }

    public void onFailure(Call<Job> call, Throwable t) {
        EventBus.getDefault().post(new EventDeleteJobNetErr(t));
    }
}
