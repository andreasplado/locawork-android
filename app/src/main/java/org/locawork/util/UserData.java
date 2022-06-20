package org.locawork.util;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserData {

    public static String getGoogleId(Activity activity) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            return acct.getId();
        } else {
            return null;
        }
    }
    public static String getUserEmail(Activity activity) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(activity);
        if (acct != null) {
            return acct.getEmail();
        } else {
            return null;
        }
    }
}
