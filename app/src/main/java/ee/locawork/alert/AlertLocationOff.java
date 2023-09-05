package ee.locawork.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import ee.locawork.R;
import ee.locawork.ui.login.LoginActivity;
import ee.locawork.util.DialogUtils;

public class AlertLocationOff {

    public static void init(final Activity activity, final Context context) {
        String location;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.FullscreenDialog);
        View dialogView = ((LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert_location_off, (ViewGroup) null);
        ImageButton retry = dialogView.findViewById(R.id.retry);
        ImageButton logout = dialogView.findViewById(R.id.logout);
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build());
        logout.setOnClickListener(v -> {
            if(mGoogleSignInClient != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener(activity, task -> {
                    activity.startActivity(new Intent(context, LoginActivity.class));
                    activity.finish();
                });
            }
        });

        retry.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        });

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.show();
        //prevent to go back
        alertDialog.setOnKeyListener((dialog, keyCode, event) -> {
            return keyCode == KeyEvent.KEYCODE_BACK;
        });
        DialogUtils.setDialogOnTopOfScreen(alertDialog);
        alertDialog.show();
    }
}
