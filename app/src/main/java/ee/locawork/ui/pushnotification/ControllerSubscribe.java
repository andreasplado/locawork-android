package ee.locawork.ui.pushnotification;


import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.model.pushnotification.SubscriptionRequestDto;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerSubscribe implements Callback<Void> {

    private Context context;
    private List<String> tokens;

    public ControllerSubscribe(Context context, SubscriptionRequestDto subscriptionRequestDto, List<String> tokens){
        this.context = context;
        this.tokens = tokens;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServicePushNotification.class).subscribeToTopic(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), subscriptionRequestDto).enqueue(this);
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        EventBus.getDefault().post(new EventSusbscribeSuccess());
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(new EventSusbscribeFailure());
    }
}
