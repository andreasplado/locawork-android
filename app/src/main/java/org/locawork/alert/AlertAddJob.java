package org.locawork.alert;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.ui.addjobs.ControllerAddJob;
import org.locawork.util.DialogUtils;
import org.locawork.util.GoogleUserData;
import org.locawork.util.LocationUtil;
import org.locawork.util.PreferencesUtil;
import com.google.android.gms.maps.model.LatLng;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AlertAddJob {

    public static void init(final Activity activity, final Context context) {
        String location;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_add_work, (ViewGroup) null);
        TextView etLocation = dialogView.findViewById(R.id.location_text);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        TextView title = dialogView.findViewById(R.id.title);
        final EditText jobTitle = dialogView.findViewById(R.id.job_title);
        final EditText jobDescription = dialogView.findViewById(R.id.job_description);
        final EditText salary = dialogView.findViewById(R.id.salary);
        ImageButton back = dialogView.findViewById(R.id.back);
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
            if (!jobTitle.getText().toString().isEmpty() || !jobDescription.getText().toString().isEmpty() || !salary.getText().toString().isEmpty()) {
                Job job = new Job();
                job.setTitle(jobTitle.getText().toString().trim());
                job.setDescription(jobDescription.getText().toString());
                job.setCategoryId(2);
                job.setLatitude(locationUtil.lococation.getLatitude());
                job.setLongitude(locationUtil.lococation.getLongitude());
                job.setAccountGoogleId(GoogleUserData.getUserEmail(activity));
                job.setSalary(Double.valueOf(salary.getText().toString()).doubleValue());
                job.setUserId(PreferencesUtil.readInt(context, KEY_USER_ID, 0));
                new ControllerAddJob().postData(context, job);
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
