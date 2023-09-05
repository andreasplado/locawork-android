package ee.locawork.ui.pushnotification;

import ee.locawork.model.Note;
import ee.locawork.model.pushnotification.NotificationRequestDto;
import ee.locawork.model.pushnotification.SubscriptionRequestDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServicePushNotification {

    @POST("notification/subscribe")
    Call<Void> subscribeToTopic(@Header("Authorization") String auth, @Body SubscriptionRequestDto subscriptionRequestDto);

    @POST("notification/unsubscribe")
    Call<Void> unsubscribeFromTopic(@Header("Authorization") String auth, @Body SubscriptionRequestDto subscriptionRequestDto);

    @POST("notification/token")
    Call<String> sendPnsToDevice(@Header("Authorization") String auth, @Body NotificationRequestDto notificationRequestDto);

    @POST("notification/topic")
    Call<String> sendPnsToTopic(@Header("Authorization") String auth, @Body NotificationRequestDto notificationRequestDto);

    @GET("users/get-offerer-firebase-token")
    Call<Note> getOffererFirebaseToken(@Header("Authorization") String auth, @Query("offererId") Integer offerer);


}
