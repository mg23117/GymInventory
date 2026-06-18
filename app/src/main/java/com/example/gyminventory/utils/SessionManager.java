package com.example.gyminventory.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Gestiona la sesión del usuario mediante SharedPreferences.
public class SessionManager {

    // Nombre del archivo SharedPreferences
    private static final String PREF_NAME = "gym_inventory_session";

    // Claves de almacenamiento
    private static final String KEY_SESSION = "sesionActiva";
    private static final String KEY_EMAIL = "correoUsuario";
    private static final String KEY_NAME ="nombreUsuario";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor; // Editor utilizado para guardar, actualizar o eliminar datos en SharedPreferences

    public SessionManager (Context context) { // Inicializa el acceso al archivo de SharedPreferences de la aplicación
            preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = preferences.edit();
    }

    // Guarda la sesión del usuario
    public void saveSession (String nombre, String correo) {
        editor.putBoolean(KEY_SESSION, true);
        editor.putString(KEY_NAME, nombre);
        editor.putString(KEY_EMAIL, correo);

        editor.apply();
    }

    // Verifica si existe una sesión activa
    public boolean isLoggedIn () {
        return preferences.getBoolean(KEY_SESSION, false);
    }

    // Obtiene el nombre del usuario
    public String getUserName () {
        return preferences.getString(KEY_NAME, "");
    }

    // Obtiene el correo del usuario
    public String getEmail () {
        return preferences.getString(KEY_EMAIL, "");
    }

    // Cierra la sesión
    public void logout () {
        editor.clear();
        editor.apply();
    }

}
