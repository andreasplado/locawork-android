package org.locawork;

public class EventUserFailedToLogin {

    private Throwable t;

    public EventUserFailedToLogin(Throwable t) {
        this.t= t;
    }

    public Throwable getT() {
        return t;
    }
}
