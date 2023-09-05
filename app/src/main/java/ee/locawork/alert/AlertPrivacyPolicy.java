package ee.locawork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

import ee.locawork.R;
import ee.locawork.model.dto.AddingJobDTO;
import ee.locawork.ui.payformemeber.alert.ControllerPayForStartingGivingWork;
import ee.locawork.util.DialogUtils;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;

public class AlertPrivacyPolicy {

    public static void init(final Activity activity, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.alert_privacy_policy, (ViewGroup) null);
        ImageButton submit = dialogView.findViewById(R.id.submit);
        ((TextView) dialogView.findViewById(R.id.title)).setText(context.getResources().getString(R.string.privacy_policy));
        final WebView content = dialogView.findViewById(R.id.content);
        content.getSettings().setJavaScriptEnabled(true);
        content.loadUrl("https://locawork-web-api.herokuapp.com/docs/privacy-policy");
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
