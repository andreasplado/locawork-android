package ee.locawork.ui.settings;

import ee.locawork.model.ResponseModel;

import retrofit2.Response;

public class EventUpdateRadiusSuccess {

    private Response<ResponseModel> response;

    public EventUpdateRadiusSuccess(Response<ResponseModel> response) {
        this.response = response;
    }

    public Response<ResponseModel> getResponse() {
        return response;
    }
}
