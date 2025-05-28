package com.example.citas_medicas.controller;

import com.example.citas_medicas.model.Rol;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.service.UserService;
import com.example.citas_medicas.dto.UserViewDto;
import com.example.citas_medicas.dto.UserUpdateRequestDto;
import com.example.citas_medicas.dto.UserRegistrationRequestDto;
import com.example.citas_medicas.exception.UserNotFoundException;
import com.example.citas_medicas.exception.OperationNotAllowedException;
import com.example.citas_medicas.exception.DataConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // Helper para obtener el usuario autenticado desde el contexto de seguridad
    private Usuario getCurrentUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new OperationNotAllowedException("Acceso no autenticado.");
        }
        String currentUsername = authentication.getName();
        return userService.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("Usuario autenticado '" + currentUsername + "' no encontrado."));
    }

    // Crear un nuevo usuario por un ADMIN_SUPREMO
    @PostMapping("/users/crear")
    public ResponseEntity<?> crearUsuarioPorAdminSupremo(@RequestBody UserRegistrationRequestDto userData) {
        try {
            Rol rolAsignar = Rol.valueOf(userData.getRol().trim().toUpperCase());
            Usuario nuevoUsuario = userService.registrarPersonalPorAdmin(
                userData.getUsername(),
                userData.getEmail(),
                userData.getPassword(),
                rolAsignar
            );
            UserViewDto usuarioDto = new UserViewDto(nuevoUsuario.getId(), nuevoUsuario.getUsername(), nuevoUsuario.getEmail(), nuevoUsuario.getRol());
            return new ResponseEntity<>(usuarioDto, HttpStatus.CREATED);
        } catch (DataConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (OperationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Rol inválido.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al crear el usuario.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener usuarios por rol
    @GetMapping("/users")
    public ResponseEntity<?> getUsersByRole(@RequestParam String role) {
        try {
            Rol requestedRol = Rol.valueOf(role.trim().toUpperCase());
            List<Usuario> usuarios = userService.getUsersByRole(requestedRol);
            List<UserViewDto> usuariosDto = usuarios.stream()
                .map(u -> new UserViewDto(u.getId(), u.getUsername(), u.getEmail(), u.getRol()))
                .collect(Collectors.toList());
            return new ResponseEntity<>(usuariosDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Rol inválido.", HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener un usuario por su ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            Usuario usuario = userService.getUserById(userId);
            UserViewDto usuarioDto = new UserViewDto(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getRol());
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un usuario
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequestDto userUpdateRequest) {
        try {
            Usuario usuarioActualizado = userService.updateUserFromAdmin(userId, userUpdateRequest);
            UserViewDto usuarioDto = new UserViewDto(usuarioActualizado.getId(), usuarioActualizado.getUsername(), usuarioActualizado.getEmail(), usuarioActualizado.getRol());
            return new ResponseEntity<>(usuarioDto, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (OperationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("Usuario eliminado exitosamente.", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OperationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}