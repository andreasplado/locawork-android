package ee.locawork.ui.findwork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.ramotion.fluidslider.FluidSlider;

import ee.locawork.R;
import ee.locawork.alert.AlertAskPermissionBeforeDeleting;
import ee.locawork.model.Job;
import ee.locawork.model.JobApplication;
import ee.locawork.ui.findwork.ControllerApplyToJob;
import ee.locawork.ui.findwork.ControllerDeleteJob;
import ee.locawork.util.AppConstants;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PrefConstants;
import ee.locawork.util.PreferencesUtil;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AlertViewJobInMap {
    public static void init(final Activity activity, final Context context, final Job job) {
        final AlertDialog jobDialog = new AlertDialog.Builder(activity,  R.style.FullscreenDialog).create();
        View dialogApplyJob = activity.getLayoutInflater().inflate(R.layout.alert_apply_work, null);
        View dialogPostedJob = activity.getLayoutInflater().inflate(R.layout.alert_posted_job_view, null);
        String myJobs = PreferencesUtil.readString(context, PrefConstants.KEY_LOCAWORK_PREFS, "");


            if (AppConstants.ROLE_JOB_OFFER.equals(myJobs)) {
                ((TextView) dialogPostedJob.findViewById(R.id.job_title)).setText(job.getTitle());
                ((TextView) dialogPostedJob.findViewById(R.id.salary)).setText(Double.toString(job.getSalary()));
                ((TextView) dialogPostedJob.findViewById(R.id.job_description)).setText(job.getDescription());
                ((TextView) dialogPostedJob.findViewById(R.id.title)).setText(context.getResources().getString(R.string.posted_job));
                ((TextView) dialogPostedJob.findViewById(R.id.title)).setText(context.getResources().getString(R.string.posted_job));
                ((TextView) dialogPostedJob.findViewById(R.id.location_name)).setText(LocationUtil.fetchLocationData(activity, new LatLng(job.getLatitude(), job.getLongitude())));
                dialogPostedJob.findViewById(R.id.back).setOnClickListener(v -> {
                    jobDialog.cancel();
                });
                dialogPostedJob.findViewById(R.id.edit_job).setOnClickListener(v -> {
                    AlertEditJob.init(activity, context, job);
                    jobDialog.cancel();
                });
                dialogPostedJob.findViewById(R.id.cancel_application).setOnClickListener(v -> {
                    if (PreferencesUtil.readString(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START).equals("1")) {
                        AlertAskPermissionBeforeDeleting.init(activity, context, job);
                    } else {
                        new ControllerDeleteJob().deleteData(context, job, jobDialog);
                    }
                });
                jobDialog.setView(dialogPostedJob);
            } else {
                jobDialog.setView(dialogApplyJob);
                ImageButton apply = dialogApplyJob.findViewById(R.id.submit);
                TextView jobDescription = dialogApplyJob.findViewById(R.id.job_description);
                TextView salary = dialogApplyJob.findViewById(R.id.salary);
                TextView locationName = dialogApplyJob.findViewById(R.id.location_name);
                ImageButton back = dialogApplyJob.findViewById(R.id.back);
                apply.setOnClickListener(v -> {

                    JobApplication jobApplication = new JobApplication();
                    jobApplication.setJob(job.getId());
                    jobApplication.setUser(PreferencesUtil.readInt(context, KEY_USER_ID, 0));
                    jobApplication.setApproved(false);
                    new ControllerApplyToJob().postApplication(context, jobApplication, job.getUserId());
                    jobDialog.dismiss();
                });
                ((TextView) dialogApplyJob.findViewById(R.id.job_provider)).setText(job.getAccountGoogleId());
                ((TextView) dialogApplyJob.findViewById(R.id.job_title)).setText(job.getTitle());
                ((TextView) dialogApplyJob.findViewById(R.id.title)).setText(context.getResources().getString(R.string.apply_to_work));
                back.setOnClickListener(v -> jobDialog.dismiss());
                salary.setText(String.format("%1$,.2f", new Object[]{Double.valueOf(job.getSalary())}));
                jobDescription.setText(job.getDescription());
                locationName.setText(LocationUtil.fetchLocationData(activity, new LatLng(job.getLatitude(), job.getLongitude())));
            }

        DialogUtils.setDialogOnTopOfScreen(jobDialog);
        jobDialog.show();

    }
}
