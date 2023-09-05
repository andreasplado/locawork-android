package ee.locawork.ui.myaddedjob;

import ee.locawork.model.Job;
import ee.locawork.model.dto.JobDTO;

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
