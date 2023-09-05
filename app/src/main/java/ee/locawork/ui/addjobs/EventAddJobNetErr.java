package ee.locawork.ui.addjobs;


public class EventAddJobNetErr {

    private Throwable f87t;

    public EventAddJobNetErr(Throwable t) {
        this.f87t = t;
    }

    public Throwable getT() {
        return this.f87t;
    }

    public void setT(Throwable t) {
        this.f87t = t;
    }
}
