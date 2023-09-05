package ee.locawork.ui.myupcomingjob;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import ee.locawork.R;
import ee.locawork.model.dto.JobDTO;
import ee.locawork.services.ServiceReachedJob;
import ee.locawork.model.Job;
import ee.locawork.model.JobWithCategory;
import ee.locawork.ui.myupcomingjob.alert.AlertCantGo;
import ee.locawork.ui.myupcomingjob.alert.AlertGoToJob;
import ee.locawork.ui.settings.EventUpdateRadiusNotValid;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.PreferencesUtil;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class AdapterMyUpcomingWork extends RecyclerView.Adapter<AdapterMyUpcomingWork.ViewHolder> {
    private Activity activity;
    private Context context;
    private List<JobDTO> listdata;

    public AdapterMyUpcomingWork(List<JobDTO> listdata, Activity activity2, Context context2) {
        this.listdata = listdata;
        this.context = context2;
        this.activity = activity2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_upcoming_work, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final List<JobDTO> myListData = this.listdata;
        holder.titleTv.setText(myListData.get(position).getTitle());
        holder.salaryTv.setText(Double.toString(myListData.get(position).getSalary()));
        holder.cancelJob.setOnClickListener(view -> AlertCantGo.init(context, myListData.get(position).getId()));
        holder.navigateJob.setOnClickListener(v -> {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + (myListData.get(position)).getLatitude() + "," + ( myListData.get(position)).getLongitude() + "&travelmode=driving")));
            Intent i = new Intent(context, ServiceReachedJob.class);
            Bundle bundle = new Bundle();
            bundle.putDouble(ServiceReachedJob.KEY_JOB_LONGITUDE, myListData.get(position).getLongitude());
            bundle.putDouble(ServiceReachedJob.KEY_JOB_LATITUDE, myListData.get(position).getLatitude());
            i.putExtras(bundle);
            JobDTO job = myListData.get(position);
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_TITLE, job.getTitle());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_DESCRIPTION, job.getDescription());
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_SALARY, String.valueOf(job.getSalary()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LATITUDE, String.valueOf(job.getLatitude()));
            PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LONGITUDE, String.valueOf(job.getLongitude()));
            context.startService(i);
            EventBus.getDefault().post(new EventGoingToWork());
        });
        holder.location.setText(LocationUtil.fetchLocationData(this.activity, new LatLng(myListData.get(position).getLatitude(), myListData.get(position).getLongitude())));
        holder.jobContent.setOnClickListener(v -> AlertGoToJob.init(activity, context, listdata.get(position)));
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton cancelJob;
        public LinearLayout jobContent;
        public TextView location;
        public ImageButton navigateJob;
        public TextView salaryTv;
        public TextView titleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cancelJob = itemView.findViewById(R.id.cancel_job);
            this.navigateJob = itemView.findViewById(R.id.navigate);
            this.salaryTv = itemView.findViewById(R.id.tv_salary);
            this.titleTv = itemView.findViewById(R.id.tv_job_title);
            this.jobContent = itemView.findViewById(R.id.job_content);
            this.location = itemView.findViewById(R.id.location);
        }
    }
}
