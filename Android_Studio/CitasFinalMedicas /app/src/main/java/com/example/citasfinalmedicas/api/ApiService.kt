package com.example.citasfinalmedicas.api

import com.example.citasfinalmedicas.model.LoginRequest
import com.example.citasfinalmedicas.model.LoginResponse
import com.example.citasfinalmedicas.model.UserRegistrationRequest
import com.example.citasfinalmedicas.model.UserRegistrationResponse
import com.example.citasfinalmedicas.model.HorarioDisponible
import com.example.citasfinalmedicas.model.Usuario
import com.example.citasfinalmedicas.model.UserUpdateRequest // Clase para actualizar usuarios.

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz que define los endpoints de la API.
// Cada método corresponde a una solicitud HTTP al backend.
interface ApiService {

    // Endpoint para registrar un nuevo usuario.
    @POST("/api/auth/registrar")
    suspend fun registerUser(@Body request: UserRegistrationRequest): Response<UserRegistrationResponse>

    // Endpoint para iniciar sesión.
    @POST("/api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    // Endpoint para crear un horario disponible para un médico.
    @POST("/api/medicos/{medicoId}/horarios")
    suspend fun crearHorario(
        @Path("medicoId") medicoId: Long, // ID del médico.
        @Body horarioData: Map<String, String> // Datos del horario en formato clave-valor.
    ): Response<HorarioDisponible>

    // --- Endpoints para Gestión de Usuarios por Admin ---

    // Endpoint para obtener una lista de usuarios según su rol.
    @GET("/api/admin/users")
    suspend fun getUsersByRole(
        @Query("role") roleName: String // Rol de los usuarios a buscar.
    ): Response<List<Usuario>> // Devuelve una lista de usuarios.

    // Endpoint para obtener los datos de un usuario por su ID.
    @GET("/api/admin/users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Long // ID del usuario.
    ): Response<Usuario> // Devuelve los datos del usuario.

    // Endpoint para actualizar los datos de un usuario.
    @PUT("/api/admin/users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: Long, // ID del usuario.
        @Body userData: UserUpdateRequest // Datos actualizados del usuario.
    ): Response<Usuario> // Devuelve el usuario actualizado.

    // Endpoint para eliminar un usuario por su ID.
    @DELETE("/api/admin/users/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Long // ID del usuario.
    ): Response<Unit> // Devuelve una respuesta vacía si la eliminación fue exitosa.
}