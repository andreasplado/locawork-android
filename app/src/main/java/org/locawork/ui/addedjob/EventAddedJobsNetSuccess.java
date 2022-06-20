package org.locawork.ui.addedjob;

import org.locawork.model.Job;
import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

public class EventAddedJobsNetSuccess {

    private Throwable t;
    private List<Job> jobs;

    public EventAddedJobsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventAddedJobsNetSuccess(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Throwable getT() {
        return t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
