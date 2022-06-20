package org.locawork.ui.payformemeber;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stripe.android.CustomerSession;
import com.stripe.android.PaymentSession;
import com.stripe.android.PaymentSessionConfig;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.view.AddPaymentMethodActivityStarter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.ActivityAddPaymentMethod;
import org.locawork.R;
import org.locawork.broadcastreciever.GpsReciver;
import org.locawork.broadcastreciever.NetworkReciever;
import org.locawork.util.AnimationUtil;
import org.locawork.util.AppConstants;
import org.locawork.util.FragmentUtils;
import org.locawork.util.PreferencesUtil;

import java.util.Arrays;
import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static org.locawork.util.PreferencesUtil.KEY_IS_PAYMENT_METHOD;

public class FragmentPayForMember extends Fragment {

    private PaymentSession paymentSession;
    private ControllerIsMember controllerIsMember = new ControllerIsMember();
    private LinearLayout serverErrorView;
    private RelativeLayout noAddedJobsView, loadingView;
    private ImageButton retry;
    private boolean isEventBusRegistred = false;
    private NetworkReciever networkReciever;
    private GpsReciver gpsReciver;
    private LinearLayout paymentLayout;
    private Button addPaymentMehtod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controllerIsMember.getData(getContext(), PreferencesUtil.readInt(getContext(), PreferencesUtil.KEY_USER_ID, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pay_for_member, container, false);
        this.noAddedJobsView = root.findViewById(R.id.no_candidates_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.retry = root.findViewById(R.id.retry);
        this.paymentLayout = root.findViewById(R.id.payment_layout);
        this.addPaymentMehtod = root.findViewById(R.id.add_payment_method);

        setOnClickListeners();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.VISIBLE);
        if(PreferencesUtil.readBoolean(getContext(), KEY_IS_PAYMENT_METHOD, false)){
            paymentLayout.setVisibility(View.VISIBLE);
            addPaymentMehtod.setVisibility(View.GONE);
        }else{
            addPaymentMehtod.setVisibility(View.VISIBLE);
            paymentLayout.setVisibility(View.GONE);
        }
    }

    private void launchPaymentMethodsActivity() {
        new AddPaymentMethodActivityStarter(this).startForResult(
                new AddPaymentMethodActivityStarter.Args.Builder()
                        .setShouldAttachToCustomer(true)
                        .build()
        );
    }
    @Override
    public void onStart() {
        super.onStart();
        registerRecievers();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void registerRecievers() {
        isEventBusRegistred = true;
        IntentFilter networkFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        networkReciever = new NetworkReciever();
        gpsReciver = new GpsReciver();
        IntentFilter gpsFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        gpsFilter.addAction(Intent.ACTION_PROVIDER_CHANGED);

        getActivity().registerReceiver(networkReciever, networkFilter);
        getActivity().registerReceiver(gpsReciver, gpsFilter);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void onMemberSuccess(IsMemberSuccess isMemberSuccess) {
        String memberRole = isMemberSuccess.getBody();
        this.loadingView.setVisibility(View.GONE);
        if(memberRole.equals(AppConstants.ROLE_JOB_OFFER)){

        }
    }

    @Subscribe
    public void onMemberFailure(IsMemberFailure isMemberFailure) {
        Throwable isMember = isMemberFailure.getT();
        this.serverErrorView.setVisibility(View.VISIBLE);
        this.loadingView.setVisibility(View.GONE);
        Toast.makeText(getContext(), isMember.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void setOnClickListeners() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentPayForMember.this);
        });
        this.addPaymentMehtod.setOnClickListener(view -> {
        });
    }
}
