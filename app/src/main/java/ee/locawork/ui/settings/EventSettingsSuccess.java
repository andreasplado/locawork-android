package ee.locawork.ui.settings;

import ee.locawork.model.Settings;
import retrofit2.Response;

public class EventSettingsSuccess {
    private Response<Settings> settings;

    public EventSettingsSuccess(Response<Settings> response) {
        this.settings = response;
    }

    public Response<Settings> getSettings() {
        return this.settings;
    }
}
