package ee.locawork.ui.payformemeber.alert;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.model.PayingToken;
import ee.locawork.model.ResponseModel;
import ee.locawork.ui.payformemeber.PayForRemovingAddsFailure;
import ee.locawork.ui.payformemeber.PayForRemovingAdds;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerPayForRemovingAdds implements Callback<ResponseModel> {

    public void payForAdds(Context context, PayingToken token) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient()
                                .create())).build().create(MemberService.class)
                .payForRemovingAdds(PreferencesUtil
                        .readString(context, PreferencesUtil.KEY_TOKEN, ""), token)
                .enqueue(this);
    }

    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
        EventBus.getDefault().post(new PayForRemovingAdds(response));
    }

    public void onFailure(Call<ResponseModel> call, Throwable t) {
        EventBus.getDefault().post(new PayForRemovingAddsFailure(t));
    }
}