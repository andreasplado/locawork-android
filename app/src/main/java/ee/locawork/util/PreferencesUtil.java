package ee.locawork.util;

import static ee.locawork.util.PrefConstants.KEY_LOCAWORK_PREFS;

import android.content.Context;

import ee.locawork.services.ServiceReachedJob;

public class PreferencesUtil {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_CARD_PARAMS = "card_params";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_RADIUS = "radius";
    public static final String KEY_COMPANY_REG_NUMBER = "company_reg_number";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_ROLE = "role";
    public static final String KEY_IS_WITHOUT_ADDS = "is_without_adds";

    public static final String TAG_RADIUS = "radius";
    public static final String KEY_PUSH_NOTIFICATION_TOKEN = "pushnotification_token";

    public static final String KEY_ID_CODE = "id_code";
    public static final String KEY_IS_PAYMENT_METHOD = "is_payment_method";
    public static final String KEY_ACCEPT_PAYMENT = "accept_payment";
    public static final String KEY_HAVE_STARTED = "key_have_started";
    public static final String KEY_WORK_START_TIME = "key_work_start_time";

    public static final String KEY_WORK_DURATION = "key_work_duration";


    public static void save(Context context, String valueKey, String value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putString(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, Boolean value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putBoolean(valueKey, value).apply();
    }

public static void save(Context context, String valueKey, boolean value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putBoolean(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, int value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putInt(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, long value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putLong(valueKey, value).apply();
    }

    public static void save(Context context, String valueKey, double value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putLong(valueKey, Double.doubleToRawLongBits(value)).apply();
    }

    public static void save(Context context, String valueKey, float value) {
        context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).edit().putFloat(valueKey, value).apply();
    }

    public static int readInt(Context context, String valueKey, int valueDefault) {
        return context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getInt(valueKey, valueDefault);
    }

    public static double readDouble(Context context, String valueKey, double valueDefault) {
        return Double.longBitsToDouble(context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getLong(valueKey, Double.doubleToLongBits(valueDefault)));
    }

    public static long readLong(Context context, String valueKey, long valueDefault) {
        return context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getLong(valueKey, valueDefault);
    }

    public static boolean readBoolean(Context context, String valueKey, boolean valueDefault) {
        return context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getBoolean(valueKey, valueDefault);
    }

    public static float readFloat(Context context, String valueKey, float valueDefault) {
        return context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getFloat(valueKey, valueDefault);
    }

    public static String readString(Context context, String valueKey, String valueDefault) {
        return context.getSharedPreferences(PrefConstants.KEY_LOCAWORK_PREFS, 0).getString(valueKey, valueDefault);
    }

    public static void flushDataOnLogout(Context context){
        PreferencesUtil.save(context, KEY_TOKEN, "");
        PreferencesUtil.save(context, KEY_EMAIL, "");
        PreferencesUtil.save(context, KEY_USER_ID, 0);
        PreferencesUtil.save(context, KEY_IS_WITHOUT_ADDS, false);
        PreferencesUtil.save(context, KEY_LOCAWORK_PREFS, "");
        PreferencesUtil.save(context, KEY_CARD_PARAMS, "");
        PreferencesUtil.save(context, KEY_RADIUS, 0);
        PreferencesUtil.save(context, KEY_COMPANY_NAME, "");
        PreferencesUtil.save(context, KEY_COMPANY_REG_NUMBER, "");
    }

    public static void flushJobProcess (Context context){
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_TITLE,"");
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_DESCRIPTION, "");
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_SALARY, "");
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LATITUDE, "");
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_LONGITUDE, "");
        PreferencesUtil.save(context, ServiceReachedJob.KEY_JOB_ID, 0);
        PreferencesUtil.save(context, PreferencesUtil.KEY_HAVE_STARTED, 0);
        PreferencesUtil.save(context, ServiceReachedJob.KEY_HAVE_REACHED, 0);
    }
}
