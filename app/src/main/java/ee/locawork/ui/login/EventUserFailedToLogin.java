package ee.locawork.ui.login;

public class EventUserFailedToLogin {

    private Throwable t;
    private  String message;

    public EventUserFailedToLogin(Throwable t, String message) {
        this.t= t;
        this.message = message;
    }

    public Throwable getT() {
        return t;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
