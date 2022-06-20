package org.locawork.ui.settings;

public class EventAskPermissionBeforeDeletingJobFailure {

    private Throwable t;

    public EventAskPermissionBeforeDeletingJobFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
