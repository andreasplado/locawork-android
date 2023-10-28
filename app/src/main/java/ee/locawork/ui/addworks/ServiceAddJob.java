package ee.locawork.ui.addworks;

import ee.locawork.model.Job;

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

    @GET("jobs/get-undone-posted-jobs")
    Call<List<Job>> getUndonePostedJobs(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobs/get-done-posted-jobs")
    Call<List<Job>> getDonePostedJobs(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobs/get-all-posted-jobs")
    Call<List<Job>> getAllPostedJobs(@Header("Authorization") String auth, @Query("userId") Integer num);
}
