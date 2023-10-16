package ee.locawork.ui.myaddedwork;

import ee.locawork.model.JobWithCategory;

public class EventDeleteJobSuccess {
    private JobWithCategory jobs;

    private Throwable t;

    public EventDeleteJobSuccess(Throwable t) {
        this.t = t;
    }

    public EventDeleteJobSuccess(JobWithCategory jobs2) {
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
