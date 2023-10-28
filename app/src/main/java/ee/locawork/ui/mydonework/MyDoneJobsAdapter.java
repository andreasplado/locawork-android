package ee.locawork.ui.mydonework;

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
import ee.locawork.model.dto.JobDTO;
import ee.locawork.ui.mydonework.alert.MyDoneWorkAlert;
import ee.locawork.util.LocationUtil;
import ee.locawork.util.TimeUtil;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class MyDoneJobsAdapter extends RecyclerView.Adapter<MyDoneJobsAdapter.ViewHolder> {
    private Activity activity;
    private Context context;
    private List<JobDTO> listdata;

    public MyDoneJobsAdapter(List<JobDTO> listdata, Activity activity, Context context) {
        this.listdata = listdata;
        this.context = context;
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_done_work, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        List<JobDTO> myListData = this.listdata;
        JobDTO job = myListData.get(position);
        holder.titleTv.setText(job.getTitle());
        holder.salaryTv.setText(Double.toString(job.getSalary()) + "/" + context.getString(R.string.per_hour));
        String locationName = LocationUtil.fetchLocationData(this.activity, new LatLng(myListData.get(position).getLatitude(), myListData.get(position).getLongitude()));
        holder.location.setText(locationName);


        holder.jobDateTv.setText(TimeUtil.dayStringFormat(Long.parseLong(job.getWorkStartTime()), context) + " -\n" + TimeUtil.dayStringFormat(Long.parseLong(job.getWorkEndTime()), context));
        holder.itemView.setOnClickListener(v -> MyDoneWorkAlert.init(activity, context, listdata.get(position), locationName));
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
        public TextView jobDateTv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cancelJob = itemView.findViewById(R.id.cancel_job);
            this.navigateJob = itemView.findViewById(R.id.navigate);
            this.salaryTv = itemView.findViewById(R.id.tv_salary);
            this.titleTv = itemView.findViewById(R.id.tv_job_title);
            this.jobContent = itemView.findViewById(R.id.job_content);
            this.location = itemView.findViewById(R.id.location_name);
            this.jobDateTv = itemView.findViewById(R.id.tv_job_date);
        }
    }
}
