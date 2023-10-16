package ee.locawork.ui.myaddedwork;

import ee.locawork.model.JobWithCategory;

public class EventAddedJobsNetFailure {
    private JobWithCategory jobs;

    private Throwable t;

    public EventAddedJobsNetFailure(Throwable t) {
        this.t = t;
    }

    public EventAddedJobsNetFailure(JobWithCategory jobs2) {
        this.jobs = jobs2;
    }

    public Throwable getT() {
        return this.t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public JobWithCategory getJobs() {
        return this.jobs;
    }

    public void setJobs(JobWithCategory jobs2) {
        this.jobs = jobs2;
    }
}
