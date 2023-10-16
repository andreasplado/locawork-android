package ee.locawork.ui.findwork;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ramotion.fluidslider.FluidSlider;

import ee.locawork.R;
import ee.locawork.alert.AlertAskPermissionBeforeDeleting;
import ee.locawork.model.Job;
import ee.locawork.ui.findwork.alert.AlertViewJobInMap;
import ee.locawork.ui.findwork.alert.AlertEditJob;
import ee.locawork.util.AppConstants;
import ee.locawork.util.PrefConstants;
import ee.locawork.util.PreferencesUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterFoundJobs extends RecyclerView.Adapter<AdapterFoundJobs.ViewHolder> {

    private final Activity activity;
    private final Context context;
    private final ControllerDeleteJob controllerDeleteJob = new ControllerDeleteJob();
    private final List<Job> listdata;
    public AdapterFoundJobs(List<Job> listdata, Activity activity, Context context) {
        this.listdata = listdata;
        this.activity = activity;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multiple_jobs_found, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<Job> myListData = this.listdata;
        Job job = myListData.get(position);
        holder.titleTv.setText(myListData.get(position).getTitle());
        holder.jobDescription.setText(myListData.get(position).getDescription());
        holder.salaryTv.setText(Double.toString(myListData.get(position).getSalary()));

        holder.btnApply.setOnClickListener(v -> {
            AlertViewJobInMap.init(activity, context, job);
        });

        holder.btnUpdate.setOnClickListener(v -> {
            AlertEditJob.init(activity, context, job);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (PreferencesUtil.readString(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START).equals("1")) {
                AlertAskPermissionBeforeDeleting.init(activity, context, job);
            } else {
                controllerDeleteJob.deleteData(context, job);
            }
        });

        String myRole = PreferencesUtil.readString(context, PrefConstants.KEY_LOCAWORK_PREFS, "");
        if(myRole.equals(AppConstants.ROLE_JOB_OFFER)) {
            holder.btnUpdate.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnNavigate.setVisibility(View.GONE);
            holder.btnApply.setVisibility(View.GONE);
        }
        if(myRole.equals(AppConstants.ROLE_JOB_SEEKER)){
            holder.btnUpdate.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            if (job.getApplyerId() == 0) {
                holder.btnNavigate.setVisibility(View.GONE);
                holder.btnApply.setVisibility(View.VISIBLE);
            } else {
                holder.btnNavigate.setVisibility(View.VISIBLE);
                holder.btnApply.setVisibility(View.GONE);

            }
        }

    }


    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton btnApply, btnDelete, btnUpdate;
        public TextView jobDescription;
        public ImageButton btnNavigate;
        public TextView salaryTv;
        public TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.btnApply = itemView.findViewById(R.id.btn_apply);
            this.salaryTv = itemView.findViewById(R.id.tv_salary);
            this.titleTv = itemView.findViewById(R.id.tv_job_title);
            this.jobDescription = itemView.findViewById(R.id.tv_job_description);
            this.btnNavigate = itemView.findViewById(R.id.navigate);
            this.btnDelete = itemView.findViewById(R.id.btn_delete);
            this.btnUpdate = itemView.findViewById(R.id.btn_update);
        }
    }
}
