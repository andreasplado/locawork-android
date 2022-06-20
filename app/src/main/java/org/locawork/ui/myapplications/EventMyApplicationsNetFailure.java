package org.locawork.ui.myapplications;

public class EventMyApplicationsNetFailure {

    private Throwable t;

    public EventMyApplicationsNetFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
