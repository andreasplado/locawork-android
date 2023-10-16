package ee.locawork.ui.settings;

public class EventGetSettingsFailure {

    private Throwable t;

    public EventGetSettingsFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
