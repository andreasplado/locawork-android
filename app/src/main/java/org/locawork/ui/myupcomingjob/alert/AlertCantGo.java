package org.locawork.ui.myupcomingjob.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import org.locawork.R;
import org.locawork.util.DialogUtils;
import org.locawork.util.PreferencesUtil;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class AlertCantGo {
    public static ControllerCantGo controllerCantGo = new ControllerCantGo();

    public static void init(final Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_cant_go, (ViewGroup) null);
        dialogBuilder.setView(dialogView);
        EditText editText = dialogView.findViewById(R.id.reason_to_quit);
        final AlertDialog alertDialog = dialogBuilder.create();
        dialogView.findViewById(R.id.back).setOnClickListener(view -> alertDialog.dismiss());
        dialogView.findViewById(R.id.submit).setOnClickListener(v -> AlertCantGo.controllerCantGo.postData(context, PreferencesUtil.readInt(context, KEY_USER_ID, 0)));
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}