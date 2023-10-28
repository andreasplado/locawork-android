package ee.locawork.ui.myupcomingwork.alert;

import ee.locawork.model.Job;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServiceCantGo {
    @PUT("jobs/cant-go-to-work")
    Call<Job> putCantGoToWork(@Header("Authorization") String auth, @Query("jobId") Integer jobId, @Query("reason") String reason);
}
