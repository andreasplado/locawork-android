package org.locawork.ui.mycandidates;

import org.locawork.model.PushNotificationRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceSendPushNotification {
    @POST("notification/send-notification")
    Call<String> send(@Body PushNotificationRequest pushNotificationRequest);
}
