package ee.locawork.ui.mydonework;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ee.locawork.R;
import ee.locawork.broadcastreciever.NetworkReciever;
import ee.locawork.event.EventNetOff;
import ee.locawork.event.EventNetOn;
import ee.locawork.model.dto.JobDTO;
import ee.locawork.ui.myupcomingwork.EventUpcomingWorkNetFailure;
import ee.locawork.ui.myupcomingwork.EventUpcomingWorkNetSuccess;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.FragmentUtils;
import ee.locawork.util.GoogleUserData;
import ee.locawork.util.GpsCurrencyUtil;
import ee.locawork.util.PreferencesUtil;

import java.io.PrintStream;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentMyDoneWork extends Fragment {
    private static final Map<String, Locale> COUNTRY_TO_LOCALE_MAP = new HashMap();
    public static SortedMap<Currency, Locale> currencyLocaleMap;
    private ControllerMyDoneJobs controllerMyDoneJobs = new ControllerMyDoneJobs();
    private String currencySymbol = "";
    private Geocoder geocoder;
    private boolean isEventBusRegistred = false;
    private LinearLayout noAddedJobsView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private RadioButton today, thisWeek, thisMonth;
    private LinearLayout serverErrorView;
    private RelativeLayout loadingView;
    private TextView tvCurrency;
    private TextView tvSum;
    private BroadcastReceiver networkReceiver;


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
        this.today = root.findViewById(R.id.today);
        this.thisWeek = root.findViewById(R.id.this_week);
        this.thisMonth = root.findViewById(R.id.this_month);
        this.networkReceiver = new NetworkReciever();
        today.setChecked(true);
        setCurrency();
        setOnClickListeners();
        return root;
    }

    static {
        for (Locale l : Locale.getAvailableLocales()) {
            COUNTRY_TO_LOCALE_MAP.put(l.getCountry(), l);
        }
    }

    public static Locale getLocaleFromCountry(String country) {
        return COUNTRY_TO_LOCALE_MAP.get(country);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userEmail = GoogleUserData.getUserEmail(getActivity());
        this.controllerMyDoneJobs.getTodaysData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
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
            this.controllerMyDoneJobs.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        this.today.setOnClickListener(v -> {
            this.controllerMyDoneJobs.getTodaysData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        this.thisWeek.setOnClickListener(v ->{
            this.controllerMyDoneJobs.getThisWeekData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));

        });
        this.thisMonth.setOnClickListener(v -> {
            this.controllerMyDoneJobs.getThisMonth(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void fragmentAppliedJobsServerError(EventUpcomingWorkNetFailure eventAddedJobsNetFailure) {
        this.serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void networkOn(EventNetOn eventNetOn){
        this.controllerMyDoneJobs.getTodaysData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        this.today.setChecked(true);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getActivity().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
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
        double hourspentOnWork = 0.0d;
        for (int i = 0; i < jobs.size(); i++) {
            long workStartTime = Long.parseLong(jobs.get(i).getWorkStartTime());
            long workEndTime = Long.parseLong(jobs.get(i).getWorkEndTime());
            long hoursSpendOnWork = workEndTime - workStartTime;
            long actualWorkingTime = TimeUnit.MILLISECONDS.toHours(hoursSpendOnWork * 60);
            hoursSpendOnWork += actualWorkingTime;
            salarySum += jobs.get(i).getSalary() * actualWorkingTime;
        }
        TextView textView = this.tvSum;
        textView.setText(getString(R.string.earned) + salarySum + "/" + hourspentOnWork + "" + getString(R.string.per_hour));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(appliedJobAdapter);
        if (appliedJobAdapter.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }else {
            this.noAddedJobsView.setVisibility(View.GONE);
            this.recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
