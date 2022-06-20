package org.locawork.ui.findjob;

import org.locawork.model.ResponseModel;

public class EventApplied {

    private ResponseModel responseModel;
    private int offererId;

    public EventApplied(ResponseModel body,int offererId) {
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
