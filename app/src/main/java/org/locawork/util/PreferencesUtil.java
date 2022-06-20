package org.locawork.util;

import android.content.Context;

public class PreferencesUtil {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PUSH_NOTIFICATION_TOKEN = "pushnotification_token";
    public static final String KEY_IS_PAYMENT_METHOD = "is_payment_method";


    public static void save(Context context, String valueKey, String value) {
        context.getSharedPreferences(PrefConstants.ROLE, 0).edit().putString(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, int value) {
        context.getSharedPreferences(PrefConstants.ROLE, 0).edit().putInt(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, long value) {
        context.getSharedPreferences(PrefConstants.ROLE, 0).edit().putLong(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, double value) {
        context.getSharedPreferences(PrefConstants.ROLE, 0).edit().putLong(valueKey, Double.doubleToRawLongBits(value)).apply();
    }

    public static void save(Context context, String valueKey, float value) {
        context.getSharedPreferences(PrefConstants.ROLE, 0).edit().putFloat(valueKey, value).apply();
    }

    public static int readInt(Context context, String valueKey, int valueDefault) {
        return context.getSharedPreferences(PrefConstants.ROLE, 0).getInt(valueKey, valueDefault);
    }

    public static double readDouble(Context context, String valueKey, double valueDefault) {
        return Double.longBitsToDouble(context.getSharedPreferences(PrefConstants.ROLE, 0).getLong(valueKey, Double.doubleToLongBits(valueDefault)));
    }

    public static long readLong(Context context, String valueKey, long valueDefault) {
        return context.getSharedPreferences(PrefConstants.ROLE, 0).getLong(valueKey, valueDefault);
    }

    public static boolean readBoolean(Context context, String valueKey, boolean valueDefault) {
        return context.getSharedPreferences(PrefConstants.ROLE, 0).getBoolean(valueKey, valueDefault);
    }

    public static float readFloat(Context context, String valueKey, float valueDefault) {
        return context.getSharedPreferences(PrefConstants.ROLE, 0).getFloat(valueKey, valueDefault);
    }

    public static String readString(Context context, String valueKey, String valueDefault) {
        return context.getSharedPreferences(PrefConstants.ROLE, 0).getString(valueKey, valueDefault);
    }
}
