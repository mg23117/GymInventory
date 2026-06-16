package com.example.gyminventory;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import com.example.gyminventory.databinding.ActivityMainBinding;
import com.example.gyminventory.utils.ThemeManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.applyTheme(this); // Aplicamos el tema antes de crear la vista
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // La configuración para la toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Obtenemos el navcontroller desde el navhostfragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            throw new RuntimeException("NavHostFragment no encontrado");
        }

        navController = navHostFragment.getNavController();

        // Configuración de AppBarConfiguration para relacionarlo con el DrawerLayout con los destinos top level
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_categories,
                R.id.nav_products,
                R.id.nav_settings)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        // Vinculamos la toolbar con el navcontroller
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        // Vinculamos NavigationView (Drawer) con el navcontroller
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Vinculamos ButtomNavigation con navcontroller
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        // Manejo del botón de retroceso
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Si el drawer está abierto, cerrarlo y consumir el evento
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (!navController.popBackStack()) {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}