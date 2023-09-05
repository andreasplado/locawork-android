package ee.locawork.ui.myupcomingjob;

import ee.locawork.model.JobWithCategory;
import ee.locawork.model.dto.JobDTO;

import java.util.List;

public class EventUpcomingWorkNetSuccess {
    private List<JobDTO> jobs;

    private Throwable f107t;

    public EventUpcomingWorkNetSuccess(Throwable t) {
        this.f107t = t;
    }

    public EventUpcomingWorkNetSuccess(List<JobDTO> jobs2) {
        this.jobs = jobs2;
    }

    public Throwable getT() {
        return this.f107t;
    }

    public void setT(Throwable t) {
        this.f107t = t;
    }

    public List<JobDTO> getJobs() {
        return this.jobs;
    }

    public void setJobs(List<JobDTO> jobs2) {
        this.jobs = jobs2;
    }
}
