package org.locawork.ui.addjobs;

import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;

public class EventAddJobNetSuccess {
    private Job job;

    public EventAddJobNetSuccess(Job job2) {
        this.job = job2;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job2) {
        this.job = job2;
    }
}
