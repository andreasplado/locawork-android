package ee.locawork;

import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCheckIfUserExists implements Callback<Boolean> {

    public void getData(String email) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceCheckIfUserExists.class).getMainData(email).enqueue(this);
    }

    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
        EventBus.getDefault().post(new EventCheckIfUserExists(response));
    }

    public void onFailure(Call<Boolean> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToGetMainData(t));
    }
}
