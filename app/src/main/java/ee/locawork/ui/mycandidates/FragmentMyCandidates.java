package ee.locawork.ui.mycandidates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ee.locawork.R;
import ee.locawork.alert.AlertCandidateChosen;
import ee.locawork.model.JobApplications;
import ee.locawork.ui.mycandidates.alert.EventChooseCandidateApplicationsNetSuccess;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.FragmentUtils;
import ee.locawork.util.PreferencesUtil;

import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentMyCandidates extends Fragment {
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
            FragmentUtils.restartFragment(FragmentMyCandidates.this);
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
        AlertCandidateChosen.init(getActivity(), getContext());
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
