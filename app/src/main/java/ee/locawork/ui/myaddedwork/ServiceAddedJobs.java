package ee.locawork.ui.myaddedwork;

import ee.locawork.model.Job;
import ee.locawork.model.JobWithCategory;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAddedJobs {
    @DELETE("/jobs/{jobid}")
    Call<Job> deleleteJob(@Header("Authorization") String auth, @Path("jobid") int i);

    @GET("jobs/getjobsbyaccount")
    Call<JobWithCategory> getUserJobs(@Header("Authorization") String auth, @Query("userId") Integer num);
}
