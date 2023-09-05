package ee.locawork.ui.payformemeber.alert;


import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

import ee.locawork.R;
import ee.locawork.model.PayingToken;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.PreferencesUtil;

public class AlertPayForRemovingAdds {



    public static void init(final Activity activity, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity, R.style.FullscreenDialog).create();
        View dialogView = activity.getLayoutInflater().inflate(R.layout.alert_remove_adds, null);
        ImageButton back = dialogView.findViewById(R.id.back);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.remove_adds));
        ImageButton submit = dialogView.findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        submit.setOnClickListener((View view) -> {
            // Get the card details from the card widget
            CardMultilineWidget cardInputWidget = dialogView.findViewById(R.id.card_multiline_widget);
            CardParams card = cardInputWidget.getCardParams();
            if (card != null) {
                // Create a Stripe token from the card details
                Stripe stripe = new Stripe(context, PaymentConfiguration.getInstance(context).getPublishableKey());
                stripe.createCardToken(card, new ApiResultCallback<Token>() {
                    @Override
                    public void onSuccess(@NonNull Token result) {
                        String tokenID = result.getId();
                        // Send the token identifier to the server...
                        Toast.makeText(context, tokenID, Toast.LENGTH_LONG).show();


                        ControllerPayForRemovingAdds controllerPayForRemovingAdds = new ControllerPayForRemovingAdds();
                        PayingToken payingToken = new PayingToken();
                        payingToken.setToken(tokenID);
                        payingToken.setUserId(PreferencesUtil.readInt(context, KEY_USER_ID, 0));
                        controllerPayForRemovingAdds.payForAdds(context, payingToken);
                                alertDialog.cancel();
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();


                    }
                });
            }
        });
        back.setOnClickListener(v -> alertDialog.cancel());
        alertDialog.setView(dialogView);
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
