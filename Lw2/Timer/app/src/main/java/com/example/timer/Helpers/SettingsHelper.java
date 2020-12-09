package com.example.timer.Helpers;

import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.example.timer.R;

import java.util.Locale;

public  class SettingsHelper
{
    public static int Theme;
    public static Configuration configuration;
    public static  Locale locale;

    public static void  SetSettings(SharedPreferences sharedPreferences)
    {
        if (sharedPreferences.getBoolean("nightMode",  false))
        {
            Theme = R.style.my_theme_dark;
        } else
        {
            Theme = R.style.my_theme_light;
        }
        configuration = new Configuration();
        configuration.fontScale = Float.parseFloat(sharedPreferences.getString("fontSize", "1.0"));

        locale = new Locale(sharedPreferences.getString("language", "RU"));
        Locale.setDefault(locale);
        configuration.locale = locale;
    }
}
