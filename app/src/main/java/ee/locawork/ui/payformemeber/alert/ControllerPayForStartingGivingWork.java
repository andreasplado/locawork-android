package ee.locawork.ui.payformemeber.alert;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.alert.PayForStartingGivingWork;
import ee.locawork.model.ResponseModel;
import ee.locawork.model.dto.AddingJobDTO;
import ee.locawork.ui.payformemeber.PayForRemovingAdds;
import ee.locawork.alert.PayForStartGivingWorkFailure;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerPayForStartingGivingWork implements Callback<ResponseModel> {

    public void addJob(Context context, AddingJobDTO addingJobDTO) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient()
                                .create())).build().create(MemberService.class)
                .payForGivingWork(PreferencesUtil
                        .readString(context, PreferencesUtil.KEY_TOKEN, ""), addingJobDTO)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new PayForStartingGivingWork(response));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new PayForStartGivingWorkFailure(t));
    }
}