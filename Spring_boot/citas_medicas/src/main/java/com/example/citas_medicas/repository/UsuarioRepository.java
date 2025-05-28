package com.example.citas_medicas.repository;

import com.example.citas_medicas.model.Rol;
import com.example.citas_medicas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Marca esta interfaz como un repositorio de Spring
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Busca un usuario por su nombre de usuario
    Optional<Usuario> findByUsername(String username);
    
    // Busca un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Verifica si existe un usuario con el nombre de usuario especificado
    boolean existsByUsername(String username);
    
    // Verifica si existe un usuario con el email especificado
    boolean existsByEmail(String email);

    // Obtiene una lista de usuarios que tienen un rol específico
    List<Usuario> findByRol(Rol rol);
    
    // Cuenta la cantidad de usuarios que tienen un rol específico
    long countByRol(Rol rol);
}