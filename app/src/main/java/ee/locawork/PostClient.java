package ee.locawork;

import ee.locawork.model.Post;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface PostClient {
    @PUT("userlog")
    Call<Post> putDetails(@Body Post post);
}
