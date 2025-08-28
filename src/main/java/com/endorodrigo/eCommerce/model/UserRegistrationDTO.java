package com.endorodrigo.eCommerce.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para el registro de usuarios.
 * Contiene solo los campos necesarios para el registro con validaciones apropiadas.
 */
public class UserRegistrationDTO {

    @NotEmpty(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotEmpty(message = "La contraseña es obligatoria")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales")
    private String password;

    @NotEmpty(message = "La confirmación de contraseña es obligatoria")
    private String confirmPassword;

    @NotNull(message = "El rol es obligatorio")
    private Long roleId;

    // Constructor por defecto
    public UserRegistrationDTO() {
    }

    // Constructor con parámetros
    public UserRegistrationDTO(String email, String password, String confirmPassword, Long roleId) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.roleId = roleId;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    // Método para validar que las contraseñas coincidan
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", confirmPassword='[PROTECTED]'" +
                ", roleId=" + roleId +
                '}';
    }
}
