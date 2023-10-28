package ee.locawork;

import ee.locawork.model.User;
import retrofit2.Response;

public class EventFailedToGetEmployerData {
    private Throwable t;
    public EventFailedToGetEmployerData(Throwable t) {
        this.t = t;
    }

    public Throwable getT() {
        return this.t;
    }
}
