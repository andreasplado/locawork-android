package ee.locawork.ui.myupcomingwork.alert;

import ee.locawork.model.Job;

public class EventCantGoNetSuccess {
    private Job job;

    public EventCantGoNetSuccess(Job body) {
        this.job = body;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job2) {
        this.job = job2;
    }
}
