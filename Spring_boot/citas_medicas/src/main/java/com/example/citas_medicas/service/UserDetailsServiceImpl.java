package com.example.citas_medicas.service;

import com.example.citas_medicas.model.Usuario;
import com.example.citas_medicas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

// Marca esta clase como un servicio de Spring
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Inyección de dependencias para interactuar con la base de datos
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Implementación del método para cargar un usuario por su nombre de usuario
    @Override
    @Transactional // Asegura que el acceso a la base de datos se realice dentro de una transacción
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por su nombre de usuario
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        // Crea un conjunto de autoridades (roles y permisos) para el usuario
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (usuario.getRol() != null) {
            // Convierte el rol del usuario en una autoridad con el prefijo "ROLE_"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
            // Aquí podrías añadir permisos adicionales si fuera necesario:
            // authorities.add(new SimpleGrantedAuthority("PERMISO_ESPECIFICO"));
        }

        // Retorna un objeto User de Spring Security con los datos del usuario
        return new User(
                usuario.getUsername(), // Nombre de usuario
                usuario.getPassword(), // Contraseña encriptada almacenada en la base de datos
                authorities // Roles y permisos asociados al usuario
        );
    }
}