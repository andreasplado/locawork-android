package ee.locawork;

public class TokenRegistredEvent {
    private String token;
    public TokenRegistredEvent(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
