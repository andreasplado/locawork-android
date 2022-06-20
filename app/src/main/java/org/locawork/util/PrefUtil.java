package org.locawork.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import java.lang.ref.WeakReference;

public class PrefUtil {
    public static void saveToPrefs(Context context, String key, Object value) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences((Context) contextWeakReference.get()).edit();
            if (value instanceof Integer) {
                editor.putInt(key, ((Integer) value).intValue());
            } else if (value instanceof String) {
                editor.putString(key, value.toString());
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (value instanceof Long) {
                editor.putLong(key, ((Long) value).longValue());
            } else if (value instanceof Float) {
                editor.putFloat(key, ((Float) value).floatValue());
            } else if (value instanceof Double) {
                editor.putLong(key, Double.doubleToRawLongBits(((Double) value).doubleValue()));
            }
            editor.commit();
        }
    }

    public static Object getFromPrefs(Context context, String key, Object defaultValue) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences((Context) contextWeakReference.get());
            try {
                if (defaultValue instanceof String) {
                    return sharedPrefs.getString(key, defaultValue.toString());
                }
                if (defaultValue instanceof Integer) {
                    return Integer.valueOf(sharedPrefs.getInt(key, ((Integer) defaultValue).intValue()));
                }
                if (defaultValue instanceof Boolean) {
                    return Boolean.valueOf(sharedPrefs.getBoolean(key, ((Boolean) defaultValue).booleanValue()));
                }
                if (defaultValue instanceof Long) {
                    return Long.valueOf(sharedPrefs.getLong(key, ((Long) defaultValue).longValue()));
                }
                if (defaultValue instanceof Float) {
                    return Float.valueOf(sharedPrefs.getFloat(key, ((Float) defaultValue).floatValue()));
                }
                if (defaultValue instanceof Double) {
                    return Double.valueOf(Double.longBitsToDouble(sharedPrefs.getLong(key, Double.doubleToLongBits(((Double) defaultValue).doubleValue()))));
                }
            } catch (Exception e) {
                Log.e("Execption", e.getMessage());
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static void removeFromPrefs(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences((Context) contextWeakReference.get()).edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public static boolean hasKey(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences((Context) contextWeakReference.get()).contains(key);
        }
        return false;
    }
}
