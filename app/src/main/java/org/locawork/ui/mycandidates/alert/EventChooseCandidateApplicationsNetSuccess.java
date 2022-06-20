package org.locawork.ui.mycandidates.alert;

import org.locawork.model.ResponseModel;

public class EventChooseCandidateApplicationsNetSuccess {
    private ResponseModel responseModel;
    private Throwable t;

    public EventChooseCandidateApplicationsNetSuccess(Throwable t) {
        this.t = t;
    }

    public EventChooseCandidateApplicationsNetSuccess(ResponseModel responseModel2) {
        this.responseModel = responseModel2;
    }

    public Throwable getT() {
        return this.t;
    }

    public void setT(Throwable t) {
        this.t = t;
    }

    public ResponseModel getResponseModel() {
        return this.responseModel;
    }

    public void setResponseModel(ResponseModel responseModel2) {
        this.responseModel = responseModel2;
    }
}
