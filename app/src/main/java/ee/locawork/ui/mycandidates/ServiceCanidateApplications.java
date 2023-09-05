package ee.locawork.ui.mycandidates;

import ee.locawork.model.JobApplications;
import ee.locawork.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceCanidateApplications {
    @POST("jobapplications/apply")
    Call<ResponseModel> apply(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("applyerId") Integer num2);

    @GET("jobapplications/candidates")
    Call<JobApplications> getCandidates(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobapplications/candidates-filtered")
    Call<JobApplications> getCandidatesWithFilter(@Header("Authorization") String auth, @Query("userId") Integer userId, @Query("filter")String filter);
}
