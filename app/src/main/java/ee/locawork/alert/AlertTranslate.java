package ee.locawork.ui.findwork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import ee.locawork.R;
import ee.locawork.util.DialogUtils;

public class AlertTranslate {

    public static void init(final Activity activity, final Context context, String title, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.alert_privacy_policy, (ViewGroup) null);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.locawork_web));
        final WebView content = dialogView.findViewById(R.id.content);
        content.getSettings().setJavaScriptEnabled(true);
        content.loadUrl("https://translate.google.com/?sl=en&tl=et&text=" + title + "%0A%0A" + description +"%0A%0A&op=translate");
        ImageButton back = dialogView.findViewById(R.id.back);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.show();
        submit.setOnClickListener(v -> {
            alertDialog.cancel();
            return;
        });
        back.setOnClickListener(view -> alertDialog.cancel());
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
