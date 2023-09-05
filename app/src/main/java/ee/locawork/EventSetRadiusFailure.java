package ee.locawork;

public class EventSetRadiusFailure {

    private Throwable t;

    public EventSetRadiusFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
