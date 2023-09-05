package ee.locawork;

import ee.locawork.model.ResponseModel;

import retrofit2.Response;

public class EventStartWork {
    private Response<ResponseModel> response;

    public EventStartWork(Response<ResponseModel> response2) {
        this.response = response2;
    }

    public Response<ResponseModel> getResponse() {
        return this.response;
    }
}
