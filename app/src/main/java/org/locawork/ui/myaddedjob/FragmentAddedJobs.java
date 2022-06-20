package org.locawork.ui.myaddedjob;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.MainActivity;
import org.locawork.R;
import org.locawork.broadcastreciever.NetworkReciever;
import org.locawork.event.EventNetOff;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.addjobs.EventAddJobNetSuccess;
import org.locawork.util.AnimationUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentAddedJobs extends Fragment {

    private AdapterAddedJobs adapterAddedJobs;
    private ControllerAddedJobs controllerAddedJobs = new ControllerAddedJobs();
    private boolean isEventBusRegistred = false;
    private List<Job> jobs;
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout loadingView;
    private NetworkReciever networkReciever;
    private LinearLayout noAddedJobsView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getActivity() != null;
        assert ((MainActivity) getActivity()).getSupportActionBar() != null;
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.my_added_jobs));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_added_jobs, container, false);
        this.recyclerView = root.findViewById(R.id.added_jobs_view);
        this.noAddedJobsView = root.findViewById(R.id.no_added_jobs_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        ImageButton imageButton = root.findViewById(R.id.retry);
        this.retry = imageButton;
        imageButton.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentAddedJobs.this);
        });
        this.controllerAddedJobs.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.GONE);
    }

    @Subscribe
    public void networkOff(EventNetOff eventNetError) {
        MainActivity.snack(null, getResources().getString(R.string.network_connection_failed), getActivity(), getView());
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
        new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.networkReciever = new NetworkReciever();
        new IntentFilter("android.location.PROVIDERS_CHANGED").addAction("android.intent.action.PROVIDER_CHANGED");
    }

    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void setEmptyView(EventEmptyList eventEmptyList) {
        if (eventEmptyList.getJobs().size() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void fillRecycleView(EventAddedJobsNetSuccess addedJobsNetSuccess) {
        this.jobs = addedJobsNetSuccess.getJobs();
        this.loadingView.setVisibility(View.GONE);
        this.serverErrorView.setVisibility(View.GONE);
        this.adapterAddedJobs = new AdapterAddedJobs(addedJobsNetSuccess.getJobs(), getContext(), getActivity());
        this.recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.layoutManager = linearLayoutManager;
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(this.adapterAddedJobs);
        if (this.adapterAddedJobs.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void myAddedWorkDeleteFailure(EventMyAddedWorkDeleteFailure eventMyAddedWorkDeleteFailure) {
    }

    @Subscribe
    public void myWorkAddingNetSuccess(EventAddJobNetSuccess eventAddJobNetSuccess) {
        this.jobs.add(eventAddJobNetSuccess.getJob());
        adapterAddedJobs = new AdapterAddedJobs(this.jobs, getContext(), getActivity());
        this.adapterAddedJobs.notifyDataSetChanged();
        this.recyclerView.setAdapter(this.adapterAddedJobs);
        if (this.adapterAddedJobs.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void myAddedWorkDeleteSuccess(EventMyAddedWorkDeleteSuccess eventMyAddedWorkDeleteSuccess) {
        int l = 0;
        for (int i = 0; i < jobs.size(); i++){
            if(this.jobs.get(i).getId() == eventMyAddedWorkDeleteSuccess.getJob().getId()){
                this.jobs.remove(i);
            }
        }
        adapterAddedJobs = new AdapterAddedJobs(this.jobs, getContext(), getActivity());
        adapterAddedJobs.notifyDataSetChanged();
        this.recyclerView.setAdapter(adapterAddedJobs);
        if (adapterAddedJobs.getItemCount() == 0) {
            this.noAddedJobsView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void netFailure(EventAddedJobsNetFailure eventAddedJobsNetFailure) {
        this.serverErrorView.setVisibility(View.VISIBLE);
    }
}