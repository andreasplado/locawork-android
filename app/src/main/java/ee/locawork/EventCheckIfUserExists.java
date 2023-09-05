package ee.locawork;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import retrofit2.Response;

public class EventCheckIfUserExists {
    private Boolean body;
    private GoogleSignInAccount googleSignInAccount;

    public EventCheckIfUserExists(Boolean body2, GoogleSignInAccount googleSignInAccount2) {
        this.body = body2;
        this.googleSignInAccount = googleSignInAccount2;
    }

    public EventCheckIfUserExists(Response<Boolean> response) {
    }

    public Boolean getBody() {
        return this.body;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {
        return this.googleSignInAccount;
    }
}
