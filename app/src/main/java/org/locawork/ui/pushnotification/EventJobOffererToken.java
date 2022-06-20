package org.locawork.ui.pushnotification;

import org.locawork.model.Note;

import retrofit2.Response;

public class EventJobOffererToken {
    private Response<Note> response;

    public EventJobOffererToken(Response<Note> response) {
        this.response = response;
    }

    public Response<Note> getResponse() {
        return response;
    }
}
