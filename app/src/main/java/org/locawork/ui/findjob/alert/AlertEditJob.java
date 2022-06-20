package org.locawork.ui.findjob.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.findjob.ControllerEditJob;
import org.locawork.util.DialogUtils;

public class AlertEditJob {
    public static void init(final Activity activity, final Context context, final Job job) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_update_job, null);
        final EditText jobTitle = dialogView.findViewById(R.id.job_title);
        final EditText jobDescription = dialogView.findViewById(R.id.job_description);
        final EditText salary = dialogView.findViewById(R.id.salary);

        ImageButton back = dialogView.findViewById(R.id.back);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.edit_job));
        alertDialog.setView(dialogView);

        alertDialog.setView(dialogView);
        jobTitle.setText(job.getTitle());
        jobDescription.setText(job.getDescription());
        jobTitle.setImeOptions(6);
        jobTitle.setRawInputType(1);
        jobDescription.setImeOptions(6);
        jobDescription.setRawInputType(1);
        salary.setText(Double.toString(job.getSalary()));

        dialogView.findViewById(R.id.submit).setOnClickListener(v -> {
            alertDialog.cancel();
            job.setTitle(jobTitle.getText().toString());
            job.setSalary(Double.parseDouble(salary.getText().toString()));
            job.setDescription(jobDescription.getText().toString());
            if (jobTitle.getText().length() == 0 || jobDescription.getText().length() == 0 || salary.getText().length() == 0) {
                Toast.makeText(context, activity.getResources().getString(R.string.please_fill_in_all_data), Toast.LENGTH_LONG).show();
            } else {
                activity.runOnUiThread(() -> new ControllerEditJob(alertDialog).putData(context, job));
            }
        });
        back.setOnClickListener(v -> alertDialog.cancel());

        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
