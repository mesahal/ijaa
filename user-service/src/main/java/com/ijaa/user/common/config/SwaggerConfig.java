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

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
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
                                "### Step 3: Test Protected APIs\n" +
                                "Now you can test all protected endpoints automatically!\n\n" +
                                "## 🧪 Testing Credentials\n" +
                                "- **Admin Email**: `admin@ijaa.com`\n" +
                                "- **Admin Password**: `admin123`\n" +
                                "- **Role**: `SUPER_ADMIN`\n\n" +
                                "## 📋 API Groups\n" +
                                "- **User Authentication**: Public endpoints for user registration/login\n" +
                                "- **Admin Authentication**: Admin login, signup, profile, dashboard\n" +
                                "- **Admin Management**: Complete admin management functionality")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Md Sahal")
                                .email("sahal@example.com")
                                .url("https://github.com/ijaa"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8000/ijaa")
                                .description("Development Server (via Gateway)"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Direct User Service")
                ))
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