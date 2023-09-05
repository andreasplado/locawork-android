package ee.locawork.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClickUtils {

    public static void copyText(Context context, String label, String text){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }
}
