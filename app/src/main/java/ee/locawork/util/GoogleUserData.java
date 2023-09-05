package ee.locawork.util;

import android.app.Activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GoogleUserData {
    public static String getGoogleId(Activity activity) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            return acct.getId();
        }
        return null;
    }

    public static String getUserEmail(Activity activity) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            return acct.getEmail();
        }
        return null;
    }
}
