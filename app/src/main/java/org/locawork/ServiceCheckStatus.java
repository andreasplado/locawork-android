package org.locawork;

import org.locawork.model.User;
import org.locawork.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceCheckStatus {
    @GET("users/")
    Call<User> postUser(@Body User user);

    @POST("login")
    Call<Void> login(@Body UserLogin user);
}
