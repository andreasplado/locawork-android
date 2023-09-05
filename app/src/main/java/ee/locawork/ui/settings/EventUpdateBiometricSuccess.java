package ee.locawork.ui.settings;

import ee.locawork.model.Settings;

import retrofit2.Response;

public class EventUpdateBiometricSuccess {

    private Response<Settings> settingsResponse;

    public EventUpdateBiometricSuccess(Response<Settings> settingsResponse) {
        this.settingsResponse = settingsResponse;
    }

    public Response<Settings> getSettingsResponse() {
        return settingsResponse;
    }
}
