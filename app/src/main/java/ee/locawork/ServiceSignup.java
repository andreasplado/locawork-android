package ee.locawork;

import ee.locawork.model.User;
import ee.locawork.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceSignup {
    @POST("users/signup")
    Call<User> signup(@Body User user);
}
