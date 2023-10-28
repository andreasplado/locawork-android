package ee.locawork.ui.myupcomingwork.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import ee.locawork.R;
import ee.locawork.util.DialogUtils;

public class AlertCantGo {
    public static ControllerCantGo controllerCantGo = new ControllerCantGo();

    public static void init(final Context context, Integer jobId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_cant_go, (ViewGroup) null);
        dialogBuilder.setView(dialogView);
        EditText reasonEt = dialogView.findViewById(R.id.reason_to_quit);
        final AlertDialog alertDialog = dialogBuilder.create();
        dialogView.findViewById(R.id.back).setOnClickListener(view -> alertDialog.dismiss());
        dialogView.findViewById(R.id.submit).setOnClickListener(v -> {
            alertDialog.dismiss();
            AlertCantGo.controllerCantGo.putData(context, jobId, reasonEt.getText().toString());
        });
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
