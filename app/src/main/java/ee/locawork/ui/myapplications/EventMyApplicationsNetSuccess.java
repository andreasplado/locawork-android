package ee.locawork.ui.myapplications;

import ee.locawork.model.MyApplications;

public class EventMyApplicationsNetSuccess {
    private MyApplications myApplications;
    private Throwable t;

    public EventMyApplicationsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventMyApplicationsNetSuccess(MyApplications myApplications2) {
        this.myApplications = myApplications2;
    }

    public Throwable getT() {
        return this.t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public MyApplications getMyApplications() {
        return this.myApplications;
    }

    public void setMyApplications(MyApplications myApplications2) {
        this.myApplications = myApplications2;
    }
}
