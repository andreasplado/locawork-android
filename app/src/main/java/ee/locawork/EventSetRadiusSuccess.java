package ee.locawork;

import ee.locawork.model.ResponseModel;

import retrofit2.Response;

public class EventSetRadiusSuccess {

    private Response<ResponseModel> response;

    public EventSetRadiusSuccess(Response<ResponseModel> response) {
        this.response = response;
    }

    public Response<ResponseModel> getResponse() {
        return response;
    }
}
