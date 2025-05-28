package com.example.citas_medicas.dto;

// Clase DTO para encapsular la respuesta de un inicio de sesión exitoso
public class LoginResponseDto {
    private String message;  // Mensaje de respuesta (ej. "Login exitoso")
    private String token;    // Campo para el token JWT
    private Long userId;     // Campo para el ID del usuario (puede ser Integer si usas Integer para los IDs)
    private String rol;      // Campo para el rol del usuario

    // Constructor con todos los campos
    public LoginResponseDto(String message, String token, Long userId, String rol) {
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.rol = rol;
    }

    // Getters para acceder a los atributos
    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRol() {
        return rol;
    }

    // Setters para modificar los atributos (puedes omitirlos si el objeto es de solo lectura)
    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}