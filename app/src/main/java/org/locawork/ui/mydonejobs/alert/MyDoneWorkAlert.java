package org.locawork.ui.mydonejobs.alert;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.util.DialogUtils;

public class MyDoneWorkAlert {
    public static void init(Activity activity, Context context, JobDTO job, String location) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.FullscreenDialog).create();
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_my_done_work, null);
        alertDialog.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(job.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(job.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.valueOf(job.getSalary()));
        ((TextView) dialogView.findViewById(R.id.location_name)).setText(location);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.work) + job.getTitle());
        dialogView.findViewById(R.id.submit).setVisibility(View.GONE);
        dialogView.findViewById(R.id.back).setOnClickListener(v -> alertDialog.dismiss());
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
