package org.locawork.ui.myapplications;

import org.locawork.model.JobApplicationDTO;
import org.locawork.model.MyApplicationDTO;
import org.locawork.model.MyApplications;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMyApplications {
    @DELETE("jobapplications/cancel-application")
    Call<MyApplicationDTO> cancelApplication(@Header("Authorization") String auth, @Query("applicationId") Integer num);

    @GET("jobapplications/my-applications")
    Call<MyApplications> getMyApplications(@Header("Authorization") String auth, @Query("userId") Integer num);
}
