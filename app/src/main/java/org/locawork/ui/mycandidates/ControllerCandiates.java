package org.locawork.ui.mycandidates;

import android.content.Context;

import org.locawork.model.JobApplications;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCandiates implements Callback<JobApplications> {
    public void getData(Context context, Integer userId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceCanidateApplications.class).getCandidates(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId).enqueue(this);
    }

    public void getDatawithFilter(Context context, Integer userId, String filter) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceCanidateApplications.class).getCandidatesWithFilter(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId, filter).enqueue(this);
    }

    public void onResponse(Call<JobApplications> call, Response<JobApplications> response) {
        EventBus.getDefault().post(new EventCandidateApplicationsNetSuccess(response.body()));
    }

    public void onFailure(Call<JobApplications> call, Throwable t) {
        EventBus.getDefault().post(new EventCandidatesNetFailure(t));
    }
}
