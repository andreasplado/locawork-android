package ee.locawork.ui.myaddedwork;

import ee.locawork.model.Job;

import java.util.List;

public class EventEmptyList {
    private List<Job> jobs;

    public EventEmptyList(List<Job> jobs2) {
        this.jobs = jobs2;
    }

    public List<Job> getJobs() {
        return this.jobs;
    }
}
