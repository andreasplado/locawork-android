package ee.locawork.ui.myapplications;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;

import ee.locawork.model.MyApplicationDTO;
import ee.locawork.util.AppConstants;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import ee.locawork.util.PreferencesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerCancelApplication implements Callback<MyApplicationDTO> {

    private AlertDialog alertDialog;

    public void cancelApplication(Context context, Integer applicationId) {
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceMyApplications.class).cancelApplication(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), applicationId).enqueue(this);
    }

    public void cancelApplication(Context context, Integer applicationId, AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
        new Retrofit.Builder().baseUrl(AppConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create())).build().create(ServiceMyApplications.class).cancelApplication(PreferencesUtil.readString(context, PreferencesUtil.KEY_TOKEN, ""), applicationId).enqueue(this);
    }

    public void onResponse(Call<MyApplicationDTO> call, Response<MyApplicationDTO> response) {
        EventBus.getDefault().post(new EventMyApplicationCancelSuccess(response.body()));
        if(alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public void onFailure(Call<MyApplicationDTO> call, Throwable t) {
        EventBus.getDefault().post(new EventMyApplicationsCancelFailure(t));
    }
}
