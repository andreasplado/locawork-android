package ee.locawork;

public class EventEndWorkFailure {

    private Throwable t;

    public EventEndWorkFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
