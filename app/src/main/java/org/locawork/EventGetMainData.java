package org.locawork;

import org.locawork.model.MainData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Response;

public class EventGetMainData {
    GoogleSignInAccount googleSignInAccount;
    private Response<MainData> response;

    public EventGetMainData(Response<MainData> response2) {
        this.response = response2;
    }

    public Response<MainData> getResponse() {
        return this.response;
    }
}
