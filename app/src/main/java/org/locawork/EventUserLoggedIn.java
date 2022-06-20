package org.locawork;

import org.locawork.model.User;
import org.locawork.model.UserLogin;

import retrofit2.Response;

public class EventUserLoggedIn {

    private Response<Void> response;

    public EventUserLoggedIn(Response<Void> response) {
        this.response = response;
    }

    public Response<Void> getResponse() {
        return response;
    }
}
