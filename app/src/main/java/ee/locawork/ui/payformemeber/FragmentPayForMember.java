package ee.locawork.ui.payformemeber;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.stripe.android.PaymentSession;
import com.stripe.android.view.AddPaymentMethodActivityStarter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import ee.locawork.R;
import ee.locawork.broadcastreciever.GpsReciver;
import ee.locawork.broadcastreciever.NetworkReciever;
import ee.locawork.ui.payformemeber.alert.AlertPayForRemovingAdds;
import ee.locawork.ui.payformemeber.alert.AlertStartGivingWork;
import ee.locawork.util.ActivityUtils;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PreferencesUtil;
import ee.locawork.util.StringUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;
import static ee.locawork.util.PreferencesUtil.KEY_CARD_PARAMS;
import static ee.locawork.util.PreferencesUtil.KEY_COMPANY_NAME;
import static ee.locawork.util.PreferencesUtil.KEY_COMPANY_REG_NUMBER;
import static ee.locawork.util.PreferencesUtil.KEY_IS_WITHOUT_ADDS;
import static ee.locawork.util.PreferencesUtil.KEY_ROLE;

import java.util.Objects;

public class FragmentPayForMember extends Fragment {

    private PaymentSession paymentSession;
    private ControllerIsMember controllerIsMember = new ControllerIsMember();
    private boolean isEventBusRegistred = false;
    private NetworkReciever networkReciever;
    private GpsReciver gpsReciver;
    private LinearLayout paymentLayout;
    private Button removeAdds;
    private Button startGivingWork;
    private Button cancelGivingWork;
    private LinearLayout removeAddsLayout;
    private TextView pucrchasedAdd;

    private Context context;

    private NavigationView navigationView;
    private View headerView;
    private TextView pleaseUseCompanyAccount;

    private TextView navRole;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pay_for_member, container, false);
        this.paymentLayout = root.findViewById(R.id.payment_layout);
        this.removeAdds = root.findViewById(R.id.remove_adds);
        this.startGivingWork = root.findViewById(R.id.start_giving_work);
        this.removeAddsLayout = root.findViewById(R.id.remove_adds_layout);
        this.pucrchasedAdd = root.findViewById(R.id.puchased_add_text);
        this.cancelGivingWork = root.findViewById(R.id.cancel_giving_work);
        this.pleaseUseCompanyAccount = root.findViewById(R.id.please_use_company_account);

        navigationView = requireActivity().findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navRole = headerView.findViewById(R.id.nav_role);

        setOnClickListeners();
        validate();
        return root;
    }

    private void validate(){
        if(PreferencesUtil.readString(getContext(), KEY_CARD_PARAMS, "").equals("")){
            String companyRegNumber = PreferencesUtil.readString(requireActivity(), KEY_COMPANY_REG_NUMBER, "");
            String companyName = StringUtil.convertStringToUTF8(PreferencesUtil.readString(requireActivity(), KEY_COMPANY_NAME, ""));
            if(companyRegNumber.equals("")){
                startGivingWork.setVisibility(View.GONE);
                pleaseUseCompanyAccount.setVisibility(View.VISIBLE);
            }else{
                startGivingWork.setText(getResources().getString(R.string.start_giving_work_as) + " " + companyName  );
                startGivingWork.setVisibility(View.VISIBLE);
                pleaseUseCompanyAccount.setVisibility(View.GONE);
            }
            cancelGivingWork.setVisibility(View.GONE);
            navRole.setText(getContext().getResources().getString(R.string.work_seeker));
        }else{
            startGivingWork.setVisibility(View.GONE);
            cancelGivingWork.setVisibility(View.VISIBLE);
            navRole.setText(getContext().getResources().getString(R.string.work_offer));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPurchasedContent();
        validate();
    }

    private void setPurchasedContent(){
        boolean isWithoutAdds = PreferencesUtil.readBoolean(getContext(), KEY_IS_WITHOUT_ADDS, false);
        if(isWithoutAdds){
            removeAddsLayout.setVisibility(View.GONE);
            pucrchasedAdd.setVisibility(View.VISIBLE);
        }else{
            removeAddsLayout.setVisibility(View.VISIBLE);
            pucrchasedAdd.setVisibility(View.GONE);
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
    public void onAddsPayed(PayForRemovingAdds payForRemovingAdds) {
        PreferencesUtil.save(getContext(), KEY_IS_WITHOUT_ADDS, true);
        setPurchasedContent();
    }

    @Subscribe
    public void onMemberFailure(PayForRemovingAddsFailure payForRemovingAddsFailure) {
        Throwable isMember = payForRemovingAddsFailure.getT();
        Toast.makeText(getContext(), isMember.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void setStartGivingWork(StartingGivingWorkButtonEvent startGivingWork) {
        validate();
    }



    private void setOnClickListeners() {
        this.removeAdds.setOnClickListener(view -> {
            AlertPayForRemovingAdds.init(getActivity(), getContext());
        });

        ImageButton addJob = getActivity().findViewById(R.id.add_job);
        this.startGivingWork.setOnClickListener(view -> {
            AlertStartGivingWork.init(getActivity(), getContext());
            PreferencesUtil.save(getContext(), KEY_ROLE, AppConstants.ROLE_JOB_OFFER);

        });
        this.cancelGivingWork.setOnClickListener(view -> {
            PreferencesUtil.save(getContext(), KEY_CARD_PARAMS, "");
            PreferencesUtil.save(getContext(), KEY_ROLE, AppConstants.ROLE_JOB_SEEKER);
            startGivingWork.setVisibility(View.VISIBLE);
            cancelGivingWork.setVisibility(View.GONE);
            addJob.setVisibility(View.GONE);
            ActivityUtils.restartActivity(getActivity());

        });
    }
}
