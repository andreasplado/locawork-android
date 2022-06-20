package org.locawork.ui.findjob;

import android.content.Context;
import org.locawork.model.JobApplication;
import org.locawork.model.ResponseModel;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerApplyToJob implements Callback<ResponseModel> {

    private int offererId;


    public void postApplication(Context context, JobApplication jobApplication, int offererId) {
        this.offererId = offererId;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceFindJob.class).postApplication(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), jobApplication).enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new EventApplied(response.body(), offererId));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new EventApplyFailure(t));
    }
}
