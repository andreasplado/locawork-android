package org.locawork.ui.myapplications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.locawork.R;
import org.locawork.model.MyApplicationDTO;
import org.locawork.util.AnimationUtil;
import org.locawork.util.FragmentUtils;
import org.locawork.util.PreferencesUtil;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static org.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentMyApplications extends Fragment {
    private AdapterMyApplications adapterMyApplications;
    private ControllerMyApplications controllerMyApplications = new ControllerMyApplications();
    private List<MyApplicationDTO> myApplications;
    private LinearLayout noCandidatesView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;
    private RelativeLayout loadingView;

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
        View root = inflater.inflate(R.layout.fragment_my_applications, container, false);
        this.recyclerView = root.findViewById(R.id.candidate_applications);
        this.noCandidatesView = root.findViewById(R.id.no_candidates_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.retry = root.findViewById(R.id.retry);
        setOnClickListeners();
        this.controllerMyApplications.getData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        return root;
    }

    private void setOnClickListeners() {
        this.retry.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            FragmentUtils.restartFragment(FragmentMyApplications.this);
        });
    }

    public void onStop() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe
    public void error(EventMyApplicationsNetFailure eventMyApplicationsNetFailure) {
        this.serverErrorView.setVisibility(View.VISIBLE);
    }

    @Subscribe
    public void cancelApplicationSuccess(EventMyApplicationCancelSuccess eventMyApplicationCancelSuccess) {
        for (int i = 0; i < this.myApplications.size(); i++) {
            if (this.myApplications.get(i).getId().equals(eventMyApplicationCancelSuccess.getMyApplicationDTO().getId())) {
                this.myApplications.remove(i);
            }
        }
        this.adapterMyApplications = new AdapterMyApplications(this.myApplications, getActivity(), getContext());

        adapterMyApplications.notifyDataSetChanged();
        this.recyclerView.setAdapter(this.adapterMyApplications);

        if (this.adapterMyApplications.getItemCount() == 0) {
            this.noCandidatesView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void cancelApplicationFailure(EventMyApplicationsCancelFailure eventMyApplicationsCancelFailure) {
        Toast.makeText(getContext(), getResources().getString(R.string.my_application_cancel_failue), Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void fillRecycleView(EventMyApplicationsNetSuccess addedJobsNetSuccess) {
        this.serverErrorView.setVisibility(View.GONE);
        this.myApplications = addedJobsNetSuccess.getMyApplications().getMyApplications();

        this.adapterMyApplications = new AdapterMyApplications(this.myApplications, getActivity(), getContext());
        this.serverErrorView.setVisibility(View.GONE);
        this.loadingView.setVisibility(View.GONE);
        //Collections.sort(myApplicaitons, comparator);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(this.adapterMyApplications);
        int itemCount = this.adapterMyApplications.getItemCount();
        if (this.adapterMyApplications.getItemCount() == 0) {
            this.noCandidatesView.setVisibility(View.VISIBLE);
            this.recyclerView.setVisibility(View.GONE);
        }
    }
}