package ee.locawork.ui.mycandidates;

import android.content.Context;
import android.widget.Toast;
import ee.locawork.model.PushNotificationRequest;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;

import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerSendNotificationToApplyer implements Callback<String> {
    private Context context;

    public void postData(PushNotificationRequest pushNotificationRequest, Context context2) {
        this.context = context2;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSendPushNotificationToApplyer.class).send(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, "") ,pushNotificationRequest).enqueue(this);
    }

    public void onResponse(Call<String> call, Response<String> response) {
    }

    public void onFailure(Call<String> call, Throwable t) {

    }
}
