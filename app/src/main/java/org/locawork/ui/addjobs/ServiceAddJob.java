package org.locawork.ui.addjobs;

import org.locawork.model.Job;
import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAddJob {
    @POST("/jobs")
    Call<Job> addJob(@Header("Authorization") String auth, @Body Job job);

    @GET("jobs/getjobsbyaccount")
    Call<List<Job>> getUserJobs(@Header("Authorization") String auth, @Query("userId") Integer num);
}
