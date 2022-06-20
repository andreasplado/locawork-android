package org.locawork.ui.addedjob;

import android.content.Context;

import org.locawork.model.Job;
import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.addjobs.ServiceAddJob;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.ui.myaddedjob.EventAddedJobsNetFailure;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static org.locawork.util.AppConstants.BASE_URL;
public class ControllerAddedJobs implements Callback<List<Job>> {


    void getData(Context context, Integer accountGoogleId) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ServiceAddJob serviceAddJob = retrofit.create(ServiceAddJob.class);

        Call<List<Job>> call = serviceAddJob.getUserJobs(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), accountGoogleId);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Job>> call, Response<List<Job>> response) {
        EventBus.getDefault().post(new EventAddedJobsNetSuccess(response.body()));
    }

    @Override
    public void onFailure(Call<List<Job>> call, Throwable t) {
        EventBus.getDefault().post(new EventAddedJobsNetFailure(t));
    }
}