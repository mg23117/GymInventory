package com.example.gyminventory.ui.products;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gyminventory.R;
import com.example.gyminventory.data.entity.Categoria;
import com.example.gyminventory.data.entity.Producto;
import com.example.gyminventory.data.repository.ProductoRepository;
import com.google.android.material.textfield.TextInputEditText;

public class ProductsFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {

    private ProductoRepository repository;
    private ProductoAdapter adapter;
    private RecyclerView rvProductos;
    private TextInputEditText etBuscar;
    private String uriImagenSeleccionada = "";
    private ImageView imgPrevisualizacionDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedIntanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        repository = new ProductoRepository(requireContext());
        rvProductos = view.findViewById(R.id.rvProductos);
        etBuscar = view.findViewById(R.id.etBuscar);
        View fabAgregar = view.findViewById(R.id.fabAgregarProducto);

        //Configuración del RecyclerView.
        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductoAdapter(this);
        rvProductos.setAdapter(adapter);

        //Funcionalidad del buscador (Filtro en tiempo real).
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    filtrarProductos(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //Evento del FAB (Agregar).
        fabAgregar.setOnClickListener(v -> abrirDialogoFormulario(null));
        cargarProductos();
        return view;
    }

    private void cargarProductos() {
        repository.obternerTodos(productos ->
                requireActivity().runOnUiThread(() -> adapter.setProductos(productos))
    );
    }

    private void filtrarProductos(String texto) {
        String consulta = texto.trim();
        if (consulta.isEmpty()) {
            cargarProductos();
        } else {
            repository.buscar(consulta, productos ->
                    requireActivity().runOnUiThread(() -> adapter.setProductos(productos))
                    );
        }
    }

