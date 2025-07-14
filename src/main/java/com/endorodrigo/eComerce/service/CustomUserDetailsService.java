package com.endorodrigo.eComerce.service;

import com.endorodrigo.eComerce.model.User;
import com.endorodrigo.eComerce.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    public Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final IUserRepository userRepository;

    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Email encontrado: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        logger.info("Usuario encontrado: " + user);
        return new User(user.getEmail(), user.getPassword(), user.getRole()); // Asegúrate de que User implemente correctamente los roles
    }
}

