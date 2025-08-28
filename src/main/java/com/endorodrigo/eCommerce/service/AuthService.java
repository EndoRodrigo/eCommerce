package com.endorodrigo.eCommerce.service;

import com.endorodrigo.eCommerce.model.Role;
import com.endorodrigo.eCommerce.model.User;
import com.endorodrigo.eCommerce.model.UserRegistrationDTO;
import com.endorodrigo.eCommerce.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de autenticación y registro de usuarios.
 * Proporciona métodos para registrar usuarios y validar credenciales.
 */
@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario si el email no está en uso.
     * Lanza excepción si el email ya existe o los datos son incompletos.
     * @param registrationDTO DTO con los datos de registro
     * @param role Rol asignado al usuario
     */
    public void registerUser(UserRegistrationDTO registrationDTO, Role role) {
        if (registrationDTO == null || registrationDTO.getEmail() == null || registrationDTO.getPassword() == null) {
            throw new IllegalArgumentException("Datos de usuario incompletos");
        }
        
        if (!registrationDTO.isPasswordMatching()) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Usuario ya registrado con email: " + registrationDTO.getEmail());
        }
        
        // Crear el usuario con el rol asignado
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRol(role);
        
        userRepository.save(user);
    }

    /**
     * Registra un nuevo usuario usando el modelo User directamente.
     * @param user Usuario a registrar
     */
    public void registerUser(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Datos de usuario incompletos");
        }
        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Usuario ya registrado con email: " + user.getEmail());
        }
        
        // Encriptar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
