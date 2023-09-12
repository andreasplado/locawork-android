package ee.locawork;

import ee.locawork.model.ResponseModel;
import retrofit2.Response;

public class EventEndWork {
    private Response<ResponseModel> response;

    public EventEndWork(Response<ResponseModel> response2) {
        this.response = response2;
    }

    public Response<ResponseModel> getResponse() {
        return this.response;
    }
}
