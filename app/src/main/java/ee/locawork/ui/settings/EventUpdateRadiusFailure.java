package ee.locawork.ui.settings;

public class EventUpdateRadiusFailure {

    private Throwable t;

    public EventUpdateRadiusFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
