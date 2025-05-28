package com.example.citasfinalmedicas.model

import com.example.citasfinalmedicas.model.Rol
import com.google.gson.annotations.SerializedName

// Clase de datos que representa un usuario en el sistema.
// Contiene información como el ID, nombre de usuario, correo electrónico, contraseña y rol.
data class Usuario(
    @SerializedName("id") val id: Int?, // ID único del usuario, puede ser nulo si es un nuevo usuario.
    @SerializedName("username") val username: String, // Nombre de usuario.
    @SerializedName("email") val email: String,       // Correo electrónico del usuario.
    @SerializedName("password") val password: String, // Contraseña del usuario. **Nota:** No debe enviarse en respuestas de la API por seguridad.
    @SerializedName("rol") val rol: Rol              // Rol del usuario, representado por el enum Rol.
)