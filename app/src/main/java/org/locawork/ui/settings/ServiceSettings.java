package org.locawork.ui.settings;

import org.locawork.model.Message;
import org.locawork.model.ResponseModel;
import org.locawork.model.Settings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServiceSettings {
    @GET("settings/get-user-settings")
    Call<Settings> getUserSettings(@Header("Authorization") String auth, @Query("userId") Integer userId);

    @POST("settings/set-initial-settings")
    Call<Settings> postUserSettings(@Header("Authorization") String auth, @Body Settings settings);

    @PUT("settings/update-ask-permissions-before-deleting-a-job")
    Call<Settings> updateAskPermissionBeforeDeletingWork(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("value") Boolean bool);

    @PUT("settings/update-biometric")
    Call<Settings> updateBiometric(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("value") Boolean bool);

    @PUT("settings/update-radius")
    Call<ResponseModel> updateRadius(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("radius") Double d);

    @PUT("settings/update-show-information-on-startup")
    Call<Settings> updateShowInformationOnStartup(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("value") Boolean bool);

    @PUT("settings/update-view-by-default")
    Call<Settings> updateViewByDefault(@Header("Authorization") String auth, @Query("userId") Integer num, @Query("value") String str);

    @POST("settings/logout")
    Call<Message> logout(@Header("Authorization") String auth, @Query("userId") Integer userId);
}
