package com.endorodrigo.eCommerce.service;

/**
 * Interfaz para servicios relacionados con la entidad Persona.
 * Define operaciones de autenticación.
 */
public interface IServicePersona {
    /**
     * Realiza el login de una persona usando email y contraseña.
     * @param email Email de la persona
     * @param password Contraseña de la persona
     * @return Token de autenticación o mensaje de error
     */
    String login(String email, String password);
}
