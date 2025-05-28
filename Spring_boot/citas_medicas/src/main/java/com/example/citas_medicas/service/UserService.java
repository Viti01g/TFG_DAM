package com.example.citas_medicas.service;

import com.example.citas_medicas.model.Rol;
import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.repository.UsuarioRepository;
import com.example.citas_medicas.dto.UserUpdateRequestDto;
import com.example.citas_medicas.exception.UserNotFoundException;
import com.example.citas_medicas.exception.OperationNotAllowedException;
import com.example.citas_medicas.exception.DataConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            throw new OperationNotAllowedException("No hay un usuario autenticado para realizar esta acción.");
        }
        String currentUsername = authentication.getName();
        return usuarioRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("Usuario autenticado '" + currentUsername + "' no encontrado en la base de datos. Posible inconsistencia."));
    }

    @Transactional
    public Usuario registrarUsuario(String username, String email, String password, Rol rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new DataConflictException("El nombre de usuario '" + username + "' ya está en uso.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new DataConflictException("El email '" + email + "' ya está en uso.");
        }
        if (rol == Rol.ADMIN || rol == Rol.ADMIN_SUPREMO) {
            throw new OperationNotAllowedException("Rol no permitido para el registro público.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRol(rol);

        return usuarioRepository.save(nuevoUsuario);
    }

    @Transactional
    public Usuario registrarPersonalPorAdmin(String username, String email, String password, Rol rolAAsignar) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new DataConflictException("El nombre de usuario '" + username + "' ya está en uso.");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new DataConflictException("El email '" + email + "' ya está en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRol(rolAAsignar);

        return usuarioRepository.save(nuevoUsuario);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public List<Usuario> getUsersByRole(Rol requestedRol) {
        if (requestedRol == null) {
            return Collections.emptyList();
        }
        return usuarioRepository.findByRol(requestedRol);
    }

    public Usuario getUserById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo.");
        }
        return usuarioRepository.findById(userId.intValue())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
    }

    @Transactional
    public Usuario updateUserFromAdmin(Long userIdToUpdate, UserUpdateRequestDto dto) {
        Integer idToUpdate = userIdToUpdate.intValue();
        Usuario usuarioAActualizar = usuarioRepository.findById(idToUpdate)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userIdToUpdate + ". No se puede actualizar."));

        Usuario currentUser = getCurrentAuthenticatedUser();

        if (currentUser.getRol() == Rol.ADMIN) {
            if (usuarioAActualizar.getRol() == Rol.ADMIN || usuarioAActualizar.getRol() == Rol.ADMIN_SUPREMO) {
                throw new OperationNotAllowedException("Un ADMIN no puede modificar a otro ADMIN o ADMIN_SUPREMO.");
            }
            if (dto.getRol() != null && !dto.getRol().trim().isEmpty()) {
                Rol rolSolicitado = Rol.valueOf(dto.getRol().toUpperCase());
                if (rolSolicitado == Rol.ADMIN || rolSolicitado == Rol.ADMIN_SUPREMO) {
                    throw new OperationNotAllowedException("Un ADMIN no puede asignar el rol ADMIN o ADMIN_SUPREMO.");
                }
            }
        }

        if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()) {
            usuarioAActualizar.setUsername(dto.getUsername().trim());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            usuarioAActualizar.setEmail(dto.getEmail().trim());
        }
        if (dto.getRol() != null && !dto.getRol().trim().isEmpty()) {
            usuarioAActualizar.setRol(Rol.valueOf(dto.getRol().toUpperCase()));
        }
        if (dto.getNuevaPassword() != null && !dto.getNuevaPassword().trim().isEmpty()) {
            usuarioAActualizar.setPassword(passwordEncoder.encode(dto.getNuevaPassword()));
        }

        return usuarioRepository.save(usuarioAActualizar);
    }

    @Transactional
    public boolean deleteUser(Long userIdToDelete) {
        Integer idToDelete = userIdToDelete.intValue();
        Usuario usuarioAEliminar = usuarioRepository.findById(idToDelete)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + idToDelete + ", no se pudo eliminar."));

        Usuario currentUser = getCurrentAuthenticatedUser();

        if (currentUser.getId().equals(idToDelete)) {
            throw new OperationNotAllowedException("Un administrador no puede eliminarse a sí mismo.");
        }

        if (currentUser.getRol() == Rol.ADMIN_SUPREMO || currentUser.getRol() == Rol.ADMIN) {
            usuarioRepository.deleteById(idToDelete);
            return true;
        }

        return false;
    }
}