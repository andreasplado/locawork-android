package ee.locawork.ui.payformemeber;

public class PayForRemovingAddsFailure {
    Throwable t;
    public PayForRemovingAddsFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
