package ee.locawork;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCheckIfWorkExists implements Callback<Boolean> {

    public void getData(Integer id) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().setLenient()
                                .create())).build().create(ServiceCheckIfWorkExists.class)
                .doesExists(id).enqueue(this);
    }

    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        EventBus.getDefault().post(new EventCheckIfWorkExists(response));
    }

    public void onFailure(Call<Boolean> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToGetMainData(t));
    }
}
