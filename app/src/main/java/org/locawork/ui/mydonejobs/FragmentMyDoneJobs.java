package org.locawork.ui.mydonejobs;

import android.content.IntentFilter;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.myupcomingjob.EventUpcomingWorkNetFailure;
import org.locawork.ui.myupcomingjob.EventUpcomingWorkNetSuccess;
import org.locawork.util.AnimationUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.GoogleUserData;
import org.locawork.util.GpsCurrencyUtil;
import org.locawork.util.PreferencesUtil;

import java.io.PrintStream;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentMyDoneJobs extends Fragment {
    private static final Map<String, Locale> COUNTRY_TO_LOCALE_MAP = new HashMap();
    public static SortedMap<Currency, Locale> currencyLocaleMap;
    private ControllerMyDoneJobs controllerMyDoneJobs = new ControllerMyDoneJobs();
    String currencySymbol = "";
    Geocoder geocoder;
    private boolean isEventBusRegistred = false;
    private LinearLayout noAddedJobsView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;
    private RelativeLayout loadingView;

    private TextView tvCurrency;
    private TextView tvSum;

    static {
        for (Locale l : Locale.getAvailableLocales()) {
            COUNTRY_TO_LOCALE_MAP.put(l.getCountry(), l);
        }
    }

    public static Locale getLocaleFromCountry(String country) {
        return COUNTRY_TO_LOCALE_MAP.get(country);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userEmail = GoogleUserData.getUserEmail(getActivity());
        this.controllerMyDoneJobs.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
    }

    public String getCurrencyCode(String countryCode) {
        String s = "";
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                if (locale.getCountry().equals(countryCode)) {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                    s = getCurrencySymbol(currency + "");
                }
            } catch (Exception e) {
            }
        }
        return s;
    }

    public String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        PrintStream printStream = System.out;
        printStream.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
        return currency.getSymbol(currencyLocaleMap.get(currency));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_done_work, container, false);
        this.tvSum = root.findViewById(R.id.sum);
        this.recyclerView = root.findViewById(R.id.done_work_view);
        this.noAddedJobsView = root.findViewById(R.id.no_candidates_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.retry = root.findViewById(R.id.retry);
        this.tvCurrency = root.findViewById(R.id.currency);
        setCurrency();
        setOnClickListeners();
        return root;
    }

    private void setCurrency() {
        GpsCurrencyUtil gpsTracker = new GpsCurrencyUtil(getContext());
        this.geocoder = new Geocoder(getContext(), getLocaleFromCountry(""));
        double lat = gpsTracker.getLatitude();
        double lng = gpsTracker.getLongitude();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                currencyLocaleMap.put(Currency.getInstance(locale), locale);
            } catch (Exception e) {
            }
        }
        try {
            this.currencySymbol = getCurrencyCode(this.geocoder.getFromLocation(lat, lng, 2).get(0).getCountryCode());
        } catch (Exception e2) {
            this.tvCurrency.setText("No currency defined");
        }
    }

    private void setOnClickListeners() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentMyDoneJobs.this);
        });
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void fragmentAppliedJobsServerError(EventUpcomingWorkNetFailure eventAddedJobsNetFailure) {
        this.serverErrorView.setVisibility(View.VISIBLE);
    }

    public void onStart() {
        super.onStart();
        registerRecievers();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void registerRecievers() {
        this.isEventBusRegistred = true;
        new IntentFilter("android.location.PROVIDERS_CHANGED").addAction("android.intent.action.PROVIDER_CHANGED");
    }

    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void fillRecycleView(EventUpcomingWorkNetSuccess addedJobsNetSuccess) {
        List<JobDTO> jobs = addedJobsNetSuccess.getJobs();
        MyDoneJobsAdapter appliedJobAdapter = new MyDoneJobsAdapter(addedJobsNetSuccess.getJobs(), getActivity(), getContext());
        this.serverErrorView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.GONE);
        //Collections.sort(jobs, comparator);
        this.recyclerView.setHasFixedSize(true);
        double salarySum = 0.0d;
        for (int i = 0; i < jobs.size(); i++) {
            salarySum += jobs.get(i).getSalary();
        }
        TextView textView = this.tvSum;
        textView.setText(getString(R.string.earned) + salarySum);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(appliedJobAdapter);
        if (appliedJobAdapter.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }
}
