package org.locawork.util;

import android.app.Activity;
import android.content.Context;

import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;

import org.locawork.R;
import org.locawork.payment.EmpheralKeyProvider;

import androidx.annotation.NonNull;

public class PaymentUtil {

    private Stripe stripe;
    private Activity activity;
    private String publishableKey = "";

    private PaymentUtil(Activity activity) {
        this.activity = activity;
    }

    public void init(Context context) {
        publishableKey = activity.getResources().getString(R.string.stripe_publishable_key);
        PaymentConfiguration.init(
                context,
                publishableKey
        );
    }

    public void setCustomerSession(Context context) {
        CustomerSession.initCustomerSession(
                context,
                new EmpheralKeyProvider()
        );
    }

    public void makePayment(
            @NonNull String clientSecret,
            @NonNull String paymentMethodId,
            Context context
    ) {
        stripe = new Stripe(
                context,
                publishableKey);

        stripe.confirmPayment(activity,
                ConfirmPaymentIntentParams.createWithPaymentMethodId(
                        paymentMethodId,
                        clientSecret
                ));
    }
}
