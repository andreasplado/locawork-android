package org.locawork.ui.appliedjob;

import org.locawork.model.JobWithCategory;
import org.locawork.model.dto.JobDTO;

import java.util.List;

public class EventAppliedJobsNetSuccess {

    private Throwable t;
    private List<JobDTO> jobs;

    public EventAppliedJobsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventAppliedJobsNetSuccess(List<JobDTO> jobs) {
        this.jobs = jobs;
    }

    public Throwable getT() {
        return t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public List<JobDTO> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobDTO> jobs) {
        this.jobs = jobs;
    }
}
