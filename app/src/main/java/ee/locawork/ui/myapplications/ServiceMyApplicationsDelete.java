package ee.locawork.ui.myapplications;

import ee.locawork.model.MyApplications;
import ee.locawork.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServiceMyApplicationsDelete {
    @DELETE("jobapplications/cancel-application")
    Call<ResponseModel> cancelApplication(@Header("Authorization") String auth, @Query("applicationId") Integer num);

    @GET("jobapplications/my-applications")
    Call<MyApplications> getMyApplications(@Header("Authorization") String auth, @Query("userId") Integer num);
}
