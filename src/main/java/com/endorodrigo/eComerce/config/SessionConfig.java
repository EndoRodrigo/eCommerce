package com.endorodrigo.eComerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 minutos
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
    
    @Bean
    public JdbcIndexedSessionRepository sessionRepository() {
        JdbcIndexedSessionRepository jdbcIndexedSessionRepository = new JdbcIndexedSessionRepository();
        return jdbcIndexedSessionRepository;
    }
}
