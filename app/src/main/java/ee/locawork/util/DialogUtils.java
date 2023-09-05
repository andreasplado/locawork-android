package ee.locawork.util;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AlertDialog;

public class DialogUtils {
    public static void setDialogOnTopOfScreen(AlertDialog dlg) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= -3;
        window.setAttributes(wlp);
    }

    public static void setDialogOnTopOfScreen(android.app.AlertDialog dlg) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= -3;
        window.setAttributes(wlp);
    }
}
