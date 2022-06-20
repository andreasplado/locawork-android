package org.locawork;

public class EventRoleFailedToSelect {

    private Throwable t;

    public EventRoleFailedToSelect(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
