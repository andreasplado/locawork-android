package ee.locawork.ui.settings;

import ee.locawork.model.Settings;

import retrofit2.Response;

public class EventSettingsEditSuccess {

    private Response<Settings> response;
    public EventSettingsEditSuccess(Response<Settings> response) {
        this.response = response;
    }

    public Response<Settings> getSettings() {
        return response;
    }
}
