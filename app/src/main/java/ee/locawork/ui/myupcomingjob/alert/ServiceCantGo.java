package ee.locawork.ui.myupcomingjob.alert;

import ee.locawork.model.Job;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceCantGo {
    @POST("jobapplications/cancel-confiremd-application")
    Call<Job> postCantGo(@Header("Authorization") String auth, @Query("applicationId") Integer num);
}
