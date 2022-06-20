package org.locawork.ui.mydonejobs;

import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMyDoneJobs {
    @GET("jobs/getmydonework")
    Call<List<JobDTO>> getMyDoneWork(@Header("Authorization") String auth, @Query("userId") Integer num);
}
