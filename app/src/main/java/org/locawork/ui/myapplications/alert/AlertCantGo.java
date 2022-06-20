package org.locawork.ui.myapplications.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import org.locawork.R;
import org.locawork.model.MyApplicationDTO;
import org.locawork.util.DialogUtils;

public class AlertCantGo {

    public static void init(final Context context, final MyApplicationDTO job) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_cant_go, (ViewGroup) null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        ((ImageButton) dialogView.findViewById(R.id.back)).setOnClickListener((View.OnClickListener) view -> alertDialog.dismiss());
        ((ImageButton) dialogView.findViewById(R.id.submit)).setOnClickListener((View.OnClickListener) v -> AlertCantGo.init(context, job));
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
