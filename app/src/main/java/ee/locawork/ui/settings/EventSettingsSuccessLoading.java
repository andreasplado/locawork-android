package ee.locawork.ui.settings;

import ee.locawork.model.Settings;
import retrofit2.Response;

public class EventSettingsSuccessLoading {
    private Response<Settings> settings;

    public EventSettingsSuccessLoading(Response<Settings> response) {
        this.settings = response;
    }

    public Response<Settings> getSettings() {
        return this.settings;
    }
}
