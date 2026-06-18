package com.example.gyminventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.widget.Toolbar;

import com.example.gyminventory.databinding.ActivityMainBinding;
import com.example.gyminventory.ui.auth.LoginActivity;
import com.example.gyminventory.utils.SessionManager;
import com.example.gyminventory.utils.ThemeManager;
import com.google.android.material.navigation.NavigationView;

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

        // Configuración del listener personalizado para el NavigationView
        // Para poder manejar items que no están en la grafica de navegacion (como el logout)
        setupNavigationDrawerListener();

        // Cargar información del usuario en el Drawer
        loadUserData();

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

    // Configuración del listener personalizado para el NavigationView
    private void setupNavigationDrawerListener(){
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem){

                // Manejar aquí el item de logout (o cualquier otro item)
                if (menuItem.getItemId() == R.id.nav_logout){
                    // cerramos el drawer
                    binding.drawerLayout.closeDrawer(GravityCompat.START);

                    // Limpiar datos de sesión
                    SessionManager sessionManager = new SessionManager(MainActivity.this);

                    sessionManager.logout();

                    // Ir nuevamente al Login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                    // Limpiar historial de actividades
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);

                    finish();

                    return true;
                }

                // Para otros items delegamos en NavigationUi
                // Para menejar la navegación y el resultado del item seleccionado
                boolean handled = NavigationUI.onNavDestinationSelected(menuItem, navController);

                if(handled){
                    // Cerramos el drawer luego de navegar
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                }

                return handled;
            }
        });
    }

    // Muestra el nombre y correo del usuario logueado en el header del Drawer
    private void loadUserData() {

        // Obtiene la vista header del Navigation Drawer
        View headerView = binding.navView.getHeaderView(0); // Proporcionar el primer header del NavigationView, por eso index 0

        // Referencias a los TextView del header
        TextView tvUserName = headerView.findViewById(R.id.tv_user_name);
        TextView tvUserEmail = headerView.findViewById(R.id.tv_user_email);

        // Obtiene datos almacenados en SharedPreferences
        SessionManager sessionManager = new SessionManager(this);

        // Muestra nombre y correo del usuario actual
        tvUserName.setText(sessionManager.getUserName());

        tvUserEmail.setText(sessionManager.getEmail());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}