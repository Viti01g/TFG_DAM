package com.example.citasfinalmedicas.model

import com.google.gson.annotations.SerializedName

// Clase de datos que representa una solicitud de registro de usuario.
// Contiene información como el nombre de usuario, correo electrónico, contraseña y rol.
data class UserRegistrationRequest(
    @SerializedName("username") val username: String, // Nombre de usuario para el registro.
    @SerializedName("email") val email: String,       // Correo electrónico del usuario.
    @SerializedName("password") val password: String, // Contraseña para la cuenta del usuario.
    @SerializedName("rol") val rol: String            // Rol del usuario (ej. "ADMIN", "MEDICO", "PACIENTE").
)