package org.locawork.ui.settings;

public class EventLogoutFailure {

    private Throwable t;

    public EventLogoutFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
