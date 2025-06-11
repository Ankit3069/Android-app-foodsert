package com.example.personalizedecommerceapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPref {

    public static SharedPreferences sharedPreferences(Context con) {
        return con.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
    }

    public static void setUserId(Context con, int value) {
        SharedPreferences.Editor editor = sharedPreferences(con).edit();
        editor.putInt("userId", value);
        editor.apply();
    }

    public static int getUserId(Context con) {
        return sharedPreferences(con).getInt("userId", -1);
    }

    public static void setLoginUserType(Context con, String value) {
        SharedPreferences.Editor editor = sharedPreferences(con).edit();
        editor.putString("loggedInUserType", value);
        editor.apply();
    }

    public static String getLoginUserType(Context con) {
        return sharedPreferences(con).getString("loggedInUserType", "");
    }

    public static void deleteAll(Context context) {
        SharedPreferences.Editor editor = sharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public static void setLoc(Context con, String value) {
        SharedPreferences.Editor editor = sharedPreferences(con).edit();
        editor.putString("Loc", value);
        editor.apply();
    }

    public static String getLoc(Context con) {
        return sharedPreferences(con).getString("Loc", "");
    }

}
