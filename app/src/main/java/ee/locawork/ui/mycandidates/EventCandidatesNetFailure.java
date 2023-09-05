package ee.locawork.ui.mycandidates;

public class EventCandidatesNetFailure {

    private Throwable t;

    public EventCandidatesNetFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
