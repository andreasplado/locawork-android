package ee.locawork.ui.mycandidates;

import ee.locawork.model.JobApplications;

public class EventCandidateApplicationsNetSuccess {
    private JobApplications jobApplications;

    private Throwable t;

    public EventCandidateApplicationsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventCandidateApplicationsNetSuccess(JobApplications jobApplications2) {
        this.jobApplications = jobApplications2;
    }

    public Throwable getT() {
        return this.t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public JobApplications getJobApplications() {
        return this.jobApplications;
    }

    public void setJobApplications(JobApplications jobApplications2) {
        this.jobApplications = jobApplications2;
    }
}
