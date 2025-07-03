package com.endorodrigo.eComerce.service;


import com.endorodrigo.eComerce.model.User;
import com.endorodrigo.eComerce.repository.IUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public void registerUser(User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String loginUser(String email, String password){
        User user = userRepository.findByEmail(email).orElse(null);
        if (passwordEncoder.matches(password, user.getPassword())){
            return "Exito";
        }
        else {
            return "Incorrect Password";
        }
    }
}
