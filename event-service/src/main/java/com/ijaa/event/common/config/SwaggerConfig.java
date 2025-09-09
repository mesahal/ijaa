package com.ijaa.event.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.ArrayList;

@Configuration
@ComponentScan(basePackages = "com.ijaa.event.presenter.rest.api")
public class SwaggerConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${production.server.url:http://localhost:8000}")
    private String productionServerUrl;

    @Value("${service.hostname:localhost}")
    private String serviceHostname;

    @Value("${service.port:8082}")
    private String servicePort;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        
        // Add servers based on environment
        if ("prod".equals(activeProfile)) {
            // Production environment - add production servers
            servers.add(new Server()
                    .url(productionServerUrl != null && !productionServerUrl.isEmpty() ? productionServerUrl : "http://localhost:8000")
                    .description("Production Server"));
            
            servers.add(new Server()
                    .url("${GATEWAY_SERVER_URL:http://localhost:8000}/ijaa")
                    .description("Production Gateway Server"));
        } else {
            // Local development environment - add local servers
            servers.add(new Server()
                    .url("http://localhost:8000/ijaa")
                    .description("Local Development Server (via Gateway)"));
            
            servers.add(new Server()
                    .url("http://" + serviceHostname + ":" + servicePort)
                    .description("Direct Event Service"));
        }

        return new OpenAPI()
                .info(new Info()
                        .title("IJAA Event Service API")
                        .description("Event management API for IJAA platform")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Md Sahal")
                                .email("sahal@example.com")
                                .url("https://github.com/ijaa"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(servers)
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token for authentication")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
