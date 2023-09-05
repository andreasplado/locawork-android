package ee.locawork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ee.locawork.R;
import ee.locawork.util.DialogUtils;

public class AlertPayForWorkFailure {

    public static void init(final Activity activity, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_error, (ViewGroup) null);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        LinearLayout alertbarLayout = dialogView.findViewById(R.id.app_bar_layout);
        TextView title = dialogView.findViewById(R.id.title);
        ImageButton back = dialogView.findViewById(R.id.back);
        TextView successTitle = dialogView.findViewById(R.id.error_title);
        TextView successText = dialogView.findViewById(R.id.error_text);

        alertbarLayout.setBackgroundColor(Color.RED);
        successTitle.setText(context.getResources().getString(R.string.oops));
        successText.setText(context.getResources().getString(R.string.your_job_psting_failed_please_check_your_credit_card_details_or_contact_with_support));
        title.setText(context.getResources().getString(R.string.error));
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
