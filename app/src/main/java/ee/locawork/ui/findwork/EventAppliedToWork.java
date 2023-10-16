package ee.locawork.ui.findwork;

import ee.locawork.model.ResponseModel;

public class EventAppliedToWork {

    private ResponseModel responseModel;
    private int offererId;

    public EventAppliedToWork(ResponseModel body, int offererId) {
        this.responseModel = body;
        this.offererId = offererId;
    }

    public ResponseModel getResponseModel() {
        return this.responseModel;
    }

    public int getOffererId() {
        return offererId;
    }
}
