package org.locawork.ui.myupcomingjob.alert;

import org.locawork.model.Job;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceCantGo {
    @POST("jobapplications")
    Call<Job> postCantGo(@Header("Authorization") String auth, @Query("userId") Integer num);
}
