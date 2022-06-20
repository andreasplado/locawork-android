package org.locawork.ui.myupcomingjob;

import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceUpcomingWork {
    @GET("jobs/get-my-upcoming-work")
    Call<List<JobDTO>> getMyUpcomingWork(@Header("Authorization") String auth, @Query("userId") Integer num);
}
