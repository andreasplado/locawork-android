package org.locawork.ui.settings;

import org.locawork.model.Message;
import org.locawork.model.Settings;

import retrofit2.Response;

public class EventLogoutSuccess {
    private Response<Message> settings;

    public EventLogoutSuccess(Response<Message> response) {
        this.settings = response;
    }

    public Response<Message> getSettings() {
        return this.settings;
    }
}
