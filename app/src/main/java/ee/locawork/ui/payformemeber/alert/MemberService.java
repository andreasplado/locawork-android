package ee.locawork.ui.payformemeber.alert;

import ee.locawork.model.PayingToken;
import ee.locawork.model.ResponseModel;
import ee.locawork.model.dto.AddingJobDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MemberService {
    @POST("users/subscribe-for-removing-ads")
    Call<ResponseModel> payForRemovingAdds(@Header("Authorization") String auth, @Body PayingToken token);

    @POST("users/pay-for-giving-work")
    Call<ResponseModel> payForGivingWork(@Header("Authorization") String auth, @Body AddingJobDTO addingJobDTO);


    @POST("users/is-member")
    Call<ResponseModel> isMember(@Header("Authorization") String auth, @Query("userId") Integer userId );


    @GET("users/pay-for-member")
    Call<String> getPaymentData(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("role") String role);

    @GET("users/pay-for-member")
    Call<String> payMemebrSetRadiusOver2Km(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("role") String role);
}
