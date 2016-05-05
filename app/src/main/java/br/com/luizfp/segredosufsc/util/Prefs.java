package br.com.luizfp.segredosufsc.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luiz on 3/10/16.
 */
public class Prefs {
    private static final String PREFS_FILE_KEY = "prefs_file_key";
    public static final String ESTA_LOGADO_KEY = "esta_logado_key";
    public static final String TOKEN_KEY = "token_key";
    public static final String ID_USER_KEY = "id_user_key";
    public static final String INICIAL_USER_KEY = "inicial_user_key";
    public static final String COURSE_USER_KEY = "course_user_key";
    private static SharedPreferences sSharedPreferences;

    public static void putString(Context context, String key, String stringValue) {
        getEditor(context).putString(key, stringValue).commit();
    }

    public static String getString(Context context, String key) {
        String defaultValue = "";
        return getSharedPreferences(context).getString(key, defaultValue);
    }

    public static void putChar(Context context, String key, char charValue) {
        getEditor(context).putString(key, String.valueOf(charValue)).commit();
    }

    public static char getChar(Context context, String key) {
        String defaultValue = "";
        return getSharedPreferences(context).getString(key, defaultValue).charAt(0);
    }

    public static void putBoolean(Context context, String key, boolean booleanValue) {
        getEditor(context).putBoolean(key, booleanValue).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        boolean defaultValue = false;
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static void putLong(Context context, String key, Long longValue) {
        getEditor(context).putLong(key, longValue).commit();
    }

    public static Long getLong(Context context, String key) {
        long defaultValue = -1;
        return getSharedPreferences(context).getLong(key, defaultValue);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sSharedPreferences != null) {
            return sSharedPreferences;
        } else {
            sSharedPreferences = context.getSharedPreferences(PREFS_FILE_KEY, Context.MODE_PRIVATE);
            return sSharedPreferences;
        }
    }
}
