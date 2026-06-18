package com.example.gyminventory.ui.categories;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gyminventory.R;
import com.example.gyminventory.data.entity.Categoria;
import com.example.gyminventory.data.repository.CategoriaRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment implements CategoriaAdapter.OnCategoriaClickListener {

    private RecyclerView rvCategorias;
    private FloatingActionButton fabAgregarCategoria;

    private CategoriaAdapter adapter;
    private CategoriaRepository repository;


    private final List<Categoria> listaCategorias = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        rvCategorias = view.findViewById(R.id.rvCategorias);
        fabAgregarCategoria = view.findViewById(R.id.fabAgregarCategoria);

        repository = new CategoriaRepository(requireContext());


        adapter = new CategoriaAdapter(this);

        rvCategorias.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCategorias.setAdapter(adapter);

        cargarCategorias();

        fabAgregarCategoria.setOnClickListener(v -> mostrarDialogAgregar());

        return view;
    }

    // Carga todas las categorías guardadas
    private void cargarCategorias() {

        new Thread(() -> {

            List<Categoria> categorias = repository.getAllCategorias();

            requireActivity().runOnUiThread(() -> {
                listaCategorias.clear();
                listaCategorias.addAll(categorias);
                adapter.setCategorias(listaCategorias);
            });

        }).start();
    }

    // Dialog para agregar categoría
    private void mostrarDialogAgregar() {

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_categoria, null);

        EditText edtNombre = dialogView.findViewById(R.id.edtNombreCategoria);
        EditText edtIcono = dialogView.findViewById(R.id.edtIconoCategoria);

        new AlertDialog.Builder(requireContext())
                .setTitle("Nueva Categoría")
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String nombre = edtNombre.getText().toString().trim();
                    String icono = edtIcono.getText().toString().trim();

                    if (!nombre.isEmpty()) {

                        Categoria categoria = new Categoria(nombre, icono);

                        repository.insert(
                                categoria,
                                () -> requireActivity().runOnUiThread(this::cargarCategorias)
                        );
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Dialog para editar
    private void mostrarDialogEditar(Categoria categoria) {

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_categoria, null);

        EditText edtNombre = dialogView.findViewById(R.id.edtNombreCategoria);
        EditText edtIcono = dialogView.findViewById(R.id.edtIconoCategoria);

        edtNombre.setText(categoria.getNombre());
        edtIcono.setText(categoria.getIcono());

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar Categoría")
                .setView(dialogView)
                .setPositiveButton("Actualizar", (dialog, which) -> {

                    categoria.setNombre(
                            edtNombre.getText().toString().trim()
                    );

                    categoria.setIcono(
                            edtIcono.getText().toString().trim()
                    );

                    repository.update(
                            categoria,
                            () -> requireActivity().runOnUiThread(this::cargarCategorias)
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEditarClick(Categoria categoria) {
        mostrarDialogEditar(categoria);
    }


    // Realiza la eliminación lógica de la categoría.
    @Override
    public void onEliminarClick(Categoria categoria) {

        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar")
                .setMessage("¿Desea eliminar esta categoría?")
                .setPositiveButton("Sí", (dialog, which) -> {

                    repository.eliminacionLogica(
                            categoria.getId(),
                            () -> requireActivity().runOnUiThread(this::cargarCategorias)
                    );

                })
                .setNegativeButton("No", null)
                .show();
    }
}