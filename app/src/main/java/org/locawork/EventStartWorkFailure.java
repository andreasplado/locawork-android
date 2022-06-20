package org.locawork;

public class EventStartWorkFailure {

    private Throwable t;

    public EventStartWorkFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
