package ee.locawork;

import ee.locawork.model.MainData;
import ee.locawork.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServiceUpdateUserFirebaseToken {
    @POST("users/update-firebase-token")
    Call<User> updateUserFirebaseToken(@Header("authorization") String auth, @Query("firebaseToken") String token, @Query("userId") int userId);
}
