package ee.locawork.ui.findwork;

import android.app.AlertDialog;
import android.content.Context;

import ee.locawork.model.Job;
import ee.locawork.ui.findwork.alert.EventEditJob;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ControllerEditJob implements Callback<Job> {
    private AlertDialog alertDialog;

    public ControllerEditJob(AlertDialog alertDialog2) {
        this.alertDialog = alertDialog2;
    }

    public void putData(Context context, Job job) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceFindJob.class).editJob(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), job.getId(), job).enqueue(this);
        EventBus.getDefault().post(new EventNetConnection());
    }

    public void onResponse(Call<Job> call, Response<Job> response) {
        EventBus.getDefault().post(new EventEditJob(response.body(), this.alertDialog));
    }

    public void onFailure(Call<Job> call, Throwable t) {
        EventBus.getDefault().post(new EventEditJobNetErr(t));
    }
}
