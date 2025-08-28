package com.endorodrigo.eCommerce.repository;

import com.endorodrigo.eCommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestión de roles de usuario.
 * Proporciona métodos para buscar y gestionar roles.
 */
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Busca un rol por su nombre.
     * @param name Nombre del rol
     * @return Optional con el rol encontrado
     */
    Optional<Role> findByName(String name);
    
    /**
     * Verifica si existe un rol con el nombre especificado.
     * @param name Nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}
