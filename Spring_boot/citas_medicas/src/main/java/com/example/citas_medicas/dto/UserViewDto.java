package com.example.citas_medicas.dto; // Asegúrate de que esté en tu paquete de DTOs

import com.example.citas_medicas.model.Rol; // Importa tu enum Rol

// Clase DTO para representar la vista de un usuario
public class UserViewDto {
    private Integer id; // ID del usuario (puede ser Integer o Long según la entidad Usuario)
    private String username; // Nombre de usuario
    private String email; // Correo electrónico
    private Rol rol; // Rol del usuario (enum Rol)

    // Constructor para inicializar el DTO con los atributos del usuario
    public UserViewDto(Integer id, String username, String email, Rol rol) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rol = rol;
    }

    // Getters para acceder a los atributos (Jackson los utiliza para serializar)
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Rol getRol() {
        return rol;
    }

    // Setters para modificar los atributos (generalmente no necesarios para DTOs de solo lectura)
    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}