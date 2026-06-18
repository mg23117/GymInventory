package com.example.gyminventory.data.repository;

import android.content.Context;

import com.example.gyminventory.data.dao.CategoriaDao;
import com.example.gyminventory.data.database.AppDatabase;
import com.example.gyminventory.data.entity.Categoria;

import java.util.List;

public class CategoriaRepository {

    private final CategoriaDao categoriaDao;

    // Obtiene acceso al DAO desde Room
    public CategoriaRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        categoriaDao = database.categoriaDao();
    }

    // Registrar categoría
    public void insert(Categoria categoria, Runnable onFinish) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoriaDao.insert(categoria);

            if (onFinish != null) {
                onFinish.run();
            }
        });
    }

    // Obtener todas las categorías
    public List<Categoria> getAllCategorias() {
        return categoriaDao.getAllCategoria();
    }

    // Actualizar categoría
    public void update(Categoria categoria, Runnable onFinish) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoriaDao.update(categoria);

            if (onFinish != null) {
                onFinish.run();
            }
        });
    }

    // Eliminar categoría
    public void delete(Categoria categoria, Runnable onFinish) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            categoriaDao.delete(categoria);

            if (onFinish != null) {
                onFinish.run();
            }
        });
    }
}