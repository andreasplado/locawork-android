package ee.locawork;

import ee.locawork.model.MainData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMainData {
    @GET("jobs/get-main-data")
    Call<String> getMainData(@Header("authorization") String auth, @Query("userId") Integer num);
}
