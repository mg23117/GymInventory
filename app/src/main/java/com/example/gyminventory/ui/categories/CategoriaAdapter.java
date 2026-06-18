package com.example.gyminventory.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gyminventory.R;
import com.example.gyminventory.data.entity.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> listaCategorias = new ArrayList<>();
    private final OnCategoriaClickListener listener;

    // Interfaz para enviar las acciones al Fragment
    public interface OnCategoriaClickListener {
        void onEditarClick(Categoria categoria);
        void onEliminarClick(Categoria categoria);
    }

    public CategoriaAdapter(OnCategoriaClickListener listener) {
        this.listener = listener;
    }

    // Actualiza la lista del RecyclerView
    public void setCategorias(List<Categoria> categorias) {
        this.listaCategorias = categorias;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);
        holder.bind(categoria, listener);
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView txtIcono, txtNombre;
        ImageButton btnMenu;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIcono = itemView.findViewById(R.id.txtIconoCategoria);
            txtNombre = itemView.findViewById(R.id.txtNombreCategoria);
            btnMenu = itemView.findViewById(R.id.btnMenuCategoria);
        }

        public void bind(final Categoria categoria, final OnCategoriaClickListener listener) {
            txtIcono.setText(categoria.getIcono());
            txtNombre.setText(categoria.getNombre());

            // Menú de tres puntos para editar o eliminar
            btnMenu.setOnClickListener(v -> {
                Context context = v.getContext();
                PopupMenu popup = new PopupMenu(context, btnMenu);

                popup.getMenu().add("Editar");
                popup.getMenu().add("Eliminar");

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Editar")) {
                        listener.onEditarClick(categoria);
                    } else if (item.getTitle().equals("Eliminar")) {
                        listener.onEliminarClick(categoria);
                    }
                    return true;
                });

                popup.show();
            });
        }
    }
}