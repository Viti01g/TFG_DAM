package com.example.citasfinalmedicas.activities // Ajusta el paquete si creaste la carpeta 'activities'

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.citasfinalmedicas.R
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.citasfinalmedicas.api.ApiService // Importa tu ApiService
import com.example.citasfinalmedicas.model.UserRegistrationRequest // Importa tu modelo de petición de registro
import com.example.citasfinalmedicas.network.RetrofitClient // Importa tu RetrofitClient
import kotlinx.coroutines.launch

// Esta clase representa la actividad de registro de usuarios.
// Permite a los usuarios crear una cuenta proporcionando sus datos y seleccionando un rol.
class RegisterActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz.
    private lateinit var regUsernameEditText: EditText
    private lateinit var regEmailEditText: EditText
    private lateinit var regPasswordEditText: EditText
    private lateinit var regConfirmPasswordEditText: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var submitRegisterButton: Button

    private lateinit var apiService: ApiService // Instancia de tu ApiService para realizar solicitudes al backend.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) // Establece el layout XML para esta actividad.

        // Inicialización de las vistas.
        regUsernameEditText = findViewById(R.id.regUsernameEditText)
        regEmailEditText = findViewById(R.id.regEmailEditText)
        regPasswordEditText = findViewById(R.id.regPasswordEditText)
        regConfirmPasswordEditText = findViewById(R.id.regConfirmPasswordEditText)
        roleSpinner = findViewById(R.id.roleSpinner)
        submitRegisterButton = findViewById(R.id.submitRegisterButton)

        // Configuración del Spinner de roles con opciones predefinidas.
        ArrayAdapter.createFromResource(
            this,
            R.array.roles_array, // Definido en res/values/strings.xml.
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter
        }

        // Inicialización del ApiService usando RetrofitClient.
        apiService = RetrofitClient.apiService

        // Configuración del listener para el botón de registro.
        submitRegisterButton.setOnClickListener {
            performRegistration() // Llama al método para realizar el registro.
        }
    }

    // Método para realizar el registro de usuarios.
    private fun performRegistration() {
        val username = regUsernameEditText.text.toString().trim() // Obtiene el nombre de usuario.
        val email = regEmailEditText.text.toString().trim() // Obtiene el correo electrónico.
        val password = regPasswordEditText.text.toString().trim() // Obtiene la contraseña.
        val confirmPassword = regConfirmPasswordEditText.text.toString().trim() // Obtiene la confirmación de la contraseña.
        val role = roleSpinner.selectedItem.toString() // Obtiene el rol seleccionado.

        // Validación de campos vacíos.
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de contraseñas coincidentes.
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // Convierte el rol a mayúsculas si el backend lo requiere.
        val roleToSend = role.uppercase()

        // Crea una solicitud de registro con los datos proporcionados.
        val registrationRequest = UserRegistrationRequest(username, email, password, roleToSend)

        // Realiza la solicitud de registro en un hilo separado usando coroutines.
        lifecycleScope.launch {
            try {
                val response = apiService.registerUser(registrationRequest) // Llama al servicio API.

                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                    // Opcional: Vuelve a la pantalla de inicio de sesión después del registro exitoso.
                    finish() // Cierra RegisterActivity y vuelve a la anterior (LoginActivity).
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido en el registro"
                    Toast.makeText(this@RegisterActivity, "Error en el registro: ${response.code()} - $errorMessage", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Excepción al registrar usuario: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace() // Imprime la excepción para depuración.
            }
        }
    }
}