package com.example.gyminventory.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gyminventory.MainActivity;
import com.example.gyminventory.data.entity.User;
import com.example.gyminventory.data.repository.UserRepository;
import com.example.gyminventory.databinding.ActivityLoginBinding;
import com.example.gyminventory.utils.SessionManager;

// Gestiona el inicio de sesión de usuarios y el acceso a la aplicación.
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);

        // Inicializa ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater()); // Android construye toda la vista
        setContentView(binding.getRoot()); // Muestra la vista principal de activity_login.xml"

        // Inicializa acceso a Room
        userRepository = new UserRepository(this);

        // Inicializa manejo de sesión
        sessionManager = new SessionManager(this);

        // Si ya existe una sesión activa, entrar directamente al sistema
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Evento del botón Iniciar Sesión
        binding.btnLogin.setOnClickListener(v-> loginUser());

        // Evento para abrir la pantalla de registro
        binding.btnGoRegister.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

    }

    // Valida credenciales y permite el acceso al sistema
    public void loginUser () {
        String correo = binding.etCorreo.getText().toString().trim(); // Permite acceder a los componentes del XML sin usar findViewById()
        String password = binding.etPassword.getText().toString().trim();

        // Validar correo vacío
        if (TextUtils.isEmpty(correo)) {
            binding.etCorreo.setError("Ingrese un correo");
            binding.etCorreo.requestFocus();
            return;
        }

        // Validar formato del correo
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.etCorreo.setError("Correo inválido");
            binding.etCorreo.requestFocus();
            return;
        }

        // Validar contraseña vacía
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Ingrese una contraseña");
            binding.etPassword.requestFocus();
            return;
        }

        // Buscar usuario en Room
        User user = userRepository.login(correo, password);

        // Si no existe coincidencia
        if (user == null) {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar información de sesión
        sessionManager.saveSession(user.getNombre(), user.getCorreo());

        Toast.makeText(this, "Bienvenido " + user.getNombre(), Toast.LENGTH_SHORT).show();

        // Abrir pantalla principal
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

        finish();

    }
}
