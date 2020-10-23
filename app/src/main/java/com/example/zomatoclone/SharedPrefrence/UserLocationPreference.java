package com.example.zomatoclone.SharedPrefrence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserLocationPreference {
    private static SharedPreferences preferences;
    private static final String PREF_NAME = "UserLocationPreference";
    private static final String USER_LOCATION_LATITUDE = "userLocationLatitude";
    private static final String USER_LOCATION_LONGITUDE = "userLocationLongitude";
    private static final String USER_LOCATION_ADDRESS = "userLocationAddress";
    private static final String USER_AREA = "userAddressArea";
    private static final String USER_SUB_LOCALITY = "userSubLocality";

    public UserLocationPreference() {
    }

    public static void init(Context context){
        if (preferences == null){
            preferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }
    }

    public static void setUserLocationLatitude(double latitude){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(USER_LOCATION_LATITUDE, (float) latitude);
        editor.commit();
    }

    public static float getUserLocationLatitude(){
        return preferences.getFloat(USER_LOCATION_LATITUDE, 0);
    }

    public static void setUserLocationLongitude(double longitude){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(USER_LOCATION_LONGITUDE, (float) longitude);
        editor.commit();
    }
    public static float getUserLocationLongitude(){
        return preferences.getFloat(USER_LOCATION_LONGITUDE, 0);
    }

    public static void setUserLocationAddress(String address){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_LOCATION_ADDRESS, address);
        editor.commit();
    }

    public static String getUserLocationAddress(){
        return preferences.getString(USER_LOCATION_ADDRESS, null);
    }

    public static void setUserArea(String area) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_AREA, area);
        editor.commit();
    }

    public static String getUserArea() {
        return preferences.getString(USER_AREA, null);
    }

    public static void setUserSubLocality(String subLocality) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_SUB_LOCALITY, subLocality);
        editor.commit();
    }

    public static String getUserSubLocality(){
        return preferences.getString(USER_SUB_LOCALITY, null);
    }
}
