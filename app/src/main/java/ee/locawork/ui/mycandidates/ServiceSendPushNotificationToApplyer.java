package ee.locawork.ui.mycandidates;

import ee.locawork.model.PushNotificationRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceSendPushNotificationToApplyer {
    @POST("notification/send-notification")
    Call<String> send(@Header("Authorization") String auth, @Body PushNotificationRequest pushNotificationRequest);
}
