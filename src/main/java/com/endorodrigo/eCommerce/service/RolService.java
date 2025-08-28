package com.endorodrigo.eCommerce.service;

import com.endorodrigo.eCommerce.model.Role;
import com.endorodrigo.eCommerce.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de roles de usuario.
 * Proporciona métodos para buscar y gestionar roles.
 */
@Service
public class RolService {

    private final IRoleRepository roleRepository;

    public RolService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Busca un rol por su ID.
     * @param id ID del rol
     * @return Optional con el rol encontrado
     */
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Busca un rol por su nombre.
     * @param name Nombre del rol
     * @return Optional con el rol encontrado
     */
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Obtiene todos los roles disponibles.
     * @return Lista de todos los roles
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Guarda un nuevo rol.
     * @param role Rol a guardar
     * @return Rol guardado
     */
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Verifica si existe un rol con el nombre especificado.
     * @param name Nombre del rol
     * @return true si existe, false en caso contrario
     */
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
