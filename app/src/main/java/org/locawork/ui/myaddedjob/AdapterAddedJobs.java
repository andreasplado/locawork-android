package org.locawork.ui.myaddedjob;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.locawork.R;
import org.locawork.alert.AlertAskPermissionBeforeDeleting;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;
import org.locawork.ui.findjob.alert.AlertEditJob;
import org.locawork.ui.myaddedjob.alert.AlertAddedJob;
import org.locawork.util.LocationUtil;
import org.locawork.util.PrefConstants;
import org.locawork.util.PreferencesUtil;
import com.google.android.gms.maps.model.LatLng;
import com.ramotion.fluidslider.FluidSlider;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterAddedJobs extends RecyclerView.Adapter<AdapterAddedJobs.ViewHolder> {
    private Activity activity;
    private Context context;
    private List<Job> myListData;

    public AdapterAddedJobs(List<Job> myListData2, Context context2, Activity activity) {
        this.myListData = myListData2;
        this.context = context2;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_added_job, parent, false);
        return new ViewHolder(listItem);
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Job job = this.myListData.get(position);
        String location = LocationUtil.fetchLocationData(this.activity, new LatLng(job.getLatitude(), job.getLongitude()));
        holder.titleTv.setText(this.myListData.get(position).getTitle());
        holder.salaryTv.setText(Double.toString(this.myListData.get(position).getSalary()));
        holder.jobContent.setOnClickListener(v -> AlertAddedJob.init(AdapterAddedJobs.this, myListData, position, activity, context, location));
        holder.edit.setOnClickListener(v -> AlertEditJob.init(activity, context, job));
        holder.location.setText(location);
        holder.delete.setOnClickListener(v -> {
            if (PreferencesUtil.readString(context, PrefConstants.TAG_ASK_PERMISSION_BEFORE_DELETING_JOB, FluidSlider.TEXT_START).equals("1")) {
                new ControllerDeleteJob().deleteJob(context, job.getId());
            } else {
                AlertAskPermissionBeforeDeleting.init(activity, context, job);
            }
        });
    }

    public int getItemCount() {
        EventBus.getDefault().post(new EventEmptyList(this.myListData));
        return this.myListData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton delete;
        public ImageButton edit;
        public LinearLayout jobContent;
        public TextView location;
        public TextView salaryTv;
        public TextView titleTv;

        ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageButton) itemView.findViewById(R.id.edit_job);
            this.delete = (ImageButton) itemView.findViewById(R.id.cancel_application);
            this.salaryTv = (TextView) itemView.findViewById(R.id.tv_salary);
            this.titleTv = (TextView) itemView.findViewById(R.id.tv_job_title);
            this.jobContent = (LinearLayout) itemView.findViewById(R.id.job_content);
            this.location = (TextView) itemView.findViewById(R.id.location);
        }
    }
}
