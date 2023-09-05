package ee.locawork.ui.payformemeber;

import ee.locawork.model.ResponseModel;

import retrofit2.Response;

public class PayForRemovingAdds {

    private Response<ResponseModel>  body;

    public PayForRemovingAdds(Response<ResponseModel> body) {
        this.body = body;
    }

    public Response<ResponseModel>  getBody() {
        return body;
    }
}
