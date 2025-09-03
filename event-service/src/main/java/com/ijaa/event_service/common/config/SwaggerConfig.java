package com.ijaa.event_service.common.config;

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

import java.util.List;
import java.util.ArrayList;

@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Value("${production.server.url:https://ijaa-event-service.onrender.com}")
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
                    .url(productionServerUrl != null && !productionServerUrl.isEmpty() ? productionServerUrl : "https://ijaa-event-service.onrender.com")
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
                    .description("Direct Event Service"));
        }

        return new OpenAPI()
                .info(new Info()
                        .title("IJAA Event Service API")
                        .description("Complete event management API documentation for IJAA (IIT Jahangirnagar Alumni Association) application. " +
                                "This API provides comprehensive event management functionality including event creation, participation, invitations, comments, media, analytics, and more.\n\n" +
                                "## 🔐 Authentication Guide\n\n" +
                                "### Step 1: Get JWT Token\n" +
                                "1. Use the **User Authentication** → **POST /api/v1/user/auth/signin** endpoint\n" +
                                "2. Enter credentials: `{\"email\": \"user@example.com\", \"password\": \"password123\"}`\n" +
                                "3. Copy the `token` from the response\n\n" +
                                "### Step 2: Authorize in Swagger UI\n" +
                                "1. Click the **🔒 Authorize** button at the top\n" +
                                "2. Enter your token: `Bearer <your-token-here>`\n" +
                                "3. Click **Authorize** and close the popup\n\n" +
                                "### Step 3: Test Protected APIs\n" +
                                "Now you can test all protected endpoints automatically!\n\n" +
                                "## 🧪 Testing Credentials\n" +
                                "- **User Email**: `user@example.com`\n" +
                                "- **User Password**: `password123`\n" +
                                "- **Role**: `USER`\n\n" +
                                "## 📋 API Groups\n" +
                                "- **User Event Management**: Core event CRUD operations for users\n" +
                                "- **Event Participation**: RSVP and participation tracking\n" +
                                "- **Event Comments**: Event commenting system\n" +
                                "- **Event Media**: Event media attachments\n" +
                                "- **Event Invitations**: Event invitation management\n" +
                                "- **Recurring Events**: Recurring event patterns\n" +


                                "- **Advanced Event Search**: Advanced search and recommendations\n" +
                                "- **Event Reminders**: Event reminder management\n\n" +
                                "## 🎯 Event Management Features\n" +
                                "The system includes comprehensive event management capabilities:\n" +
                                "- **Event Creation & Management**: Full CRUD operations\n" +
                                "- **Event Participation**: RSVP system with multiple statuses\n" +
                                "- **Event Invitations**: Send and manage invitations\n" +
                                "- **Event Comments**: Commenting system for events\n" +
                                "- **Event Media**: Media attachment support\n" +
                                "- **Recurring Events**: Support for recurring event patterns\n" +


                                "- **Advanced Search**: Advanced search with multiple filters\n" +
                                "- **Event Reminders**: Automated reminder system\n\n" +
                                "## 📊 Sample Event Data\n" +
                                "Test with these event scenarios:\n" +
                                "- **Conference Events**: Large-scale professional events\n" +
                                "- **Workshop Events**: Interactive learning sessions\n" +
                                "- **Online Events**: Virtual meetings and webinars\n" +
                                "- **Meetup Events**: Informal networking events\n" +
                                "- **Recurring Events**: Regular series events\n\n" +
                                "## 📝 Sample Request Examples\n\n" +
                                "### Create Event\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"title\": \"Tech Conference 2025\",\n" +
                                "  \"description\": \"Annual technology conference with industry experts\",\n" +
                                "  \"startDate\": \"2025-03-15T09:00:00\",\n" +
                                "  \"endDate\": \"2025-03-15T17:00:00\",\n" +
                                "  \"location\": \"Bangabandhu International Conference Center\",\n" +
                                "  \"eventType\": \"CONFERENCE\",\n" +
                                "  \"privacy\": \"PUBLIC\",\n" +
                                "  \"maxParticipants\": 500,\n" +
                                "  \"isOnline\": false\n" +
                                "}\n" +
                                "```\n\n" +
                                "### RSVP to Event\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"eventId\": 1,\n" +
                                "  \"status\": \"GOING\",\n" +
                                "  \"message\": \"Looking forward to the event!\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "### Add Event Comment\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"eventId\": 1,\n" +
                                "  \"content\": \"This looks like a great event! Looking forward to it.\"\n" +
                                "}\n" +
                                "```\n\n" +
                                "### Send Event Invitations\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"eventId\": 1,\n" +
                                "  \"invitees\": [\n" +
                                "    {\n" +
                                "      \"email\": \"friend1@example.com\",\n" +
                                "      \"message\": \"You're invited to our event!\"\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"email\": \"friend2@example.com\",\n" +
                                "      \"message\": \"Join us for networking!\"\n" +
                                "    }\n" +
                                "  ]\n" +
                                "}\n" +
                                "```\n\n" +
                                "### Create Recurring Event\n" +
                                "```json\n" +
                                "{\n" +
                                "  \"title\": \"Weekly Tech Meetup\",\n" +
                                "  \"description\": \"Weekly technology discussion and networking\",\n" +
                                "  \"startDate\": \"2025-01-15T18:00:00\",\n" +
                                "  \"endDate\": \"2025-01-15T20:00:00\",\n" +
                                "  \"location\": \"Tech Hub Dhaka\",\n" +
                                "  \"eventType\": \"MEETUP\",\n" +
                                "  \"recurrencePattern\": \"WEEKLY\",\n" +
                                "  \"recurrenceInterval\": 1,\n" +
                                "  \"endDate\": \"2025-12-31T23:59:59\"\n" +
                                "}\n" +
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
                                        "1. Use the login endpoint: POST /api/v1/user/auth/signin\n" +
                                        "2. Copy the 'token' field from response\n" +
                                        "3. Add 'Bearer ' prefix when authorizing\n\n" +
                                        "**Example:** `Bearer eyJhbGciOiJIUzUxMiJ9...`")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
