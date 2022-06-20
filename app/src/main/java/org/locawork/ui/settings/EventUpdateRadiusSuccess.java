package org.locawork.ui.settings;

import org.locawork.model.ResponseModel;

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
