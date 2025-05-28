package com.example.citasfinalmedicas.model // Asegúrate de que el paquete sea correcto

import com.google.gson.annotations.SerializedName

// Clase de datos que representa una solicitud de inicio de sesión.
// Contiene el nombre de usuario y la contraseña que se envían al backend.
data class LoginRequest(
    @SerializedName("username")
    val username: String, // Nombre de usuario del cliente que intenta iniciar sesión.
    @SerializedName("password")
    val password: String // Contraseña del cliente que intenta iniciar sesión.
)