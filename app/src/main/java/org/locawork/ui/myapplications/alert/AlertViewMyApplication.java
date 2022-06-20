package org.locawork.ui.myapplications.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.locawork.R;
import org.locawork.model.MyApplicationDTO;
import org.locawork.ui.myapplications.ControllerCancelApplication;
import org.locawork.ui.mycandidates.alert.ControllerChooseCandidate;
import org.locawork.util.DialogUtils;
import org.locawork.util.PreferencesUtil;

import androidx.appcompat.app.AlertDialog;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AlertViewMyApplication {

    public static void init(final Context context, final MyApplicationDTO jobApplication) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.FullscreenDialog);
        final ControllerChooseCandidate controllerChooseCandidate = new ControllerChooseCandidate();
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_view_my_application, (ViewGroup) null);

        dialogBuilder.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(jobApplication.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(jobApplication.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.valueOf(jobApplication.getSalary()));
        ((TextView)dialogView.findViewById(R.id.applyer_email)).setText(jobApplication.getAccountEmail());
        ((TextView)dialogView.findViewById(R.id.title)).setText(jobApplication.getTitle());
        final AlertDialog alertDialog = dialogBuilder.create();

        ((ImageButton)dialogView.findViewById(R.id.dismiss)).setOnClickListener(v -> {
            new ControllerCancelApplication().cancelApplication(context, jobApplication.getId(), alertDialog);
        });
        dialogView.findViewById(R.id.back).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
