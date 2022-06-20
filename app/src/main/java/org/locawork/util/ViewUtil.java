package org.locawork.util;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {
    public static void replaceView(View oldV, View newV) {
        ViewGroup par = (ViewGroup) oldV.getParent();
        if (par != null) {
            int i1 = par.indexOfChild(oldV);
            par.removeViewAt(i1);
            par.addView(newV, i1);
        }
    }
}
