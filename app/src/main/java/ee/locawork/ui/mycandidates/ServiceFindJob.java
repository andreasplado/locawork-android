package ee.locawork.ui.mycandidates;

import ee.locawork.model.Job;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ServiceFindJob {
    @GET("jobs/{jobid}")
    Call<Job> getJob(@Header("Authorization") String auth, @Path("jobid") Integer num);
}
