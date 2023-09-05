package ee.locawork;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceCheckIfWorkExists {
    @GET("jobs/does-exists")
    Call<Boolean> doesExists(@Query("workId") Integer workId);
}
