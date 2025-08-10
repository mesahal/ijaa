package com.ijaa.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.service.JWTService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.show-sql=true",
    "logging.level.org.springframework.security=DEBUG",
    "jwt.secret=testSecretKeyForTestingPurposesOnlyDoNotUseInProduction",
    "jwt.expiration=3600"
})
public class TestConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Primary
    public UniqueIdGenerator uniqueIdGenerator() {
        return new UniqueIdGenerator();
    }

    @Bean
    @Primary
    public JWTService jwtService() {
        return new JWTService();
    }
} 