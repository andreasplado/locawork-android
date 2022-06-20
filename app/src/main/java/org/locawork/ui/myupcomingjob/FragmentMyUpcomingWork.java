package org.locawork.ui.myupcomingjob;

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
import org.locawork.R;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.util.AnimationUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.GoogleUserData;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentMyUpcomingWork extends Fragment {
    private ControllerUpcomingWork controllerUpcomingWork = new ControllerUpcomingWork();
    private boolean isEventBusRegistred = false;
    private LinearLayout noCandidatesView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;
    private RelativeLayout loadingView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userEmail = GoogleUserData.getUserEmail(getActivity());
        this.controllerUpcomingWork.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_upcoming_work, container, false);
        this.recyclerView = root.findViewById(R.id.applied_jobs_view);
        this.noCandidatesView = root.findViewById(R.id.no_candidates_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.retry = root.findViewById(R.id.retry);
        setOnClickListeners();
        return root;
    }

    private void setOnClickListeners() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentMyUpcomingWork.this);
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
        AdapterMyUpcomingWork adapterMyUpcomingWork = new AdapterMyUpcomingWork(addedJobsNetSuccess.getJobs(), getActivity(), getContext());
        this.serverErrorView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.GONE);
        //Collections.sort(jobs, comparator);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(adapterMyUpcomingWork);
        if (adapterMyUpcomingWork.getItemCount() == 0) {
            this.noCandidatesView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }
}
