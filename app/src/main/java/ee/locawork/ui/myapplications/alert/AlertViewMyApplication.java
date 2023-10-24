package ee.locawork.ui.myapplications.alert;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ee.locawork.R;
import ee.locawork.alert.AlertTranslate;
import ee.locawork.model.MyApplicationDTO;
import ee.locawork.ui.myapplications.ControllerCancelApplication;
import ee.locawork.ui.mycandidates.alert.ControllerChooseCandidate;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;

import androidx.appcompat.app.AlertDialog;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AlertViewMyApplication {

    public static void init(final Activity activity, final Context context, final MyApplicationDTO myApplicationDTO) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.FullscreenDialog);
        final ControllerChooseCandidate controllerChooseCandidate = new ControllerChooseCandidate();
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_view_my_application, (ViewGroup) null);

        dialogBuilder.setView(dialogView);
        ((TextView) dialogView.findViewById(R.id.job_title)).setText(myApplicationDTO.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_description)).setText(myApplicationDTO.getDescription());
        ((TextView) dialogView.findViewById(R.id.job_salary)).setText(String.valueOf(myApplicationDTO.getSalary()));
        ((TextView) dialogView.findViewById(R.id.title)).setText(myApplicationDTO.getTitle());
        ((TextView) dialogView.findViewById(R.id.job_location)).setText(LocationUtil.fetchLocationData(activity, new LatLng(myApplicationDTO.getLatitude().doubleValue(), myApplicationDTO.getLongitude().doubleValue())));

        ((ImageButton) dialogView.findViewById(R.id.translate)).setOnClickListener(v -> {
            String jobTitle = ((TextView) dialogView.findViewById(R.id.job_title)).getText().toString();
            String jobDescription = ((TextView) dialogView.findViewById(R.id.job_description)).getText().toString();
            ArrayList data = new ArrayList();
            data.add(jobTitle);
            data.add(jobDescription);
            AlertTranslate.init(activity, context, data);
        });
        final AlertDialog alertDialog = dialogBuilder.create();

        ((ImageButton)dialogView.findViewById(R.id.dismiss)).setOnClickListener(v -> {
            new ControllerCancelApplication().cancelApplication(context, myApplicationDTO.getId(), alertDialog);
        });
        dialogView.findViewById(R.id.back).setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
