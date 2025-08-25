package com.endorodrigo.eCommerce.service;


import com.endorodrigo.eCommerce.model.User;
import com.endorodrigo.eCommerce.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Servicio personalizado para la autenticación de usuarios.
 * Implementa UserDetailsService para cargar usuarios por email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Logger para registrar eventos de autenticación.
     */
    public Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /**
     * Repositorio de usuarios para consultas en la base de datos.
     */
    private final IUserRepository userRepository;

    /**
     * Constructor que inyecta el repositorio de usuarios.
     * @param userRepository Repositorio de usuarios
     */
    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga un usuario por su email para autenticación.
     *
     * @param email Email del usuario
     * @return Detalles del usuario para Spring Security
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Email encontrado: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        logger.info("Usuario encontrado: " + user);
        return new User(user.getEmail(), user.getPassword(), user.getRole()); // Asegúrate de que User implemente correctamente los roles
    }
}

