package ee.locawork.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class URLUtils {

    public static void goToUrl (String url, Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

}
