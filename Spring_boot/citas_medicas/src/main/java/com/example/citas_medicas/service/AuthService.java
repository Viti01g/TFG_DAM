package com.example.citas_medicas.service;

/*
 * import com.example.citamedicabackend.model.Usuario; import
 * com.example.citamedicabackend.repository.UsuarioRepository; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.stereotype.Service;
 * 
 * import java.util.Optional; import java.util.regex.Matcher; import
 * java.util.regex.Pattern;
 * 
 * @Service public class AuthService {
 * 
 * @Autowired private UsuarioRepository usuarioRepository;
 * 
 * @Autowired private PasswordEncoder passwordEncoder;
 * 
 * public Optional<Usuario> registrarUsuario(String username, String password,
 * String dni, String nombreCompleto, Usuario.RolUsuario role, String
 * especialidad) { if (usuarioRepository.existsByUsername(username)) { return
 * Optional.empty(); // El nombre de usuario ya existe }
 * 
 * // Validaciones adicionales if (!isValidUsername(username)) { return
 * Optional.empty(); // Nombre de usuario inválido } if
 * (!isValidPassword(password)) { return Optional.empty(); // Contraseña
 * inválida } if (!isValidDni(dni)) { return Optional.empty(); // DNI inválido }
 * if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) { return
 * Optional.empty(); // Nombre completo inválido } // Podríamos añadir más
 * validaciones para la especialidad si es necesario
 * 
 * Usuario nuevoUsuario = new Usuario(); nuevoUsuario.setUsername(username);
 * nuevoUsuario.setPassword(passwordEncoder.encode(password)); // Encriptar la
 * contraseña nuevoUsuario.setDni(dni);
 * nuevoUsuario.setNombreCompleto(nombreCompleto); nuevoUsuario.setRole(role);
 * nuevoUsuario.setEspecialidad(especialidad);
 * 
 * return Optional.of(usuarioRepository.save(nuevoUsuario)); }
 * 
 * public Optional<Usuario> iniciarSesion(String username, String password) {
 * Optional<Usuario> usuarioOptional =
 * usuarioRepository.findByUsername(username); if (usuarioOptional.isPresent())
 * { Usuario usuario = usuarioOptional.get(); if
 * (passwordEncoder.matches(password, usuario.getPassword())) { return
 * Optional.of(usuario); // Inicio de sesión exitoso } } return
 * Optional.empty(); // Credenciales inválidas }
 * 
 * // Métodos de validación (podríamos extraerlos a una clase utilitaria)
 * private boolean isValidUsername(String username) { // Ejemplo: longitud
 * mínima y máxima, caracteres alfanuméricos y algunos especiales return
 * username != null && username.matches("^[a-zA-Z0-9._-]{3,20}$"); }
 * 
 * private boolean isValidPassword(String password) { // Ejemplo: longitud
 * mínima, al menos un carácter especial return password != null &&
 * password.length() >= 8 &&
 * Pattern.compile("[^a-zA-Z0-9]").matcher(password).find(); }
 * 
 * private boolean isValidDni(String dni) { // Ejemplo: validación básica del
 * formato (podría ser más específica según el país) return dni != null &&
 * dni.matches("^[0-9]{8}[A-Za-z]$"); } }
 */