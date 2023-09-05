package ee.locawork.ui.addedjob;

import ee.locawork.model.Job;
import ee.locawork.model.JobWithCategory;
import ee.locawork.model.dto.JobDTO;

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
