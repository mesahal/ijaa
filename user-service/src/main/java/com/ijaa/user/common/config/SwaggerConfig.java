package com.ijaa.user.common.config;

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

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${production.server.url:https://ijaa-user-service.onrender.com}")
    private String productionServerUrl;

    @Value("${service.hostname:localhost}")
    private String serviceHostname;

    @Value("${service.port:8000}")
    private String servicePort;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        
        // Add servers based on environment
        if ("prod".equals(activeProfile)) {
            // Production environment - add production servers
            servers.add(new Server()
                    .url(productionServerUrl != null && !productionServerUrl.isEmpty() ? productionServerUrl : "https://ijaa-user-service.onrender.com")
                    .description("Production Server"));
            
            servers.add(new Server()
                    .url("https://ijaa-gateway-service.onrender.com/ijaa")
                    .description("Production Gateway Server"));
        } else {
            // Local development environment - add local servers
            servers.add(new Server()
                    .url("http://localhost:8000/ijaa")
                    .description("Local Development Server (via Gateway)"));
            
            servers.add(new Server()
                    .url("http://" + serviceHostname + ":" + servicePort)
                    .description("Direct User Service"));
        }

        return new OpenAPI()
                .info(new Info()
                        .title("IJAA Alumni Association API")
                        .description("Complete backend API documentation for IJAA (IIT Jahangirnagar Alumni Association) application. " +
                                "This API provides authentication, user management, admin management, and various administrative functions.\n\n" +
                                "## 🔐 Authentication Guide\n\n" +
                                "### Step 1: Get JWT Token\n" +
                                "1. Use the **Admin Authentication** → **POST /api/v1/admin/login** endpoint\n" +
                                "2. Enter credentials: `{\"email\": \"admin@ijaa.com\", \"password\": \"admin123\"}`\n" +
                                "3. Copy the `token` from the response\n\n" +
                                "### Step 2: Authorize in Swagger UI\n" +
                                "1. Click the **🔒 Authorize** button at the top\n" +
                                "2. Enter your token: `Bearer <your-token-here>`\n" +
                                "3. Click **Authorize** and close the popup\n\n" +
                                "```")
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
                                .description("JWT token for authentication. Include 'Bearer ' prefix.\n\n" +
                                        "**How to get token:**\n" +
                                        "1. Use the login endpoint: POST /api/v1/admin/login\n" +
                                        "2. Copy the 'token' field from response\n" +
                                        "3. Add 'Bearer ' prefix when authorizing\n\n" +
                                        "**Example:** `Bearer eyJhbGciOiJIUzUxMiJ9...`")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
} 
