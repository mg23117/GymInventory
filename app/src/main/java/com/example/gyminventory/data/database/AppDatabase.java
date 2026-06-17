package com.example.gyminventory.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gyminventory.data.dao.UserDao;
import com.example.gyminventory.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.gyminventory.data.dao.CategoriaDao;
import com.example.gyminventory.data.entity.Categoria;

// Registramos todas las entidades de Room
@Database(
        entities = {User.class, Categoria.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    // Room generará automáticamente la implementación
    public abstract UserDao userDao();
    public abstract CategoriaDao categoriaDao();

    // Instancia única de la base de datos (Singleton)
    private static volatile AppDatabase INSTANCE;

    // Hilo para operaciones de base de datos
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = // Android no permite hacer consultas pesadas en el hilo principal.
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Obtiene la instancia única de la BD
    public static AppDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "gym_inventory_db"
                            )
                            // Reconstruye la BD si cambia la estructura
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}