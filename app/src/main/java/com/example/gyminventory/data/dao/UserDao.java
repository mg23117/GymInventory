package com.example.gyminventory.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gyminventory.data.entity.User;

// Interfaz que contiene consultas SQL para Room
@Dao
public interface UserDao {

    // Inserta un nuevo usuario
    @Insert
    void insert (User user);

    // Busca un usuario por correo
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    User getUserByEmail (String correo);

    // Valida credenciales para login
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    User login (String correo, String password);

}