package org.locawork.ui.payformemeber;

import org.locawork.model.Message;
import org.locawork.model.dto.JobDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ServicePayForMember {
    @GET("users/member-role")
    Call<String> getMemberRole(@Header("Authorization") String auth, @Query("userId") Integer num);


    @GET("users/pay-for-member")
    Call<String> getPaymentData(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("role") String role);

    @GET("users/pay-for-member")
    Call<String> payMemebrSetRadiusOver2Km(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("role") String role);
}
