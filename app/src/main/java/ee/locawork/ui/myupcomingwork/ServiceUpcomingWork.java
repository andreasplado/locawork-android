package ee.locawork.ui.myupcomingwork;

import ee.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceUpcomingWork {
    @GET("jobs/get-my-upcoming-work")
    Call<List<JobDTO>> getMyUpcomingWork(@Header("Authorization") String auth, @Query("userId") Integer num);
}
