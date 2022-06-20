package org.locawork.ui.mycandidates.alert;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.model.ResponseModel;
import org.locawork.ui.mycandidates.ServiceCanidateApplications;
import org.locawork.util.AppConstants;
import org.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerChooseCandidate implements Callback<ResponseModel> {

    public void apply(Context context, Integer userId, Integer applyerId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceCanidateApplications.class).apply(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), userId, applyerId).enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new EventChooseCandidateApplicationsNetSuccess(response.body()));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new EventChooseCandidateApplicationsNetFailure(t));
    }
}
