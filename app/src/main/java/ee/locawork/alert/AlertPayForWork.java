package ee.locawork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import ee.locawork.R;
import ee.locawork.util.DialogUtils;

public class AlertPayForWork {

    public static void init(final Activity activity, final Context context) {
        String location;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_success, (ViewGroup) null);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        TextView title = dialogView.findViewById(R.id.title);
        ImageButton back = dialogView.findViewById(R.id.back);
        TextView successTitle = dialogView.findViewById(R.id.success_title);
        TextView successText = dialogView.findViewById(R.id.success_text);

        successTitle.setText(context.getResources().getString(R.string.congratulations_you_just_added_new_work));
        successText.setText(context.getResources().getString(R.string.you_just_posted_a_work_please_check_in_your_added_work_to_see_or_edit_your_posted_work));
        title.setText(context.getResources().getString(R.string.succees));
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
