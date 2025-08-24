package com.ijaa.file_service.config;

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
                        .title("IJAA File Service API")
                        .description("File management API for IJAA (IIT Jahangirnagar Alumni Association) platform. " +
                                "This API provides file upload, download, and management functionality for user profile and cover photos.\n\n" +
                                "## 📁 File Management Features\n\n" +
                                "### Supported File Types\n" +
                                "- **Profile Photos**: JPG, JPEG, PNG, WEBP (max 5MB)\n" +
                                "- **Cover Photos**: JPG, JPEG, PNG, WEBP (max 5MB)\n\n" +
                                "### File Storage\n" +
                                "- Files are stored locally in `/uploads/profile/` and `/uploads/cover/` directories\n" +
                                "- Unique UUID-based filenames prevent conflicts\n" +
                                "- Automatic cleanup of old files when replaced\n\n" +
                                "### API Usage\n" +
                                "1. Upload files using multipart/form-data\n" +
                                "2. Retrieve file URLs for display\n" +
                                "3. Delete files when no longer needed\n\n" +
                                "### 🔧 Swagger UI File Upload Instructions\n" +
                                "**For File Upload Endpoints:**\n" +
                                "1. Click 'Try it out' on any upload endpoint\n" +
                                "2. In the file parameter section, click 'Choose File'\n" +
                                "3. Select your image file (JPG, JPEG, PNG, WEBP)\n" +
                                "4. Make sure the file size is under 5MB\n" +
                                "5. Click 'Execute' to upload\n\n" +
                                "**Troubleshooting:**\n" +
                                "- If you get 'File is required' error, make sure you selected a file\n" +
                                "- If you get 'Invalid file type' error, use only JPG, JPEG, PNG, or WEBP files\n" +
                                "- If you get 'File size exceeds limit' error, use a smaller file\n\n" +
                                "### Testing with curl\n" +
                                "```bash\n" +
                                "curl -X POST 'http://localhost:8083/api/v1/users/{userId}/profile-photo' \\\n" +
                                "  -H 'Content-Type: multipart/form-data' \\\n" +
                                "  -F 'file=@/path/to/your/image.jpg'\n" +
                                "```")
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
                                .url("http://localhost:8083")
                                .description("Direct File Service")
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
