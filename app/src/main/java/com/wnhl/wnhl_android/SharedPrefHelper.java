package com.wnhl.wnhl_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Defines a Helper Class to easily access shared preferences
 */
public class SharedPrefHelper {

    /**
     * Inserts an Integer into Shared Preferences
     *
     * @param context
     * @param key Shared Pref Name
     * @param value Shared Pref Value
     */
    public static void putSharedPreferencesInt(Context context, String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }//putSharedPreferencesInt

    /**
     * Retrieves an Integer from Shared Preferences
     *
     * @param context
     * @param key Shared Pref Name
     * @param _default Default Value if Shared Pref Name does not yet exist
     * @return
     */
    public static int getSharedPreferencesInt(Context context, String key, int _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }//getSharedPreferencesInt

    /**
     * Inserts a Boolean into Shared Preferences
     *
     * @param context
     * @param key Shared Pref Name
     * @param value Shared Pref Value
     */
    public static void putSharedPreferencesBool(Context context, String key, boolean value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }//putSharedPreferencesBool

    /**
     * Retrieves a Boolean from Shared Preferences.
     *
     * @param context
     * @param key Shared Pref Name
     * @param _default Default Value if Shared Pref Name does not yet exist
     * @return
     */
    public static boolean getSharedPreferencesBool(Context context, String key, boolean _default){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key,_default);
    }//getSharedPreferencesBool

    /**
     * Retrieves an integer from Shared Preferences.
     *
     * @param context
     * @param key Shared Pref Name
     */
    public static void removeSharedPreferencesInt(Context context, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

}//SharedPrefHelper
