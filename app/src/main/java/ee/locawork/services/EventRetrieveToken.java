package ee.locawork.services;

import retrofit2.Response;

public class EventRetrieveToken {

    private String token;
    public EventRetrieveToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
