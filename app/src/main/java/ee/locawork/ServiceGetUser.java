package ee.locawork;

import ee.locawork.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceGetUser {
    @GET("users/get-user")
    Call<User> getuser(@Header("authorization") String auth, @Query("userId") Integer id);
}
