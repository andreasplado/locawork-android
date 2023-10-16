package ee.locawork.ui.myupcomingwork.alert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import ee.locawork.R;
import ee.locawork.model.dto.JobDTO;
import ee.locawork.services.ServiceReachedJob;
import ee.locawork.ui.myupcomingwork.EventGoingToWork;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;

public class AlertGoToJob {
    public static void init(Activity activity, final Context context, final JobDTO job) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.FullscreenDialog).create();
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_go_to_job, null);
        alertDialog.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(job.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(job.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.format("%1$,.2f", new Object[]{Double.valueOf(job.getSalary())}));
        ((TextView) dialogView.findViewById(R.id.location)).setText(LocationUtil.fetchLocationData(activity, new LatLng(job.getLatitude(), job.getLongitude())));
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.my_upcoming_work));
        dialogView.findViewById(R.id.back).setOnClickListener(v -> {
           alertDialog.dismiss();
        });
        dialogView.findViewById(R.id.submit).setOnClickListener(v -> {
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_TITLE, job.getTitle());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_DESCRIPTION, job.getDescription());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_SALARY, String.valueOf(job.getSalary()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LATITUDE, String.valueOf(job.getLatitude()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LONGITUDE, String.valueOf(job.getLongitude()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_STATUS, job.getStatus());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_HOURS_TO_WORK, (job.getHoursToWork()).longValue() * 60 * 60 * 1000);

            Intent i = new Intent(context, ServiceReachedJob.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(ServiceReachedJob.KEY_JOB_LONGITUDE, job.getLongitude());
            bundle.putDouble(ServiceReachedJob.KEY_JOB_LATITUDE, job.getLatitude());
            i.putExtras(bundle);
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_TITLE, job.getTitle());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_DESCRIPTION, job.getDescription());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_SALARY, String.valueOf(job.getSalary()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LATITUDE, String.valueOf(job.getLatitude()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LONGITUDE, String.valueOf(job.getLongitude()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_ID, Integer.valueOf(job.getId()));
            context.startService(i);
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + job.getLatitude() + "," + job.getLongitude() + "&travelmode=driving")));
            EventBus.getDefault().post(new EventGoingToWork());
        });

        dialogView.findViewById(R.id.dismiss).setOnClickListener(v -> AlertCantGo.init(context, job.getId()));
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
