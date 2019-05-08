package com.example.test1706.Config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

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

    private void setLocale(Context context) {
        String lang = getLanguage();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", lang);

        editor.apply();

    }

}
