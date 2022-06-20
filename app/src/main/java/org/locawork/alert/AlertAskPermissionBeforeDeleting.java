package org.locawork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.JobApplicationDTO;
import org.locawork.model.MyApplicationDTO;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.myaddedjob.ControllerDeleteJob;
import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;
import com.ramotion.fluidslider.FluidSlider;

public class AlertAskPermissionBeforeDeleting {
    public static void init(Activity activity, final Context context, final Job job) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_delete_job, null);
        alertDialog.setView(dialogView);
        alertDialog.show();
        final CheckBox dontAskMeAgainCb = dialogView.findViewById(R.id.cb_dont_ask_me_again);
        dialogView.findViewById(R.id.submit).setOnClickListener(v -> {
            if (dontAskMeAgainCb.isChecked()) {
                PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, "1");
            } else {
                PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START);
            }
            new ControllerDeleteJob().deleteJob(context, job.getId());
            alertDialog.cancel();
        });
        dialogView.findViewById(R.id.back).setOnClickListener(v -> alertDialog.dismiss());
    }

    public static void init(Activity activity, final Context context, final JobApplicationDTO job) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_delete_job, (ViewGroup) null);
        final CheckBox dontAskMeAgainCb = dialogView.findViewById(R.id.cb_dont_ask_me_again);
        alertDialog.setView(dialogView);
        alertDialog.show();
        ((ImageButton) dialogView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dontAskMeAgainCb.isChecked()) {
                    PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, "1");
                } else {
                    PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START);
                }
                new ControllerDeleteJob().deleteJob(context, job.getId().intValue());
                alertDialog.cancel();
            }
        });
    }

    public static void init(Activity activity, final Context context, final MyApplicationDTO job) {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity,R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_delete_job, (ViewGroup) null);
        final CheckBox dontAskMeAgainCb = dialogView.findViewById(R.id.cb_dont_ask_me_again);
        alertDialog.setView(dialogView);
        alertDialog.show();
        ((ImageButton) dialogView.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dontAskMeAgainCb.isChecked()) {
                    PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, "1");
                } else {
                    PreferencesUtil.save(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START);
                }
                new ControllerDeleteJob().deleteJob(context, job.getId().intValue());
                alertDialog.cancel();
            }
        });
    }
}
