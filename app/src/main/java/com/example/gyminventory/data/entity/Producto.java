package com.example.gyminventory.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "productos",
        foreignKeys = @ForeignKey(
                entity = Categoria.class,
                parentColumns = "id",
                childColumns = "categoriaId",
                onDelete = ForeignKey.RESTRICT //Evita eliminar categorías si tienen productos.
        )
)
public class Producto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private double precio;
    private int stock;
    private String imagen; //Guarda la URI en formato String.

    @ColumnInfo(name = "categoriaId")
    private int categoryId;

    //Campo para eliminación lógica.
    private boolean activo;

    //Constructor vacio.
    public Producto(){}

    //Constructor.
    public Producto(String nombre, double precio, int stock, String imagen, int categoryId) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.categoryId = categoryId;
        this.activo = true; //Cada producto se registra al incio como activo.
    }

    //Getters y Setters.
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
