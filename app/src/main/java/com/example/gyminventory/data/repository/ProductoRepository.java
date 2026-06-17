package com.example.gyminventory.data.repository;

import android.content.Context;

import com.example.gyminventory.data.dao.ProductoDao;
import com.example.gyminventory.data.database.AppDatabase;
import com.example.gyminventory.data.entity.Categoria;
import com.example.gyminventory.data.entity.Producto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductoRepository {

    private final ProductoDao productoDao;
    private final ExecutorService executorService;
    private final Context context;

    public interface OnDataReadyCallBack<T> {
        void onDataReady(T result);
    }

    public ProductoRepository(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getDatabase(this.context);
        this.productoDao = db.productoDao();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void insertar(Producto producto) {
        executorService.execute(() -> productoDao.insertar(producto));
    }

    public void actualizar(Producto producto) {
        executorService.execute(() -> productoDao.actualizar(producto));
    }

    public void eliminarLogico(int id) {
        executorService.execute(() -> productoDao.eliminacionLogica(id));
    }

    public void obternerTodos(OnDataReadyCallBack<List<Producto>> callBack) {
        executorService.execute(() -> {
            List<Producto> productos = productoDao.obtenerTodos();
            callBack.onDataReady(productos);
        });
    }

    public void buscar(String texto, OnDataReadyCallBack<List<Producto>> callBack) {
        executorService.execute(() -> {
            List<Producto> productos = productoDao.buscarPorNombre("%" + texto + "%");
            callBack.onDataReady(productos);
        });
    }

    public void obtenerCategorias(OnDataReadyCallBack<List<Categoria>> callBack) {
        executorService.execute(() -> {
            List<Categoria> categorias = AppDatabase.getDatabase(context).categoriaDao().getAllCategoria();
            if (callBack != null) {
                callBack.onDataReady(categorias);
            }
        });
    }
}