    //---- Implementaciones de los clics del Adaptador ---
    @Override
    public void onProductoClick(Producto producto) {
        // Buscar la categoría asociada en segundo plano para obtener su nombre real
        repository.obtenerCategorias(listaCategorias -> {
            String nombreCategoria = "Desconocida";

            // Buscar cuál categoría coincide con el ID del producto
            for (Categoria cat : listaCategorias) {
                if (cat.getId() == producto.getCategoryId()) {
                    nombreCategoria = cat.getNombre();
                    break;
                }
            }

            // Una vez encontrado el nombre, empaquetamos y navegamos en el hilo principal
            String finalNombreCategoria = nombreCategoria;
            requireActivity().runOnUiThread(() -> {
                Bundle bundle = new Bundle();
                bundle.putString("nombre", producto.getNombre());
                bundle.putDouble("precio", producto.getPrecio());
                bundle.putInt("stock", producto.getStock());
                bundle.putString("imagen", producto.getImagen());
                bundle.putString("categoria_nombre", finalNombreCategoria);

                DetalleProductoFragment detalleFragment = new DetalleProductoFragment();
                detalleFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, detalleFragment)
                        .addToBackStack(null)
                        .commit();
            });
        });
    }

    @Override
    public void onEditarClick(Producto producto) {
        abrirDialogoFormulario(producto);
    }

    @Override
    public void onEliminarClick(Producto producto) {
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Eliminar producto?")
                .setMessage("¿Desea eliminar "+ producto.getNombre() +"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    //Se ejecuta la eliminación lógica.
                    repository.eliminarLogico(producto.getId());
                    Toast.makeText(getContext(), "Producto eliminado.", Toast.LENGTH_SHORT).show();
                    cargarProductos(); //Refrescar la lista.
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void abrirDialogoFormulario(@Nullable Producto productoExistente) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_producto, null);

        TextView lblTitulo = dialogView.findViewById(R.id.lblTituloDialog);
        imgPrevisualizacionDialog = dialogView.findViewById(R.id.imgDialogProducto);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etDialogNombre);
        TextInputEditText etPrecio = dialogView.findViewById(R.id.etDialogPrecio);
        TextInputEditText etStock = dialogView.findViewById(R.id.etDialogStock);
        FrameLayout btnSeleccionarImg = dialogView.findViewById(R.id.frameSeleccionarImagen);
        Spinner spnCategorias = dialogView.findViewById(R.id.spnDialogCategoria); // ◄--- El nuevo Spinner

        // 1. Cargar las categorías de la base de datos al Spinner
        repository.obtenerCategorias(listaCategorias -> {
            if (getActivity() == null) return;

            getActivity().runOnUiThread(() -> {
                if (listaCategorias.isEmpty()) {
                    Toast.makeText(getContext(), "¡Atención! Primero debes crear una categoría", Toast.LENGTH_LONG).show();
                }

                // Adaptador personalizado anónimo para mostrar solo el nombre
                ArrayAdapter<Categoria> adapterSpinner = new ArrayAdapter<Categoria>(requireContext(), android.R.layout.simple_spinner_item, listaCategorias) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView tv = (TextView) super.getView(position, convertView, parent);
                        tv.setText(listaCategorias.get(position).getNombre()); // Muestra nombre
                        return tv;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                        tv.setText(listaCategorias.get(position).getNombre()); // Muestra nombre en despliegue
                        return tv;
                    }
                };

                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCategorias.setAdapter(adapterSpinner);

                // Si estamos EDITANDO, preseleccionar la categoría que ya tenía el producto
                if (productoExistente != null) {
                    for (int i = 0; i < listaCategorias.size(); i++) {
                        if (listaCategorias.get(i).getId() == productoExistente.getCategoryId()) {
                            spnCategorias.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });

        // Configurar click de galería de fotos
        btnSeleccionarImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galleryLauncher.launch(intent);
        });

        // Cargar datos si es edición
        if (productoExistente != null) {
            lblTitulo.setText("Modificar Producto");
            etNombre.setText(productoExistente.getNombre());
            etPrecio.setText(String.valueOf(productoExistente.getPrecio()));
            etStock.setText(String.valueOf(productoExistente.getStock()));
            uriImagenSeleccionada = productoExistente.getImagen();

            if (uriImagenSeleccionada != null && !uriImagenSeleccionada.isEmpty()) {
                imgPrevisualizacionDialog.setImageURI(Uri.parse(uriImagenSeleccionada));
            }
        } else {
            uriImagenSeleccionada = "";
        }

        // Construcción del Diálogo
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String precioStr = etPrecio.getText().toString().trim();
                    String stockStr = etStock.getText().toString().trim();

                    // Validar que se haya seleccionado una categoría real
                    Categoria categoriaSeleccionada = (Categoria) spnCategorias.getSelectedItem();

                    if (nombre.isEmpty() || precioStr.isEmpty() || stockStr.isEmpty() || categoriaSeleccionada == null) {
                        Toast.makeText(getContext(), "Por favor, completa todos los campos y selecciona una categoría", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double precio = Double.parseDouble(precioStr);
                    int stock = Integer.parseInt(stockStr);
                    int idCategoria = categoriaSeleccionada.getId(); // ◄--- CAPTURA EL ID REAL AQUÍ

                    if (productoExistente != null) {
                        // ACTUALIZAR
                        productoExistente.setNombre(nombre);
                        productoExistente.setPrecio(precio);
                        productoExistente.setStock(stock);
                        productoExistente.setImagen(uriImagenSeleccionada);
                        productoExistente.setCategoryId(idCategoria); // Guardar nuevo ID de categoría
                        repository.actualizar(productoExistente);
                        Toast.makeText(getContext(), "Producto actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        // CREAR con relación de clave foránea válida
                        Producto nuevoProducto = new Producto(nombre, precio, stock, uriImagenSeleccionada, idCategoria);
                        repository.insertar(nuevoProducto);
                        Toast.makeText(getContext(), "Producto creado exitosamente", Toast.LENGTH_SHORT).show();
                    }

                    // Esperar un breve instante para dar tiempo a Room de insertar y recargar la lista
                    new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(this::cargarProductos, 300);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    //Para capturar de manera asíncrona la ruta (Uri) de la galería.
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        // Otorgar permisos persistentes de lectura a la URI recibida
                        requireContext().getContentResolver().takePersistableUriPermission(
                                imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        uriImagenSeleccionada = imageUri.toString(); // Guardar como String para Room
                        if (imgPrevisualizacionDialog != null) {
                            imgPrevisualizacionDialog.setImageURI(imageUri); // Mostrar en la vista
                        }
                    }
                }
            }
    );
}