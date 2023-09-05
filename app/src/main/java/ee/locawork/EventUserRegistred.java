package ee.locawork;

import ee.locawork.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Response;

public class EventUserRegistred {
    GoogleSignInAccount googleSignInAccount;
    private Response<User> response;

    public EventUserRegistred(Response<User> response2) {
        this.response = response2;
    }

    public Response<User> getResponse() {
        return this.response;
    }
}
