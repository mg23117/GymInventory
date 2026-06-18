package com.example.gyminventory.ui.products;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.gyminventory.R;
import com.google.android.material.button.MaterialButton;

public class DetalleProductoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_producto, container, false);

        MaterialButton btnRegresar = view.findViewById(R.id.btnRegresarDetalle);
        ImageView imgProducto = view.findViewById(R.id.imgDetalleProducto);
        TextView txtNombre = view.findViewById(R.id.txtDetalleNombre);
        TextView txtCategoria = view.findViewById(R.id.txtDetalleCategoria);
        TextView txtPrecio = view.findViewById(R.id.txtDetallePrecio);
        TextView txtStock = view.findViewById(R.id.txtDetalleStock);

        //Botón regresar.
        btnRegresar.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Recuperar los datos enviados desde la lista
        if (getArguments() != null) {
            txtNombre.setText(getArguments().getString("nombre", ""));

            String nombreCat = getArguments().getString("categoria_nombre", "Sin categoría");
            txtCategoria.setText("Categoría: " + nombreCat);

            txtPrecio.setText(String.format("$%.2f", getArguments().getDouble("precio", 0.0)));
            txtStock.setText("Unidades disponibles: " + getArguments().getInt("stock", 0));

            String stringUri = getArguments().getString("imagen", "");
            if (stringUri != null && !stringUri.isEmpty()) {
                imgProducto.setImageURI(Uri.parse(stringUri));
                imgProducto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imgProducto.setImageResource(R.drawable.foto);
                imgProducto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imgProducto.setPadding(64,64,64,64);
            }
        }

        return view;
    }
}