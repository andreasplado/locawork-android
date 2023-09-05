package ee.locawork.ui.settings;

public class EventSettingsFailure {

    private Throwable t;

    public EventSettingsFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
