package com.example.gyminventory.data.repository;

import android.content.Context;

import com.example.gyminventory.data.dao.CategoriaDao;
import com.example.gyminventory.data.database.AppDatabase;
import com.example.gyminventory.data.entity.Categoria;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CategoriaRepository {

    private final CategoriaDao categoriaDao;

    // Obtiene acceso al DAO desde Room
    public CategoriaRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        categoriaDao = database.categoriaDao();
    }

    // Registrar categoría
    public void insert(Categoria categoria, Runnable onFinish){
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

    // Obtiene el número total de categorías
    public int getTotalCategorias() {
        // Ejecuta la consulta en un hilo secundario para obtener la cantidad total de categorías
        Future<Integer> future = AppDatabase.databaseWriteExecutor.submit(() -> categoriaDao.getTotalCategorias());

        try {
            return future.get(); // Espera el resultado de la consulta (y bloquea hasta recibir datos)
        } catch (ExecutionException | InterruptedException e) { // Si ocurre un error en la consulta o el hilo es interrumpido
            e.printStackTrace();
            return 0;
        }

    }

        // Realiza la eliminación lógica de una categoría.

        public void eliminacionLogica(int id, Runnable onFinish) {
            AppDatabase.databaseWriteExecutor.execute(() -> {

                // Ejecuta la consulta definida en el DAO
                categoriaDao.eliminacionLogica(id);


                if (onFinish != null) {
                    onFinish.run();
                }
            });
        }
}
