package com.ijaa.gateway.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.ijaa.gateway.repository")
@EntityScan(basePackages = "com.ijaa.gateway.domain.entity")
public class DatabaseConfig {
}
