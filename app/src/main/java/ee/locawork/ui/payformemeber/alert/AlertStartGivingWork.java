package ee.locawork.ui.payformemeber.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.stripe.android.model.CardParams;
import com.stripe.android.view.CardMultilineWidget;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.R;
import ee.locawork.ui.payformemeber.StartingGivingWorkButtonEvent;
import ee.locawork.util.ActivityUtils;
import ee.locawork.util.AppConstants;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.PreferencesUtil;

public class AlertStartGivingWork {



    public static void init(final Activity activity, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_pay_for_giving_work, null);
        ImageButton back = dialogView.findViewById(R.id.back);
        CheckBox saveForFuturePayments = dialogView.findViewById(R.id.save_for_future_payments);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.start_giving_work));
        ImageButton submit = dialogView.findViewById(R.id.submit);


        saveForFuturePayments.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.alert))
                .setMessage(context.getResources().getString(R.string.you_are_going_be_pay_2_euros_after_adding_new_work_every_time_will_you_accept_this))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    PreferencesUtil.save(context, PreferencesUtil.KEY_ACCEPT_PAYMENT, true);
                })
                .setNegativeButton(android.R.string.no, ((dialog, which) -> {
                    PreferencesUtil.save(context, PreferencesUtil.KEY_ACCEPT_PAYMENT, false);
                }))

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());

        submit.setOnClickListener((View view) -> {

            NavigationView navigationView = activity.findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView textView = headerView.findViewById(R.id.nav_role);

            // Get the card details from the card widget
            CardMultilineWidget cardMultilineWidget = dialogView.findViewById(R.id.card_multiline_widget);
            CardParams card = cardMultilineWidget.getCardParams();
            boolean acceptPayment = PreferencesUtil.readBoolean(context, PreferencesUtil.KEY_ACCEPT_PAYMENT, false);
            if(saveForFuturePayments.isChecked()
            && acceptPayment){
                PreferencesUtil.save(context, PreferencesUtil.KEY_ACCEPT_PAYMENT, false);
                if(cardMultilineWidget.validateAllFields()){
                    Gson gson = new Gson();
                    String json = gson.toJson(card);
                    PreferencesUtil.save(context, PreferencesUtil.KEY_CARD_PARAMS, json);


                    textView.setText(context.getResources().getString(R.string.offer_work));

                    PreferencesUtil.readBoolean(context, AppConstants.ROLE_JOB_SEEKER, false);

                    ActivityUtils.restartActivity(activity);


                    EventBus.getDefault().post(new StartingGivingWorkButtonEvent());

                }else{
                    textView.setText(context.getResources().getString(R.string.find_work));
                    Toast.makeText(context, context.getResources().getString(R.string.please_enter_correct_credit_card_details), Toast.LENGTH_LONG).show();
                }
                alertDialog.cancel();
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.please_make_sure_to_save_details_to_add_work), Toast.LENGTH_LONG).show();
            }
        });
        back.setOnClickListener(v -> alertDialog.cancel());
        alertDialog.setView(dialogView);
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
