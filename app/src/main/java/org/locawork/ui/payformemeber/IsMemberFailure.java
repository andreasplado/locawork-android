package org.locawork.ui.payformemeber;

public class IsMemberFailure {
    Throwable t;
    public IsMemberFailure(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return t;
    }
}
