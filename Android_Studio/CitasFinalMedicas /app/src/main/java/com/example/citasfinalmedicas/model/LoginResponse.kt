package com.example.citasfinalmedicas.model

import com.google.gson.annotations.SerializedName

// Clase de datos que representa la respuesta de inicio de sesión del backend.
// Contiene información como el mensaje, token, ID de usuario y rol del usuario.
data class LoginResponse(
    @SerializedName("message")
    val message: String?, // Mensaje de respuesta del servidor, puede ser nulo si no se proporciona.
    @SerializedName("token") // Token de autenticación devuelto por el servidor.
    val token: String?,     // Puede ser nulo si el servidor no lo proporciona en ciertos casos.
    @SerializedName("userId") // ID único del usuario autenticado.
    val userId: Long?,      // Puede ser nulo si no se incluye en la respuesta.
    @SerializedName("rol") // Rol del usuario autenticado (ej. "ADMIN", "MEDICO", "PACIENTE").
    val role: String?        // Puede ser nulo si no se especifica en la respuesta.
)