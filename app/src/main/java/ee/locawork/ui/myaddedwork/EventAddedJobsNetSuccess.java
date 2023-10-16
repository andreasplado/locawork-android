package ee.locawork.ui.myaddedwork;

import ee.locawork.model.Job;

import java.util.List;

public class EventAddedJobsNetSuccess {
    private List<Job> jobs;

    private Throwable t;

    public EventAddedJobsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventAddedJobsNetSuccess(List<Job> jobs2) {
        this.jobs = jobs2;
    }

    public Throwable getT() {
        return this.t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public List<Job> getJobs() {
        return this.jobs;
    }

    public void setJobs(List<Job> jobs2) {
        this.jobs = jobs2;
    }
}
