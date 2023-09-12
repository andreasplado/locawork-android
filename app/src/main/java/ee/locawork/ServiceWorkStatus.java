package ee.locawork;

import ee.locawork.model.ResponseModel;

import ee.locawork.model.dto.EndTimeDTO;
import ee.locawork.model.dto.StartTimeDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceWorkStatus {
    @POST("jobs/start-work")
    Call<ResponseModel> startWork(@Header("Authorization") String auth, @Body StartTimeDTO startTimeDTO);

    @POST("jobs/end-work")
    Call<ResponseModel> endWork(@Header("Authorization") String auth, @Body EndTimeDTO endTimeDTO);
}
