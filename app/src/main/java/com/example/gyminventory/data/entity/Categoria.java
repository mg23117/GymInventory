
package com.example.gyminventory.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Indica que esta clase representa una tabla en Room
@Entity(tableName = "categoria")
public class Categoria {

    // Clave primaria autoincrementable
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Nombre de la categoría
    private String nombre;

    // Nombre o ruta del icono asociado
    private String icono;

    // Constructor utilizado al crear una nueva categoría
    public Categoria(String nombre, String icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    // Métodos getter y setter para acceder y modificar los datos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}