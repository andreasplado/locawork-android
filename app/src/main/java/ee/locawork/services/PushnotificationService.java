package ee.locawork.services;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PushnotificationService {
    @POST("http://locawork.org/push.php")
    Call<Boolean> signup(@Query("id") String registrationToken);
}
