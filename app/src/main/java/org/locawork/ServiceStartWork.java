package org.locawork;

import org.locawork.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceStartWork {
    @POST("jobs/start-work")
    Call<ResponseModel> startWork(@Header("Authorization") String auth, @Query("applyerId") Integer num);
}
