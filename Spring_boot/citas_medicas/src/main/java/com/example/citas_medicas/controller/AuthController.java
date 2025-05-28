package com.example.citas_medicas.controller;

import com.example.citas_medicas.model.Rol;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.repository.UsuarioRepository;
import com.example.citas_medicas.service.UserService;
import com.example.citas_medicas.dto.LoginResponseDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registro de un nuevo usuario
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> registroData) {
        String username = registroData.get("username");
        String email = registroData.get("email");
        String password = registroData.get("password");
        String rolStr = registroData.get("rol");

        if (username == null || email == null || password == null || rolStr == null) {
            return new ResponseEntity<>("Todos los campos (username, email, password, rol) son requeridos", HttpStatus.BAD_REQUEST);
        }

        Rol rol;
        try {
            rol = Rol.valueOf(rolStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Rol inválido. Los roles permitidos son: PACIENTE, MEDICO", HttpStatus.BAD_REQUEST);
        }

        try {
            Usuario nuevoUsuario = userService.registrarUsuario(username, email, password, rol);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    // Autenticación de usuario
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || password == null) {
            return new ResponseEntity<>("Username y password son requeridos para el login", HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
            if (usuarioOptional.isPresent()) {
                Usuario usuarioAutenticado = usuarioOptional.get();
                String token = "TOKEN_JWT_GENERADO_POR_SPRING_BOOT"; // Reemplaza con tu lógica de generación de token JWT
                return ResponseEntity.ok(new LoginResponseDto(
                    "Login exitoso para el usuario: " + username,
                    token,
                    usuarioAutenticado.getId().longValue(),
                    usuarioAutenticado.getRol().name()
                ));
            } else {
                return new ResponseEntity<>("Error interno: Usuario no encontrado después de autenticación.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Credenciales inválidas o error inesperado.", HttpStatus.UNAUTHORIZED);
        }
    }
}