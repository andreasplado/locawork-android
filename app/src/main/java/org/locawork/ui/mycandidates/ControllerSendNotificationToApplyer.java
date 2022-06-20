package org.locawork.ui.mycandidates;

import android.content.Context;
import android.widget.Toast;
import org.locawork.model.PushNotificationRequest;
import org.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerSendNotificationToApplyer implements Callback<String> {
    private Context context;

    /* access modifiers changed from: package-private */
    public void postData(PushNotificationRequest pushNotificationRequest, Context context2) {
        this.context = context2;
        ((ServiceSendPushNotification) new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceSendPushNotification.class)).send(pushNotificationRequest).enqueue(this);
    }

    public void onResponse(Call<String> call, Response<String> response) {
        Toast.makeText(this.context, "Success", 1).show();
    }

    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this.context, "failure", 1).show();
    }
}
