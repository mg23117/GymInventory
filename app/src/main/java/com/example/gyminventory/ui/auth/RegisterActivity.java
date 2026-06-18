package com.example.gyminventory.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gyminventory.data.entity.User;
import com.example.gyminventory.databinding.ActivityRegisterBinding;
import com.example.gyminventory.data.repository.UserRepository;

// Gestiona el registro de nuevos usuarios y valida la información ingresada
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa ViewBinding
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializa acceso a usuarios
        userRepository = new UserRepository(this);

        // Registrar usuario
        binding.btnRegister.setOnClickListener(v -> registerUser());

        // Ir a Login
        binding.btnGoLogin.setOnClickListener(v-> {
            finish();
        });
    }

    // Valida y registra un nuevo usuario
    public void registerUser (){
        String nombre = binding.etNombre.getText().toString().trim();
        String correo = binding.etCorreo.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        // Validar nombre
        if (TextUtils.isEmpty(nombre)) {  //TextUtils es un metodo de android que ayuda a verificar si ens null o empty a la vez
            binding.etNombre.setError("Ingrese un nombre");
            binding.etNombre.requestFocus(); // Mueve el cursor hacia ese campo
            return;
        }

        // Validar correo
        if (TextUtils.isEmpty(correo)) {
            binding.etCorreo.setError("Ingrese un correo");
            binding.etCorreo.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) { // Patterns es una expresión de android que sirve para validar formato del correo
            binding.etCorreo.setError("Correo inválido");
            binding.etCorreo.requestFocus();
            return;
        }

        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.setError("Ingrese una contraseña");
            binding.etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            binding.etPassword.setError("Mínimo 6 caracteres");
            binding.etPassword.requestFocus();
            return;
        }

        // Confirmar contraseña
        if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Las contraseñas no coinciden");
            binding.etConfirmPassword.requestFocus();
            return;
        }

        // Verificar si el correo ya existe
        User existingUser = userRepository.getUserByEmail(correo);

        if (existingUser != null) {
            binding.etCorreo.setError("Este correo ya está registrado");
            binding.etCorreo.requestFocus();
            return;
        }

        // Crear usuario
        User user = new User(nombre, correo, password);

        // Guardar en Room
        userRepository.insert(user);

        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

        finish();
    }
}