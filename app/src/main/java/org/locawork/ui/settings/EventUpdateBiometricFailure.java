package org.locawork.ui.settings;

public class EventUpdateBiometricFailure {

    private Throwable t;

    public EventUpdateBiometricFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
