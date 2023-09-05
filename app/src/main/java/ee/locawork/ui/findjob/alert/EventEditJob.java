package ee.locawork.ui.findjob.alert;

import android.app.AlertDialog;
import ee.locawork.model.Job;

public class EventEditJob {
    private AlertDialog alertDialog;
    private Job job;

    public EventEditJob(Job job2) {
        this.job = job2;
    }

    public EventEditJob(Job body, AlertDialog alertDialog2) {
    }

    public AlertDialog getAlertDialog() {
        return this.alertDialog;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job2) {
        this.job = job2;
    }
}
