package ee.locawork.ui.mydonejobs;

import ee.locawork.model.JobWithCategory;
import ee.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMyDoneJobs {
    @GET("jobs/getmydonework")
    Call<List<JobDTO>> getMyDoneWork(@Header("Authorization") String auth, @Query("userId") Integer num);
}
