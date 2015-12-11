package com.example.bookshelf.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// Save the shared preference
public class SaveSharedPreference {
    // --{{{ START USER INFORMATION
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_ID = "id";
    private static final String PREF_FIRST_NAME = "firsname";
    private static final String PREF_LAST_NAME = "lastname";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_HOUSE_NUMBER = "housenumber";
    private static final String PREF_ZIP_CODE = "zipcode";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_PASSWORD, password);
        editor.commit();
    }

    public static void setID(Context context, String ID) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_ID, ID);
        editor.commit();
    }

    public static void setFirstName(Context context, String firstname) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_FIRST_NAME, firstname);
        editor.commit();
    }

    public static void setLastName(Context context, String lastname) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LAST_NAME, lastname);
        editor.commit();
    }

    public static void setHouseNumber(Context context, String housenumber) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_HOUSE_NUMBER, housenumber);
        editor.commit();
    }

    public static void setZipCode(Context context, String zipcode) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_LAST_NAME, zipcode);
        editor.commit();
    }

    public static void setEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_EMAIL, email);
        editor.commit();
    }

    public static String getZipCode(Context context, String zipcode) {
        return getSharedPreferences(context).getString(PREF_ZIP_CODE, "");
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_NAME, "");
    }

    public static String getHouseNumber(Context context) {
        return getSharedPreferences(context).getString(PREF_HOUSE_NUMBER, "");
    }

    public static String getPassword(Context context) {
        return getSharedPreferences(context).getString(PREF_PASSWORD, "");
    }

    public static String getID(Context context) {
        return getSharedPreferences(context).getString(PREF_ID, "");
    }

    public static String getFirstName(Context context) {
        return getSharedPreferences(context).getString(PREF_FIRST_NAME, "");
    }

    public static String getLastName(Context context) {
        return getSharedPreferences(context).getString(PREF_LAST_NAME, "");
    }

    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(PREF_EMAIL, "");
    }

    public static void logout(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().clear().commit();
    }
    // --{{{ END USER INFORMATION
}
