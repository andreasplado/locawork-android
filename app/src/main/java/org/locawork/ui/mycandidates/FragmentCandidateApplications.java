package org.locawork.ui.mycandidates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.locawork.R;
import org.locawork.model.JobApplications;
import org.locawork.ui.mycandidates.alert.EventChooseCandidateApplicationsNetSuccess;
import org.locawork.util.AnimationUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.PreferencesUtil;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentCandidateApplications extends Fragment {
    private ControllerCandiates controllerCandiates = new ControllerCandiates();
    private JobApplications myCandidates;
    private AdapterCandidates myCandidatesAdapter;
    private LinearLayout noCandidatesView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;
    private RelativeLayout loadingView;
    private NavigationView navigationView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_candidate_applications, container, false);
        this.recyclerView = root.findViewById(R.id.candidate_applications);
        this.noCandidatesView = root.findViewById(R.id.no_candidates_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.retry = root.findViewById(R.id.retry);
        this.navigationView = root.findViewById(R.id.nav_view);
        this.controllerCandiates.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        setOnClickListeners();
        return root;
    }

    private void setOnClickListeners() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentCandidateApplications.this);
        });
    }

    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void error(EventCandidatesNetFailure eventCandidatesNetFailure) {
        this.serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void fillRecycleView(EventCandidateApplicationsNetSuccess addedJobsNetSuccess) {
        this.myCandidates = addedJobsNetSuccess.getJobApplications();
        this.myCandidatesAdapter = new AdapterCandidates(addedJobsNetSuccess.getJobApplications().getJobApplications(), getContext());
        this.serverErrorView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.GONE);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.myCandidatesAdapter);
        int itemCount = this.myCandidatesAdapter.getItemCount();
        AdapterWorkSpinner adapterWorkSpinner = new AdapterWorkSpinner(addedJobsNetSuccess.getJobApplications().getJobApplications(), getContext());
        if(adapterWorkSpinner.getCount() > 0) {
            //this.candidateSpinner.setAdapter(adapterWorkSpinner);
        }

        if (this.myCandidatesAdapter.getItemCount() == 0) {
            //filter.setVisibility(View.GONE);
            this.noCandidatesView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
        //filter.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void chooseCandidate(EventChooseCandidateApplicationsNetSuccess eventChooseCandidateApplicationsNetSuccess) {
        this.noCandidatesView.setVisibility(View.VISIBLE);
        this.recyclerView.setVisibility(View.GONE);
        MenuItem myCandidatesCounter = this.navigationView.getMenu().findItem(R.id.nav_my_candidates);
    }

    @Subscribe
    public void myAddedWorkDeleteSuccess(EventCanditatesDeleteSuccess eventMyAddedWorkDeleteSuccess) {
        for (int i = 0; i < this.myCandidates.getJobApplications().size(); i++) {
            if (this.myCandidates.getJobApplications().get(i).getId() == eventMyAddedWorkDeleteSuccess.getId()) {
                this.myCandidates.getJobApplications().remove(i);
            }
        }
        this.myCandidatesAdapter = new AdapterCandidates(this.myCandidates.getJobApplications(), getContext());
        this.myCandidatesAdapter.notifyDataSetChanged();
        this.recyclerView.setAdapter(this.myCandidatesAdapter);

        //AdapterWorkSpinner adapterWorkSpinner = new AdapterWorkSpinner(this.myCandidates.getJobApplications(), getContext());
        //this.candidateSpinner.setAdapter(adapterWorkSpinner);

        if (this.myCandidatesAdapter.getItemCount() == 0) {
            this.noCandidatesView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void myAddedWorkDeleteFailure(EventCandidatesDeleteFailure eventMyAddedWorkDeleteFailure) {
    }
}
