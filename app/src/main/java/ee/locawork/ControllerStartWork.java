package ee.locawork;

import android.content.Context;

import ee.locawork.model.ResponseModel;
import ee.locawork.model.dto.StartTimeDTO;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.locawork.EventStartWorkFailure;

import java.util.Date;

import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerStartWork implements Callback<ResponseModel> {
    public void postData(Context context, StartTimeDTO startTimeDTO) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient().create()))
                .build().create(ServiceWorkStatus.class)
                .startWork(PreferencesUtil
                        .readString(context, PreferencesUtil.KEY_TOKEN, ""), startTimeDTO)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new EventStartWork(response));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new EventStartWorkFailure(t));
    }
}
