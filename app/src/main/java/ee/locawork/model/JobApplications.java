package ee.locawork.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JobApplications {
    @SerializedName("job_applications")
    private List<JobApplicationDTO> jobApplications;

    public List<JobApplicationDTO> getJobApplications() {
        return this.jobApplications;
    }

    public void setJobApplications(List<JobApplicationDTO> jobApplications2) {
        this.jobApplications = jobApplications2;
    }
}
