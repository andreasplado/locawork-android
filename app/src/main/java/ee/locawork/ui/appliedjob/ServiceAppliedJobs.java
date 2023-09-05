package ee.locawork.ui.appliedjob;

import ee.locawork.model.JobWithCategory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceAppliedJobs {

    @GET("jobs/getappliedjobsbygoogleaccount")
    Call<JobWithCategory> getAppliedJobs(@Header("Authorization") String auth, @Query("googleAccountEmail") String accountGoogleId);
}
