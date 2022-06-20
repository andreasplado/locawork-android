package org.locawork;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceCheckIfUserExists {
    @GET("users/does-exists")
    Call<Boolean> getMainData(@Query("email") String str);
}
