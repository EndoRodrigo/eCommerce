package com.endorodrigo.eComerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 minutos
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

    @Bean
    public JdbcIndexedSessionRepository sessionRepository(JdbcTemplate jdbcTemplate,
                                                          PlatformTransactionManager transactionManager) {
        return new JdbcIndexedSessionRepository(
                jdbcTemplate,
                new TransactionTemplate(transactionManager)
        );
    }
}
