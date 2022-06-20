package org.locawork.ui.myaddedjob;

import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;

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
