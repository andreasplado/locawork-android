package org.locawork.payment;

import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;

import org.jetbrains.annotations.NotNull;
import org.locawork.model.payment.Pay;

public class EmpheralKeyProvider implements EphemeralKeyProvider {
    private final ControllerEmpheralKey controllerEmpheralKey = new ControllerEmpheralKey();

    @Override
    public void createEphemeralKey(@NotNull String s, @NotNull EphemeralKeyUpdateListener ephemeralKeyUpdateListener) {
        Pay pay = new Pay();
        pay.setApiVersion(s);
        controllerEmpheralKey.post(pay);
    }
}
