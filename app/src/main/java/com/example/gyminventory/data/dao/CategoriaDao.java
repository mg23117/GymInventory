package com.example.gyminventory.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gyminventory.data.entity.Categoria;

import java.util.List;

// Interfaz que contiene consultas SQL para Room
@Dao
public interface CategoriaDao {

    // Inserta una nueva categoría
    @Insert
    void insert(Categoria categoria);

    // Obtiene solo las categorías activas.
    // Las categorías con activo = 0 no se muestran en la lista.
    @Query("SELECT * FROM Categoria WHERE activo = 1")
    List<Categoria> getAllCategoria();

    // Actualiza una categoría existente
    @Update
    void update(Categoria categoria);

    // Elimina una categoría
    @Delete
    void delete(Categoria categoria);

    // Obtiene la cantidad total de categorías registradas
    @Query("SELECT COUNT(*) FROM categoria")
    int getTotalCategorias();

    // Realiza eliminación lógica.
     // No borra el registro, solo cambia activo a 0.
    @Query("UPDATE Categoria SET activo = 0 WHERE id = :id")
    void eliminacionLogica(int id);
}
