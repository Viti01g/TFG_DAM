package com.example.citas_medicas.dto; // O tu paquete de DTOs

// Puedes usar anotaciones de validación si tienes 'spring-boot-starter-validation'
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;

// Clase DTO para encapsular los datos necesarios para registrar un usuario
public class UserRegistrationRequestDto {

    // Nombre de usuario (opcionalmente validado con anotaciones)
    // @NotBlank(message = "El nombre de usuario no puede estar vacío")
    // @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

    // Email del usuario (opcionalmente validado con anotaciones)
    // @NotBlank(message = "El email no puede estar vacío")
    // @Email(message = "El formato del email no es válido")
    private String email;

    // Contraseña del usuario (opcionalmente validada con anotaciones)
    // @NotBlank(message = "La contraseña no puede estar vacía")
    // @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Rol del usuario como cadena de texto (ejemplo: "MEDICO", "PACIENTE", "ADMIN")
    // @NotBlank(message = "El rol no puede estar vacío")
    private String rol;

    // Constructor vacío (necesario para la deserialización JSON)
    public UserRegistrationRequestDto() {
    }

    // Constructor con todos los campos (útil para inicializar instancias)
    public UserRegistrationRequestDto(String username, String email, String password, String rol) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters para acceder y modificar los atributos
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}