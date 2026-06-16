package com.example.gyminventory.data.repository;

import android.content.Context;

import com.example.gyminventory.data.dao.UserDao;
import com.example.gyminventory.data.database.AppDatabase;
import com.example.gyminventory.data.entity.User;

public class UserRepository {

    private final UserDao userDao;

    // Obtiene acceso al DAO desde Room
    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        userDao = database.userDao();
    }

    // Registrar usuario
    public void insert(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> { // databaseWriteExecutor ejecuta el método pero en un hilo secundario
            userDao.insert(user);
        });
    }

    // Buscar usuario por correo
    public User getUserByEmail(String correo) {
        return userDao.getUserByEmail(correo);
    }

    // Validar login
    public User login(String correo, String password) {
        return userDao.login(correo, password);
    }
}