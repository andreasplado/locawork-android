package ee.locawork.alert;

import ee.locawork.model.ResponseModel;

import retrofit2.Response;

public class PayForStartingGivingWork {

    private Response<ResponseModel> body;

    public PayForStartingGivingWork(Response<ResponseModel> body) {
        this.body = body;
    }

    public Response<ResponseModel> getBody() {
        return body;
    }
}
