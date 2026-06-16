package com.example.gyminventory.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String PREF_NAME = "gym_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    public static void applyTheme(Context context) {
        boolean darkMode = isDarkMode(context);
        setThemePreference(context, darkMode);
    }

    public static void saveThemePreference(Context context, boolean isDark) {
        SharedPreferences sharedPreferences = getPreferences(context);
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isDark).apply();

        setThemePreference(context, isDark);
    }

    // De uso publico por si sirve de algo en otras clases para saber si hay tema oscuro
    public static boolean isDarkMode(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }


    // Helper para no escribir esto cada vez que se quiera acceder a las preferencias
    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Helper para cambiar el tema
    private static void setThemePreference(Context context, boolean isDark) {
        AppCompatDelegate.setDefaultNightMode(
                isDark
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );
    }
}