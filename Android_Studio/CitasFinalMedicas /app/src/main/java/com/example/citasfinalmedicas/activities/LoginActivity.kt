package com.example.citasfinalmedicas.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences // Para almacenar datos persistentes
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.citasfinalmedicas.MainActivity
import com.example.citasfinalmedicas.R
import com.example.citasfinalmedicas.api.ApiService
import com.example.citasfinalmedicas.model.LoginRequest
import com.example.citasfinalmedicas.model.Rol // Enum para los roles de usuario
import com.example.citasfinalmedicas.network.RetrofitClient
import kotlinx.coroutines.launch

// Esta clase representa la actividad de inicio de sesión.
// Permite al usuario ingresar sus credenciales y acceder a la aplicación.
class LoginActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz y el servicio API.
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var apiService: ApiService

    companion object {
        // Constantes para las preferencias compartidas (SharedPreferences).
        const val AUTH_PREFS_NAME = "auth_prefs"
        const val KEY_USER_ID = "user_id"
        const val KEY_USER_ROLE = "user_role"
        const val KEY_USERNAME_FOR_BASIC_AUTH = "username_basic"
        const val KEY_PASSWORD_FOR_BASIC_AUTH = "password_basic"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Establece el diseño de la actividad.

        // Inicialización de los elementos de la interfaz.
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        // Inicialización del servicio API usando Retrofit.
        apiService = RetrofitClient.apiService

        // Configuración del listener para el botón de inicio de sesión.
        loginButton.setOnClickListener {
            performLogin() // Llama al método para realizar el inicio de sesión.
        }

        // Configuración del listener para el botón de registro.
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent) // Lanza la actividad de registro.
        }
    }

    // Método para realizar el inicio de sesión.
    private fun performLogin() {
        val currentUsername = usernameEditText.text.toString().trim() // Obtiene el nombre de usuario.
        val currentPassword = passwordEditText.text.toString().trim() // Obtiene la contraseña.

        // Validación de campos vacíos.
        if (currentUsername.isEmpty() || currentPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce tu nombre de usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        loginButton.isEnabled = false // Desactiva el botón mientras se realiza el login.

        lifecycleScope.launch {
            try {
                // Crea una solicitud de inicio de sesión con las credenciales del usuario.
                val loginRequest = LoginRequest(currentUsername, currentPassword)
                val response = apiService.loginUser(loginRequest) // Llama al servicio API.

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    // Verifica que la respuesta del servidor contenga los datos necesarios.
                    if (loginResponse != null && loginResponse.userId != null && loginResponse.role != null && !loginResponse.role.isBlank()) {
                        val sharedPreferences: SharedPreferences = getSharedPreferences(
                            AUTH_PREFS_NAME,
                            Context.MODE_PRIVATE
                        )

                        val roleFromServer = loginResponse.role.trim().toUpperCase()

                        // Guarda las credenciales y datos del usuario en SharedPreferences.
                        with(sharedPreferences.edit()) {
                            putString(KEY_USERNAME_FOR_BASIC_AUTH, currentUsername)
                            putString(KEY_PASSWORD_FOR_BASIC_AUTH, currentPassword)
                            putLong(KEY_USER_ID, loginResponse.userId)
                            putString(KEY_USER_ROLE, roleFromServer)
                            apply()
                        }

                        Toast.makeText(this@LoginActivity, "Login exitoso.", Toast.LENGTH_SHORT).show()
                        Log.d("LoginActivity", "Credenciales guardadas: $currentUsername, Rol: $roleFromServer")

                        // Redirige al usuario a la actividad correspondiente según su rol.
                        val intent: Intent? = when (roleFromServer) {
                            Rol.MEDICO.name -> Intent(this@LoginActivity, MedicoActivity::class.java)
                            Rol.PACIENTE.name -> Intent(this@LoginActivity, PacienteActivity::class.java)
                            Rol.ADMIN_SUPREMO.name -> Intent(this@LoginActivity, AdminSupremoActivity::class.java)
                            Rol.ADMIN.name -> Intent(this@LoginActivity, AdminActivity::class.java)
                            else -> {
                                Toast.makeText(this@LoginActivity, "Rol desconocido: $roleFromServer.", Toast.LENGTH_LONG).show()
                                Log.e("LoginActivity", "Rol desconocido: $roleFromServer")
                                null
                            }
                        }

                        // Si el intent es válido, inicia la actividad y finaliza la actual.
                        if (intent != null) {
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            loginButton.isEnabled = true
                        }

                    } else {
                        Toast.makeText(this@LoginActivity, "Respuesta incompleta del servidor.", Toast.LENGTH_LONG).show()
                        Log.w("LoginActivity", "Datos faltantes en la respuesta: $loginResponse")
                        loginButton.isEnabled = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@LoginActivity, "Error al iniciar sesión (${response.code()}): ${errorBody ?: "Error desconocido"}", Toast.LENGTH_LONG).show()
                    Log.e("LoginActivity", "Error API (${response.code()}): $errorBody")
                    loginButton.isEnabled = true
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Excepción: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("LoginActivity", "Excepción durante el login", e)
                loginButton.isEnabled = true
            }
        }
    }
}