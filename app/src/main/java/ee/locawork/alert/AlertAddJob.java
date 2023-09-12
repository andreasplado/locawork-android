package ee.locawork.alert;


import static ee.locawork.util.PreferencesUtil.KEY_CARD_PARAMS;
import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.List;

import ee.locawork.R;
import ee.locawork.model.dto.AddingJobDTO;
import ee.locawork.ui.payformemeber.alert.ControllerPayForStartingGivingWork;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;

public class AlertAddJob {

    public static void init(final Activity activity, final Context context, final ImageButton addButton, final SpinKitView loadingView) {
        String location;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_add_work, (ViewGroup) null);
        TextView etLocation = dialogView.findViewById(R.id.location_text);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        TextView title = dialogView.findViewById(R.id.title);
        final EditText jobTitle = dialogView.findViewById(R.id.job_title);
        final EditText jobDescription = dialogView.findViewById(R.id.job_description);
        final EditText salary = dialogView.findViewById(R.id.salary);
        final EditText unitsToWork = dialogView.findViewById(R.id.units_to_work);
        final Spinner payroll = dialogView.findViewById(R.id.payroll);
        ImageButton back = dialogView.findViewById(R.id.back);

        List<String> payrollValue = new ArrayList<>();
        payrollValue.add(context.getResources().getString(R.string.hours));
        payrollValue.add(context.getResources().getString(R.string.days));



        ArrayAdapter<String> payrollAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item , payrollValue);
        payrollAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        payroll.setAdapter(payrollAdapter);

        payroll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    unitsToWork.setHint(context.getResources().getString(R.string.hours));
                }
                if(position == 1){
                    unitsToWork.setHint(context.getResources().getString(R.string.days));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobTitle.setImeOptions(6);
        jobTitle.setRawInputType(1);
        title.setText(context.getResources().getString(R.string.add_job));
        jobDescription.setImeOptions(6);
        jobDescription.setRawInputType(1);
        final LocationUtil locationUtil = new LocationUtil(activity, activity.getApplicationContext());
        locationUtil.init();
        if (locationUtil.lococation != null) {
            location = LocationUtil.fetchLocationData(activity, new LatLng(locationUtil.lococation.getLatitude(), locationUtil.lococation.getLongitude()));
        } else {
            location = context.getResources().getString(R.string.undefined_location);
        }
        etLocation.setText(location);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.show();
        submit.setOnClickListener(v -> {
            addButton.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
            if (!jobTitle.getText().toString().isEmpty() || !jobDescription.getText().toString().isEmpty() || !salary.getText().toString().isEmpty()) {
                AddingJobDTO job = new AddingJobDTO();
                job.setJobTitle(jobTitle.getText().toString().trim());
                job.setDescription(jobDescription.getText().toString());
                job.setCategoryId(2);
                job.setLatitude(locationUtil.lococation.getLatitude());
                job.setLongitude(locationUtil.lococation.getLongitude());
                job.setPayroll(String.valueOf(payroll.getSelectedItem()));
                job.setSalary(Double.valueOf(salary.getText().toString()).doubleValue());
                job.setHoursToWork(Double.parseDouble(unitsToWork.getText().toString()));
                job.setUserId(PreferencesUtil.readInt(context, KEY_USER_ID, 0));

                Gson gson = new Gson();
                String cardParamsJSON = PreferencesUtil.readString(context, KEY_CARD_PARAMS, "");
                CardParams card = gson.fromJson(cardParamsJSON, CardParams.class);

                //NOW POSTING JOB WITH 2 EURO PAYMENT
                if (card != null) {
                    // Create a Stripe token from the card details
                    Stripe stripe = new Stripe(context, PaymentConfiguration.getInstance(context).getPublishableKey());
                    stripe.createCardToken(card, new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(@NonNull Token result) {
                            String tokenID = result.getId();
                            // Send the token identifier to the server...
                            //Toast.makeText(context, tokenID, Toast.LENGTH_LONG).show();


                            ControllerPayForStartingGivingWork controllerPayForRemovingAdds = new ControllerPayForStartingGivingWork();

                            job.setToken(tokenID);
                            controllerPayForRemovingAdds.addJob(context, job);

                            alertDialog.cancel();
                        }

                        @Override
                        public void onError(@NonNull Exception e) {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();


                        }
                    });
                }

                //new ControllerAddJob().postData(context, job);
                alertDialog.cancel();
                return;
            }
            Toast.makeText(context, context.getResources().getString(R.string.please_fill_in_all_data), Toast.LENGTH_LONG).show();
        });
        back.setOnClickListener(view -> alertDialog.cancel());
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
