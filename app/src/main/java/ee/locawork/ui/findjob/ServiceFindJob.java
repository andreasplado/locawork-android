package ee.locawork.ui.findjob;

import ee.locawork.model.Job;
import ee.locawork.model.JobApplication;
import ee.locawork.model.JobWithCategory;
import ee.locawork.model.ResponseModel;
import ee.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceFindJob {
    @DELETE("/jobs/{jobid}")
    Call<Job> deleteJob(@Header("Authorization") String auth, @Path("jobid") int i);

    @PUT("/jobs/{jobid}")
    Call<Job> editJob(@Header("Authorization") String auth, @Path("jobid") int i, @Body Job job);

    @GET("/jobs/getalljobsbylocation")
    Call<JobDTO> loadAllJobsWithCategoriesByLocation(@Header("Authorization") String auth, @Query("latitude") double d, @Query("longitude") double d2, @Query("distance") int i);

    @GET("/jobs")
    Call<JobDTO> loadJobsWithCategories();

    @GET("/jobs/getjobsbyaccount")
    Call<List<Job>> loadMyJobsWithCategories(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("/jobs/get-available-jobs")
    Call<List<Job>> getAvailableJobs(@Header("Authorization") String auth, @Query("latitude") double d, @Query("longitude") double d2, @Query("distance") int i, @Query("userId") Integer num);

    @POST("/jobapplications")
    Call<ResponseModel> postApplication(@Header("Authorization") String auth, @Body JobApplication jobApplication);

}
