package ee.locawork.ui.findwork;

import ee.locawork.model.Job;

public class EventApplyedJob {
    private Job job;

    public EventApplyedJob(Job job2) {
        this.job = job2;
    }

    public EventApplyedJob() {
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job2) {
        this.job = job2;
    }
}
