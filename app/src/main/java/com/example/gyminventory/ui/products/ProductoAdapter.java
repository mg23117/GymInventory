package com.example.gyminventory.ui.products;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gyminventory.R;
import com.example.gyminventory.data.entity.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos = new ArrayList<>();
    private final OnProductoClickListener listener;

    //Interfaz para delegar acciones al fragment.
    public interface OnProductoClickListener {
        void onProductoClick(Producto producto);
        void onEditarClick(Producto producto);
        void onEliminarClick(Producto producto);
    }

    public ProductoAdapter(OnProductoClickListener listener) {
        this.listener = listener;
    }

    public void setProductos(List<Producto> productos) {
        this.listaProductos = productos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtPrecio, txtStock;
        ImageButton btnMenu;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.imgProductoItem);
            txtNombre = itemView.findViewById(R.id.txtNombreItem);
            txtPrecio = itemView.findViewById(R.id.txtPrecioItem);
            txtStock = itemView.findViewById(R.id.txtStockItem);
            btnMenu = itemView.findViewById(R.id.btnMenuOpciones);
        }

        public void bind(final Producto producto, final OnProductoClickListener listener) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.format("$%.2f", producto.getPrecio()));
            txtStock.setText("Stock: " + producto.getStock() + " uds");

            //Cargamos la imagen local mediante URI si existe.
            if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                imgProducto.setImageURI(Uri.parse(producto.getImagen()));
            } else {
                imgProducto.setImageResource(R.drawable.foto);
            }

            //Pulsar en la tarjeta abre los detalles.
            itemView.setOnClickListener(v -> listener.onProductoClick(producto));

            //Menú desplegable flotante de acciones.
            btnMenu.setOnClickListener(v -> {
                Context context = v.getContext();
                PopupMenu popup = new PopupMenu(context, btnMenu);
                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Editar")) {
                        listener.onEditarClick(producto);
                    } else if (item.getTitle().equals("Eliminar")) {
                        listener.onEliminarClick(producto);
                    }
                    return true;
                });
                popup.show();
            });
        }
    }

}
