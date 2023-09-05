package ee.locawork.ui.login;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.EventUserLoggedIn;
import ee.locawork.R;
import ee.locawork.ServiceSignIn;
import ee.locawork.model.UserLogin;
import ee.locawork.util.AppConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerLogin implements Callback<Void> {

    private Context context;
    public void login(UserLogin user, Context context) {
        this.context = context;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).
                addConverterFactory(GsonConverterFactory.
                        create(new GsonBuilder().setLenient().create())).build().
                create(ServiceSignIn.class).authenticate(user).enqueue(this);
    }

    public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.code() == 401){
            EventBus.getDefault().post(new EventUserFailedToLogin(null, context.getString(R.string.invalid_credentials)));
        }
        EventBus.getDefault().post(new EventUserLoggedIn(response));
    }

    public void onFailure(Call<Void> call, Throwable t) {
        EventBus.getDefault().post(new EventUserFailedToLogin(t, ""));
    }
}
