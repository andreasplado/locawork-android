package ee.locawork.ui.myaddedwork;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ee.locawork.ActivityEndWork;
import ee.locawork.EventEndWork;
import ee.locawork.EventEndWorkFailure;
import ee.locawork.EventFailedToGetUserData;
import ee.locawork.EventGetUserData;
import ee.locawork.ActivityMain;
import ee.locawork.R;
import ee.locawork.broadcastreciever.NetworkReciever;
import ee.locawork.event.EventNetOff;
import ee.locawork.model.Job;
import ee.locawork.model.User;
import ee.locawork.ui.addworks.EventAddJobNetSuccess;
import ee.locawork.util.AnimationUtil;
import ee.locawork.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;

public class FragmentAddedJobs extends Fragment {

    private AdapterAddedJobs adapterAddedJobs;
    private ControllerAddedJobs controllerAddedJobs = new ControllerAddedJobs();
    private boolean isEventBusRegistred = false;
    private List<Job> jobs = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RelativeLayout loadingView;
    private NetworkReciever networkReciever;
    private LinearLayout noAddedJobsView;
    private RecyclerView recyclerView;
    private ImageButton retry;
    private LinearLayout serverErrorView;
    private RadioButton doneWork, unDoneWork, allWork;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getActivity() != null;
        assert ((ActivityMain) getActivity()).getSupportActionBar() != null;
        ((ActivityMain) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.my_added_jobs));
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_added_jobs, container, false);
        this.recyclerView = root.findViewById(R.id.added_jobs_view);
        this.noAddedJobsView = root.findViewById(R.id.no_added_jobs_view);
        this.serverErrorView = root.findViewById(R.id.server_error_view);
        this.loadingView = root.findViewById(R.id.loading_view);
        this.allWork = root.findViewById(R.id.all_work);
        this.unDoneWork = root.findViewById(R.id.undone_work);
        this.doneWork = root.findViewById(R.id.done_work);
        ImageButton imageButton = root.findViewById(R.id.retry);
        this.retry = imageButton;
        this.controllerAddedJobs.getUndonePostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        imageButton.setOnClickListener(view -> {
            AnimationUtil.animateBubble(view);
            this.controllerAddedJobs.getUndonePostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        this.doneWork.setOnClickListener(view -> {
            this.controllerAddedJobs.getDonePostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        this.unDoneWork.setOnClickListener(view -> {
            this.controllerAddedJobs.getUndonePostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });
        this.allWork.setOnClickListener(view -> {
            this.controllerAddedJobs.getAllPostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        });

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.loadingView.setVisibility(View.GONE);
    }

    @Subscribe
    public void networkOff(EventNetOff eventNetError) {
        ActivityMain.snack(null, getResources().getString(R.string.network_connection_failed), getActivity(), getView());
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
    public void getApplyerData(EventGetUserData eventGetUserData) {
        User selectedApplyer = eventGetUserData.getResponse().body();
        View alertView = eventGetUserData.getView();
        if(selectedApplyer != null){
            ((LinearLayout)alertView.findViewById(R.id.candidates_view)).setVisibility(View.VISIBLE);
            ((TextView)alertView.findViewById(R.id.no_candidates_view)).setVisibility(View.GONE);
            ((TextView)alertView.findViewById(R.id.candidate_email)).setText(selectedApplyer.getEmail());
            ((TextView)alertView.findViewById(R.id.job_performer_phone)).setText(selectedApplyer.getContact());
        }else {
            ((LinearLayout)alertView.findViewById(R.id.candidates_view)).setVisibility(View.GONE);
            ((TextView)alertView.findViewById(R.id.no_candidates_view)).setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void endWork(EventEndWork eventEndWork){
        if(eventEndWork.getResponse().code() == 200){
            this.controllerAddedJobs.getUndonePostedJobsData(getContext(), PreferencesUtil.readInt(getContext(), KEY_USER_ID, 0));
        }
    }

    @Subscribe
    public void endWork(EventEndWorkFailure eventEndWorkFailure){
        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
    }
    @Subscribe
    public void getAplyerData(EventFailedToGetUserData eventGetUserData) {
        Throwable mainData = eventGetUserData.getT();
        Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
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
