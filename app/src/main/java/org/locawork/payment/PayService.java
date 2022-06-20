package org.locawork.payment;

import org.locawork.model.User;
import org.locawork.model.payment.Pay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PayService {
    @POST("payment/get-payment-data")
    Call<String> pay(@Body Pay pay);

    @POST("payment/pay")
    Call<String> pay(@Body PurchaseData user);
}
