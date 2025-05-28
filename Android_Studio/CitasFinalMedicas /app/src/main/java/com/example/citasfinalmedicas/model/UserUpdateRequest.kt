package com.example.citasfinalmedicas.model // O tu paquete de modelos

import com.google.gson.annotations.SerializedName

// Clase de datos que representa una solicitud para actualizar los datos de un usuario.
// Solo se envían los campos que han cambiado; los demás se dejan como nulos.
data class UserUpdateRequest(
    @SerializedName("username")
    val username: String? = null, // Nuevo nombre de usuario, si se actualiza.

    @SerializedName("email")
    val email: String? = null, // Nuevo correo electrónico, si se actualiza.

    @SerializedName("rol") // Se envía el nombre del rol como String, ej. "MEDICO".
    val rol: String? = null, // Nuevo rol del usuario, si se actualiza.

    @SerializedName("nuevaPassword") // El backend se encarga de encriptar esta si se proporciona.
    val nuevaPassword: String? = null // Nueva contraseña, si se actualiza.
)