package org.locawork.ui.addedjob;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.locawork.MainActivity;
import org.locawork.R;
import org.locawork.broadcastreciever.GpsReciver;
import org.locawork.broadcastreciever.NetworkReciever;
import org.locawork.event.EventNetOff;
import org.locawork.event.EventNetOn;
import org.locawork.model.Job;
import org.locawork.ui.myaddedjob.EventAddedJobsNetFailure;
import org.locawork.ui.myaddedjob.EventMyAddedWorkDeleteSuccess;
import org.locawork.util.PreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentAddedJobs extends Fragment {

    private RecyclerView recyclerView;
    private ControllerAddedJobs controllerAddedJobs = new ControllerAddedJobs();
    private RecyclerView.LayoutManager layoutManager;
    private NetworkReciever networkReciever;
    private GpsReciver gpsReciver;
    private boolean isEventBusRegistred = false;
    private LinearLayout noAddedJobsView, serverErrorView;
    private View loadingView;
    private AddedJobsAdapter addedJobsAdapter;
    private List<Job> jobs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controllerAddedJobs.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.my_added_jobs));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_added_jobs, container, false);
        recyclerView = root.findViewById(R.id.added_jobs_view);
        noAddedJobsView = root.findViewById(R.id.no_added_jobs_view);
        serverErrorView = root.findViewById(R.id.server_error_view);
        loadingView = root.findViewById(R.id.loading_view);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Subscribe
    public void networkConnected(EventNetOn networkSuccessEvent) {
        MainActivity.hideSnackBar();
    }

    @Subscribe
    public void networkOff(EventNetOff eventNetError) {
        MainActivity.snack(null, getResources().getString(R.string.network_connection_failed), getActivity(), getView());
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
        getActivity().unregisterReceiver(networkReciever);
        getActivity().unregisterReceiver(gpsReciver);
        super.onStop();
    }

    @Subscribe
    public void fillRecycleView(EventAddedJobsNetSuccess addedJobsNetSuccess){
        serverErrorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        jobs = addedJobsNetSuccess.getJobs();

        Comparator<Job> comparator = (left, right) -> {
            return right.getId() - left.getId();
        };

        Collections.sort(jobs, comparator);

        addedJobsAdapter = new AddedJobsAdapter(addedJobsNetSuccess.getJobs());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(addedJobsAdapter);
        if(addedJobsAdapter.getItemCount() == 0){
            noAddedJobsView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    @Subscribe
    public void addedJobsNetFailure(EventAddedJobsNetFailure eventAddedJobsNetFailure){
        serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void myEventDeleteSuccess(EventMyAddedWorkDeleteSuccess eventMyAddedWorkDeleteSuccess){
        /*for (int i = 0; i < eventMyAddedWorkDeleteSuccess.getJob().getJobs().size(); i++) {
            if (this.jobs.get(i).getId() == eventMyAddedWorkDeleteSuccess.getJob().getJobs().get(i).getId()) {
                this.jobs.remove(i);
            }
        }*/
        addedJobsAdapter.notifyDataSetChanged();
        this.recyclerView.setAdapter(addedJobsAdapter);
        if (addedJobsAdapter.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }
}
