package org.locawork.ui.findjob;

import android.app.AlertDialog;
import org.locawork.model.Job;
import org.locawork.model.dto.JobDTO;

public class EventDeleteJob {
    private AlertDialog alertDialog;
    private Job job;

    private Throwable f90t;

    public EventDeleteJob(Throwable t) {
        this.f90t = t;
    }

    public EventDeleteJob(Job job, AlertDialog alertDialog2) {
        this.job = job;
        this.alertDialog = alertDialog2;
    }

    public EventDeleteJob(Job job) {
        this.job = job;
    }

    public Throwable getT() {
        return this.f90t;
    }

    public void setT(Throwable t) {
        this.f90t = t;
    }

    public Job getJobs() {
        return this.job;
    }

    public void setJobs(Job job) {
        this.job = job;
    }

    public AlertDialog getAlertDialog() {
        return this.alertDialog;
    }
}
