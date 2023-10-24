package ee.locawork.ui.mydonework;

import ee.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMyDoneJobs {
    @GET("jobs/getmydonework")
    Call<List<JobDTO>> getMyDoneWork(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobs/getmydonework/today")
    Call<List<JobDTO>> getMyDoneWorkToday(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobs/getmydonework/this-week")
    Call<List<JobDTO>> getMyDoneWorkThisWeek(@Header("Authorization") String auth, @Query("userId") Integer num);

    @GET("jobs/getmydonework/this-month")
    Call<List<JobDTO>> getMyDoneWorkThisMonth(@Header("Authorization") String auth, @Query("userId") Integer num);
}
