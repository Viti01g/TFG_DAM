package com.example.citas_medicas.dto; // Asegúrate de que esté en tu paquete de DTOs

// Clase DTO para representar los datos necesarios para actualizar un usuario
public class UserUpdateRequestDto {

    private String username;    // El nuevo username (opcional, si se permite cambiar)
    private String email;       // El nuevo email (opcional)
    private String rol;         // El nuevo rol como String (ej. "MEDICO", "PACIENTE", "ADMIN")
                                // El servicio se encargará de convertirlo al enum Rol
    private String nuevaPassword; // Campo para la nueva contraseña (opcional)

    // Constructor vacío (útil para algunos frameworks de mapeo o para Jackson)
    public UserUpdateRequestDto() {
    }

    // Constructor con todos los campos (opcional, pero puede ser útil)
    public UserUpdateRequestDto(String username, String email, String rol, String nuevaPassword) {
        this.username = username;
        this.email = email;
        this.rol = rol;
        this.nuevaPassword = nuevaPassword;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }
}