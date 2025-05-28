package com.example.citasfinalmedicas // Paquete del nuevo proyecto

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.citasfinalmedicas.model.UserRegistrationRequest // Importa del nuevo paquete
import com.example.citasfinalmedicas.network.RetrofitClient      // Importa del nuevo paquete
import com.example.citasfinalmedicas.ui.theme.CitasFinalMedicasTheme // <-- USA EL TEMA DEL NUEVO PROYECTO
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Usa el tema generado para tu nuevo proyecto "CitasFinalMedicas"
            CitasFinalMedicasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationScreenCitasFinalMedicas() // Renombrado para claridad
                }
            }
        }
    }
}

@Composable
fun RegistrationScreenCitasFinalMedicas(modifier: Modifier = Modifier) { // Renombrado
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val TAG = "RegistrationScreen"

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") } // Considera usar un DropdownMenu para roles
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Asumimos que RetrofitClient.apiService está disponible y configurado correctamente
    // y que UserRegistrationRequest y UserRegistrationResponse están definidos
    // en el paquete com.example.citasfinalmedicas.model

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro de Usuario (CitasFinalMedicas)", // Título actualizado
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField( // Para el rol, sería mejor un DropdownMenu o ExposedDropdownMenuBox
            value = rol,
            onValueChange = { rol = it.uppercase() }, // Convertir a mayúsculas si tu backend espera MEDICO/PACIENTE
            label = { Text("Rol (ej. MEDICO, PACIENTE)") }, // Actualizado para reflejar roles
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                if (username.isBlank() || email.isBlank() || rol.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                // Asegúrate que 'rol' sea uno de los valores esperados por tu backend, ej. "MEDICO" o "PACIENTE"
                if (rol != "MEDICO" && rol != "PACIENTE") { // Validación simple de rol
                    Toast.makeText(context, "Rol inválido. Use MEDICO o PACIENTE.", Toast.LENGTH_LONG).show()
                    return@Button
                }

                val userRequest = UserRegistrationRequest(username, email, password, rol) // Orden correcto según UserRegistrationRequest

                coroutineScope.launch {
                    try {
                        // Asegúrate que RetrofitClient y apiService estén correctamente configurados en el nuevo proyecto
                        val response = RetrofitClient.apiService.registerUser(userRequest)

                        if (response.isSuccessful) {
                            val registeredUser = response.body()
                            val message = "Usuario registrado: ${registeredUser?.username} (${registeredUser?.email}) con rol ${registeredUser?.rol}"
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            Log.d(TAG, message)
                            Log.d(TAG, "ID del usuario: ${registeredUser?.id}")

                            username = ""
                            email = ""
                            rol = ""
                            password = ""
                            // Aquí podrías navegar a la pantalla de Login
                            // Ejemplo: val intent = Intent(context, LoginActivity::class.java)
                            // context.startActivity(intent)
                            // (activity as? ComponentActivity)?.finish() // Si quieres cerrar esta activity

                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = "Error en el registro (${response.code()}): ${errorBody ?: "Error desconocido"}"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            Log.e(TAG, errorMessage)
                        }
                    } catch (e: Exception) {
                        val errorMessage = "Excepción al registrar: ${e.localizedMessage}"
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        Log.e(TAG, errorMessage, e)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Usuario")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewCitasFinalMedicas() { // Renombrado
    CitasFinalMedicasTheme {
        RegistrationScreenCitasFinalMedicas() // Renombrado
    }
}