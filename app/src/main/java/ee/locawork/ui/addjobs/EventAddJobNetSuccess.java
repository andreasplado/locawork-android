package ee.locawork.ui.addjobs;

import ee.locawork.model.Job;
import ee.locawork.model.dto.JobDTO;

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
