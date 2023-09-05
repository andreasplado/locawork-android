package ee.locawork.ui.myaddedjob;

import ee.locawork.model.Job;

public class EventMyAddedWorkDeleteSuccess {
    private Job job;

    public EventMyAddedWorkDeleteSuccess(Job body) {
        this.job = body;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job jobId2) {
        this.job = jobId2;
    }
}
