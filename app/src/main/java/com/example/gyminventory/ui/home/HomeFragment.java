package com.example.gyminventory.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gyminventory.R;
import com.example.gyminventory.data.repository.CategoriaRepository;
import com.example.gyminventory.data.repository.ProductoRepository;
import com.example.gyminventory.databinding.FragmentHomeBinding;
import com.example.gyminventory.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Muestra un resumen inicial con bienvenida y estadísticas generales del inventario
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false );
        loadWelcomeMessage();
        loadStatistics();
        setupCardNavigation();

        return binding.getRoot();
    }

    // Muestra el nombre del usuario logueado
    private void loadWelcomeMessage () {
        SessionManager sessionManager = new SessionManager(requireContext());
        binding.tvWelcome.setText("Bienvenido/a, " + sessionManager.getUserName());
    }

    // Carga estadísticas generales del sistema
    // Carga estadísticas generales del sistema
    private void loadStatistics() {

        CategoriaRepository categoriaRepository = new CategoriaRepository(requireContext());

        ProductoRepository productoRepository = new ProductoRepository(requireContext());

        // Obtener cantidad total de categorías
        int totalCategorias = categoriaRepository.getTotalCategorias();

        // Obtener cantidad total de productos
        int totalProductos = productoRepository.getTotalProductos();

        // Mostrar estadísticas
        binding.tvProductsCount.setText(String.valueOf(totalProductos));
        binding.tvCategoriesCount.setText(String.valueOf(totalCategorias));
    }

    // Configura la navegación desde las tarjetas del dashboard
    private void setupCardNavigation() {
        // Al pulsar la tarjeta de productos, selecciona la opción Productos del BottomNavigation y navega al ProductsFragment
        binding.cardProducts.setOnClickListener(v -> {

            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);

            bottomNav.setSelectedItemId(R.id.nav_products);

        });

        // Al pulsar la tarjeta de categorías, selecciona la opción Categorías del BottomNavigation y navega al CategoriesFragment
        binding.cardCategories.setOnClickListener(v -> {

            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);

            bottomNav.setSelectedItemId(R.id.nav_categories);

        });
    }

    @Override
    public void onDestroyView() { // evita fugas de memoria al liberar el binding cuando el view ya no existe
        super.onDestroyView();

        binding = null;
    }
}