package com.example.gyminventory.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gyminventory.data.entity.Producto;

import java.util.List;

@Dao
public interface ProductoDao {

    @Insert
    void insertar(Producto producto);

    @Update
    void actualizar(Producto producto);

    //Obtiene todos los productos que no han sido eliminados lógicamente.
    @Query("SELECT * FROM productos WHERE activo = 1")
    List<Producto> obtenerTodos();

    //Filtro de búsqueda por nombre (solo los que se encuentran activos)
    @Query("SELECT * FROM productos WHERE activo = 1 AND nombre LIKE :busqueda")
    List<Producto> buscarPorNombre(String busqueda);

    //Eliminación lógica. Cambia el estado de activo a 0 = false.
    @Query("UPDATE productos SET activo = 0 WHERE id = :id")
    void eliminacionLogica(int id);

    // Obtiene la cantidad total de productos activos
    @Query("SELECT COUNT(*) FROM productos WHERE activo = 1")
    int getTotalProductos();

    // Cuenta productos activos asociados a una categoría
    @Query("SELECT COUNT(*) FROM productos WHERE categoriaId = :categoriaId AND activo = 1")
    int contarProductosPorCategoria(int categoriaId);
}
