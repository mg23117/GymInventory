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

    // Obtiene todas las categorías
    @Query("SELECT * FROM Categoria")
    List<Categoria> getAllCategoria();

    // Actualiza una categoría existente
    @Update
    void update(Categoria categoria);

    // Elimina una categoría
    @Delete
    void delete(Categoria categoria);
}