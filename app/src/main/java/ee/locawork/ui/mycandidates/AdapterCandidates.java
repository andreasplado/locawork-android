package ee.locawork.ui.mycandidates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ee.locawork.R;
import ee.locawork.model.JobApplicationDTO;
import ee.locawork.model.PushNotificationRequest;
import ee.locawork.ui.mycandidates.alert.AlertChooseCandidate;
import ee.locawork.ui.mycandidates.alert.ControllerChooseCandidate;
import ee.locawork.util.PreferencesUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static ee.locawork.util.PreferencesUtil.KEY_USER_ID;
import static ee.locawork.util.PreferencesUtil.readString;

public class AdapterCandidates extends RecyclerView.Adapter<AdapterCandidates.ViewHolder> {
    private Context context;
    ControllerChooseCandidate controllerChooseCandidate = new ControllerChooseCandidate();
    ControllerSendNotificationToApplyer controllerSendNotificationToApplyer = new ControllerSendNotificationToApplyer();
    private List<JobApplicationDTO> listdata;

    public AdapterCandidates(List<JobApplicationDTO> listdata2, Context context) {
        this.listdata = listdata2;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_candidate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCandidates.ViewHolder holder, final int position) {
        final List<JobApplicationDTO> myListData = this.listdata;
        final JobApplicationDTO jobApplicationDTO = myListData.get(position);
        holder.jobDescription.setText(jobApplicationDTO.getDescription());
        holder.jobTitle.setText(jobApplicationDTO.getTitle());
        holder.jobSalary.setText(String.valueOf(jobApplicationDTO.getSalary()));
        holder.jobApplyerEmail.setText(jobApplicationDTO.getAccountEmail());
        holder.applyerContact.setText(jobApplicationDTO.getContact());
        holder.jobCandidateContent.setOnClickListener(v -> AlertChooseCandidate.init(context, jobApplicationDTO));
        holder.approve.setOnClickListener(v -> {
            PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
            pushNotificationRequest.setMessage("Teid valiti tööle");
            pushNotificationRequest.setToken(readString(context, PreferencesUtil.KEY_PUSH_NOTIFICATION_TOKEN , ""));
            pushNotificationRequest.setTopic("chosen_to_work");
            pushNotificationRequest.setTitle("test");
            controllerSendNotificationToApplyer.postData(pushNotificationRequest, context);
            controllerChooseCandidate.apply(context, jobApplicationDTO.getUserId(), PreferencesUtil.readInt(context, KEY_USER_ID, 0));
        });
    }

    public int getItemCount() {
        return this.listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView applyerContact;
        public ImageButton approve;
        public TextView jobApplyerEmail;
        public LinearLayout jobCandidateContent;
        public LinearLayout jobContent;
        public TextView jobDescription;
        public TextView jobSalary;
        public TextView jobTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            this.jobCandidateContent = itemView.findViewById(R.id.job_candidate_content);
            this.jobApplyerEmail = itemView.findViewById(R.id.job_applyer_email);
            this.jobTitle = itemView.findViewById(R.id.tv_job_title);
            this.jobContent = itemView.findViewById(R.id.job_content);
            this.jobSalary = itemView.findViewById(R.id.tv_salary);
            this.approve = itemView.findViewById(R.id.approve);
            this.jobDescription = itemView.findViewById(R.id.job_description);
            this.applyerContact = itemView.findViewById(R.id.applyer_contact);
        }
    }
}
