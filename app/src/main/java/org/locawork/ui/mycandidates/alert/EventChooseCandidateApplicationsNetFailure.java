package org.locawork.ui.mycandidates.alert;

public class EventChooseCandidateApplicationsNetFailure {

    private Throwable t;

    public EventChooseCandidateApplicationsNetFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
