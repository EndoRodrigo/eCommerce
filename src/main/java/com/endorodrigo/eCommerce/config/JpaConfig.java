package com.endorodrigo.eCommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.endorodrigo.eCommerce.repository")
@EnableTransactionManagement
public class JpaConfig {
    // Configuración por defecto de Spring Boot JPA
}
