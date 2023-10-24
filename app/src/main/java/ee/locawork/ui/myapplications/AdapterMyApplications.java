package ee.locawork.ui.myapplications;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import ee.locawork.R;
import ee.locawork.model.MyApplicationDTO;
import ee.locawork.ui.myapplications.alert.AlertViewMyApplication;
import ee.locawork.util.LocationUtil;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class AdapterMyApplications extends RecyclerView.Adapter<AdapterMyApplications.ViewHolder> {
    private Activity activity;
    private Context context;
    ControllerCancelApplication controllerCancelApplication = new ControllerCancelApplication();
    private List<MyApplicationDTO> listdata;

    public AdapterMyApplications(List<MyApplicationDTO> listdata2, Activity activity2, Context context2) {
        this.listdata = listdata2;
        this.context = context2;
        this.activity = activity2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_application, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final List<MyApplicationDTO> myListData = this.listdata;
        MyApplicationDTO myApplicationDTO = myListData.get(position);
        holder.jobTitle.setText(myApplicationDTO.getTitle());
        holder.jobSalary.setText(String.valueOf(myApplicationDTO.getSalary()));
        holder.location.setText(LocationUtil.fetchLocationData(this.activity, new LatLng(myApplicationDTO.getLatitude().doubleValue(), myApplicationDTO.getLongitude().doubleValue())));
        holder.cancelApplication.setOnClickListener(v -> new ControllerCancelApplication().cancelApplication(context, myListData.get(position).getId()));
        holder.jobCandidateContent.setOnClickListener(v -> AlertViewMyApplication.init(activity, context, myListData.get(position)));
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton cancelApplication;
        public LinearLayout jobCandidateContent;
        public LinearLayout jobContent;
        public TextView jobSalary;
        public TextView jobTitle;
        public TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            this.jobCandidateContent = itemView.findViewById(R.id.job_candidate_content);
            this.jobTitle = itemView.findViewById(R.id.tv_job_title);
            this.jobContent = itemView.findViewById(R.id.job_content);
            this.jobSalary = itemView.findViewById(R.id.tv_salary);
            this.cancelApplication = itemView.findViewById(R.id.cancel_application);
            this.location = itemView.findViewById(R.id.location);
        }
    }
}
