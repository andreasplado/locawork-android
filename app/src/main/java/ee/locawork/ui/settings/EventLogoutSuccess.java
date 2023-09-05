package ee.locawork.ui.settings;

import ee.locawork.model.Message;
import ee.locawork.model.Settings;

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
