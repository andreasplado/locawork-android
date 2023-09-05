package ee.locawork.ui.myapplications;

public class EventMyApplicationsCancelFailure {

    private Throwable t;

    public EventMyApplicationsCancelFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
