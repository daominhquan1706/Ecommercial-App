package com.example.test1706.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setSwitchQuangCao(boolean bool) {
        prefs.edit().putBoolean("switchquangcao", bool).apply();
    }

    public boolean getSwitchQuangCao() {
        return prefs.getBoolean("switchquangcao", true);
    }

    public void setSwitchHuongDan(boolean bool) {
        prefs.edit().putBoolean("switchhuongdan", bool).apply();
    }

    public boolean getSwitchHuongDan() {
        return prefs.getBoolean("switchhuongdan", true);
    }

    public void setLanguage(String language) {
        prefs.edit().putString("appLanguage", language).apply();
    }

    public String getLanguage() {
        return prefs.getString("appLanguage", "vi");
    }

    public void setIsChangeLanguage(boolean bool) {
        prefs.edit().putBoolean("setIsChangeLanguage", bool).apply();
    }

    public boolean getIsChangeLanguage() {
        return prefs.getBoolean("setIsChangeLanguage", true);
    }


    public void setLocation_Lat(String lat) {
        prefs.edit().putString("location_Lat", lat).apply();
    }

    public String getLocation_Lat() {
        return prefs.getString("location_Lat", "0");
    }

    public void setLocation_Lng(String lat) {
        prefs.edit().putString("location_Lng", lat).apply();
    }

    public String getLocation_Lng() {
        return prefs.getString("location_Lng", "0");
    }

    public void setLocation_Name(String lat) {
        prefs.edit().putString("location_Name", lat).apply();
    }

    public String getLocation_Name() {
        return prefs.getString("location_Name", "");
    }





}
