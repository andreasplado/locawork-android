package ee.locawork.alert;

public class PayForStartGivingWorkFailure {
    Throwable t;
    public PayForStartGivingWorkFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
