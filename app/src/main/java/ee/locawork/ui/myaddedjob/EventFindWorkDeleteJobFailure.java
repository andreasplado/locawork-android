package ee.locawork.ui.myaddedjob;

import ee.locawork.model.Job;

class EventFindWorkDeleteJobFailure {
    private Job job;

    /* renamed from: t */
    private Throwable f96t;

    public EventFindWorkDeleteJobFailure(Throwable t) {
        this.f96t = t;
    }

    public EventFindWorkDeleteJobFailure(Job job2) {
        this.job = job2;
    }

    public Throwable getT() {
        return this.f96t;
    }

    public void setT(Throwable t) {
        this.f96t = t;
    }

    public Job getJobs() {
        return this.job;
    }

    public void setJobs(Job job2) {
        this.job = job2;
    }
}
