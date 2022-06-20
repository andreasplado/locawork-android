package org.locawork.util;

import org.locawork.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtils {
    public static void restartFragment(Fragment fragment) {
        FragmentTransaction tr = fragment.getFragmentManager().beginTransaction();
        tr.detach(fragment);
        tr.attach(fragment);
        tr.commit();
    }
}
