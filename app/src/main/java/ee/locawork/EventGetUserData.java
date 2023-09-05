package ee.locawork;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import ee.locawork.model.User;
import ee.locawork.ui.mycandidates.AdapterCandidates;

import retrofit2.Response;

public class EventGetUserData {
    GoogleSignInAccount googleSignInAccount;
    private Response<User> response;

    private View view;
    public EventGetUserData(Response<User> response2, View view) {
        this.view = view;
        this.response = response2;
    }

    public Response<User> getResponse() {
        return this.response;
    }

    public View getView() {
        return view;
    }
}
