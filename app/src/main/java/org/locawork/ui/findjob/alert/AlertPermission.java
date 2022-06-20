package org.locawork.ui.findjob.alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertPermission {
    public static void init(String message, DialogInterface.OnClickListener okListener, Context context) {
        AlertDialog al = new AlertDialog.Builder(context).setMessage(message).setPositiveButton("OK", okListener).create();
        al.setCancelable(false);
        al.show();
    }
}
