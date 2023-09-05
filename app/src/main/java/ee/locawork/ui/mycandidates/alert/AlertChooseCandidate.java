package ee.locawork.ui.mycandidates.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import ee.locawork.R;
import ee.locawork.model.JobApplicationDTO;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.PreferencesUtil;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AlertChooseCandidate {
    public static void init(final Context context, final JobApplicationDTO jobApplication) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_choose_candidate, (ViewGroup) null);
        final ControllerChooseCandidate controllerChooseCandidate = new ControllerChooseCandidate();
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(jobApplication.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_candidate)).setText(jobApplication.getAccountEmail());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(jobApplication.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.valueOf(jobApplication.getSalary()));
        ((TextView) dialogView.findViewById(R.id.applyer_contact)).setText(jobApplication.getContact());
        dialogView.findViewById(R.id.back).setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        dialogView.findViewById(R.id.submit).setOnClickListener(v -> {
            controllerChooseCandidate.apply(context, jobApplication.getUserId(), PreferencesUtil.readInt(context, KEY_USER_ID, 0));
            alertDialog.dismiss();
        });
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
