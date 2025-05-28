package com.example.citas_medicas.model;

import jakarta.persistence.*;

// Marca esta clase como una entidad JPA que se mapeará a una tabla en la base de datos
@Entity
public class Usuario {

    // Define el campo 'id' como clave primaria con generación automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Campo para el nombre de usuario
    private String username;

    // Campo para el correo electrónico
    private String email;

    // Campo para la contraseña
    private String password;

    // Campo para el rol del usuario, almacenado como texto en la base de datos
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Constructor vacío requerido por JPA
    public Usuario() {
    }

    // Constructor para inicializar un usuario con sus atributos
    public Usuario(String username, String email, String password, Rol rol) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Métodos getter y setter para acceder y modificar los atributos
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Método para representar el objeto como una cadena de texto
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                '}';
    }
}