// Archivo: com/example/citasmedicasapp/model/UserRegistrationResponse.kt
package com.example.citasfinalmedicas.model // <--- ¡Asegúrate de que este paquete sea correcto!

import com.google.gson.annotations.SerializedName

// Clase de datos que representa la respuesta del backend tras registrar un usuario.
// Contiene información como el ID, nombre de usuario, correo electrónico y rol del usuario.
data class UserRegistrationResponse(
    @SerializedName("id") val id: Long, // ID único del usuario registrado.
    @SerializedName("username") val username: String, // Nombre de usuario registrado.
    @SerializedName("email") val email: String,       // Correo electrónico del usuario registrado.
    @SerializedName("rol") val rol: String,           // Rol del usuario registrado (ej. "ADMIN", "MEDICO", "PACIENTE").
    @SerializedName("password") val password: String  // Contraseña del usuario (¡Precaución! No es seguro devolverla en producción).
)