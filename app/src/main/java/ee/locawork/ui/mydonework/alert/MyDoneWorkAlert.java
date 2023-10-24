package ee.locawork.ui.mydonework.alert;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ee.locawork.R;
import ee.locawork.alert.AlertTranslate;
import ee.locawork.model.dto.JobDTO;
import ee.locawork.util.DialogUtils;

public class MyDoneWorkAlert {
    public static void init(Activity activity, Context context, JobDTO job, String location) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.FullscreenDialog).create();
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_my_done_work, null);
        alertDialog.setView(dialogView);
        long workStartTime = Long.parseLong(job.getWorkStartTime());
        long workEndTime = Long.parseLong(job.getWorkEndTime());
        long hoursSpendOnWork = workEndTime - workStartTime;
        long hours = TimeUnit.MILLISECONDS.toHours(hoursSpendOnWork * 60);
        LinearLayout reasonWhyUserLeftEarlier = dialogView.findViewById(R.id.reason_user_left_learlier_layout);
        ((ImageButton) dialogView.findViewById(R.id.translate)).setOnClickListener(v -> {
            String jobTitle = ((TextView)dialogView.findViewById(R.id.job_title)).getText().toString();
            String jobDescription = ((TextView)dialogView.findViewById(R.id.job_description)).getText().toString();
            ArrayList data = new ArrayList();
            data.add(activity.getResources().getString(R.string.my_work_title_label) + " " + jobTitle);
            data.add(activity.getResources().getString(R.string.my_work_description) + " " + jobDescription);
            data.add(activity.getResources().getString(R.string.label_payroll) + " " + job.getPayroll());
            data.add(activity.getResources().getString(R.string.label_worked_hours) + " " + hours);
            data.add(activity.getResources().getString(R.string.label_reason_of_leaving_earlier) + " ");
            AlertTranslate.init(activity, context, data);
        });
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(job.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(job.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.valueOf(job.getSalary()));
        ((TextView) dialogView.findViewById(R.id.location_name)).setText(location);
        ((TextView) dialogView.findViewById(R.id.payroll)).setText(job.getPayroll());


        ((TextView) dialogView.findViewById(R.id.required_working_hours)).setText(job.getHoursToWork() + "");
        ((TextView) dialogView.findViewById(R.id.worked_hours)).setText(hours + "");

        if(hours < Double.valueOf(job.getHoursToWork()).longValue()){
            reasonWhyUserLeftEarlier.setVisibility(View.VISIBLE);
        }else{
            reasonWhyUserLeftEarlier.setVisibility(View.GONE);
        }


        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.work) + job.getTitle());
        dialogView.findViewById(R.id.submit).setVisibility(View.GONE);
        dialogView.findViewById(R.id.back).setOnClickListener(v -> alertDialog.dismiss());
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
