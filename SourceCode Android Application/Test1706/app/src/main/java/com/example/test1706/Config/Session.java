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
        prefs.edit().putBoolean("switchquangcao", bool).commit();
    }

    public boolean getSwitchQuangCao() {
        boolean usename = prefs.getBoolean("switchquangcao",true);
        return usename;
    }
    public void setSwitchHuongDan(boolean bool) {
        prefs.edit().putBoolean("switchhuongdan", bool).commit();
    }

    public boolean getSwitchHuongDan() {
        boolean usename = prefs.getBoolean("switchhuongdan",true);
        return usename;
    }




}
