package org.locawork.ui.findjob;

public class EventApplyFailure {

    private Throwable f88t;

    public EventApplyFailure(Throwable t) {
        this.f88t = t;
    }

    public Throwable getT() {
        return this.f88t;
    }
}
