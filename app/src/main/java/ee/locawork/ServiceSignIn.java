package ee.locawork;

import ee.locawork.model.User;
import ee.locawork.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceSignIn {
    @POST("users/")
    Call<User> postUser(@Body User user);

    @POST("login")
    Call<Void> login(@Body UserLogin user);


    @POST("auth/authenticate")
    Call<Void> authenticate(@Body UserLogin user);
}
