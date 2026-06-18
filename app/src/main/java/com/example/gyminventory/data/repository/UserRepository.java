package com.example.gyminventory.data.repository;

import android.content.Context;

import com.example.gyminventory.data.dao.UserDao;
import com.example.gyminventory.data.database.AppDatabase;
import com.example.gyminventory.data.entity.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// Capa intermedia que gestiona el acceso a los datos de usuarios entre la UI y Room

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

    // Busca un usuario por correo
    public User getUserByEmail(String correo) {
        // Ejecuta la consulta en un hilo secundario (NO UI THREAD)
        Future<User> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.getUserByEmail(correo)); // submit manda la tarea a otro hilo

        try {
            return future.get(); // Espera el resultado de la consulta (y bloquea hasta recibir datos)
        } catch (ExecutionException | InterruptedException e) { // Si ocurre un error en la consulta o el hilo es interrumpido
            e.printStackTrace();
            return null;
        }
    }

    // Valida login
    public User login(String correo, String password) {

        // Ejecuta la consulta en background para validar credenciales
        Future<User> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.login(correo, password));

        try {
            return future.get(); // Espera el resultado: usuario encontrado o null
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}