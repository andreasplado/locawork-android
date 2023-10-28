package ee.locawork.util;

import android.content.Context;
import android.widget.Toast;
import ee.locawork.PostClient;
import ee.locawork.R;
import ee.locawork.model.Post;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecurityUtil {
    public static void putUser(final Context context) {
        ((PostClient) new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(PostClient.class)).putDetails(new Post("", "")).enqueue(new Callback<Post>() {
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    PreferencesUtil.save(context, "login_data", response.body().getToken());
                } else if (response.code() == 400) {
                    Toast.makeText(context, context.getResources().getString(R.string.invalid_credentials), 1).show();
                }
            }

            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.server_error), 1).show();
            }
        });
    }
}
