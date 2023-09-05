package ee.locawork;

import ee.locawork.model.ResponseModel;
import ee.locawork.model.User;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ServiceUpdateRole {
    @PUT("users/update-role")
    Call<String> updateRole(@Header("Authorization") String auth, @Query("userRole") String role, @Query("id") Integer id);
}
