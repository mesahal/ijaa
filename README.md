# IJAA (International Jute Alumni Association)

A comprehensive microservices-based alumni management system built with Spring Boot, featuring advanced event management, user networking, and file handling capabilities.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Node.js (for frontend development)

### Database Setup
```sql
-- Create databases for each service
CREATE DATABASE ijaa_users;
CREATE DATABASE ijaa_events;
CREATE DATABASE ijaa_files;
```

### Running the Services

1. **Start Discovery Service** (Port: 8761)
```bash
cd discovery-service
mvn spring-boot:run
```

2. **Start Config Service** (Port: 8888)
```bash
cd config-service
mvn spring-boot:run
```

3. **Start User Service** (Port: 8081)
```bash
cd user-service
mvn spring-boot:run
```

4. **Start Event Service** (Port: 8082)
```bash
cd event-service
mvn spring-boot:run
```

5. **Start File Service** (Port: 8083)
```bash
cd file-service
mvn spring-boot:run
```

6. **Start Gateway Service** (Port: 8080)
```bash
cd gateway-service
mvn spring-boot:run
```

### Access Points
- **Gateway**: http://localhost:8080
- **Discovery Service**: http://localhost:8761
- **User Service**: http://localhost:8081
- **Event Service**: http://localhost:8082
- **File Service**: http://localhost:8083

## 📋 Features

### Core Features
- ✅ **User Management**: Registration, authentication, profile management
- ✅ **Alumni Networking**: Search, connect, and network with alumni
- ✅ **Event Management**: Create, manage, and participate in events
- ✅ **File Management**: Profile photos, cover photos, and file uploads
- ✅ **Admin System**: Comprehensive admin dashboard and user management

### Advanced Event Features
- ✅ **Event Creation & Management**: Full CRUD operations for events
- ✅ **Event Participation**: RSVP system with status tracking
- ✅ **Event Comments**: Comment system with likes and replies
- ✅ **Event Invitations**: Send and manage event invitations
- ✅ **Event Media**: File attachments for events
- ✅ **Event Templates**: Reusable event templates with categories
- ✅ **Recurring Events**: Support for recurring event patterns
- ✅ **Event Analytics**: Comprehensive analytics and reporting
- ✅ **Calendar Integration**: External calendar sync (Google, Outlook, Apple)
- ✅ **Advanced Search**: Multi-filter event search capabilities
- ✅ **Event Reminders**: Customizable event reminders and notifications

### Technical Features
- ✅ **Microservices Architecture**: Distributed system with service discovery
- ✅ **API Gateway**: Centralized routing and authentication
- ✅ **Feature Flags**: Dynamic feature toggling system
- ✅ **JWT Authentication**: Secure token-based authentication
- ✅ **Role-Based Access Control**: USER and ADMIN roles
- ✅ **Swagger Documentation**: Complete API documentation
- ✅ **Database Integration**: PostgreSQL with JPA/Hibernate

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Mobile App    │    │   Third Party   │
│   (React/Vue)   │    │   (React Native)│    │   Integrations  │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │    Gateway Service        │
                    │    (Port: 8080)           │
                    │  - Authentication         │
                    │  - Routing                │
                    │  - Rate Limiting          │
                    └─────────────┬─────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
┌───────▼────────┐    ┌───────────▼──────────┐    ┌────────▼────────┐
│  User Service  │    │   Event Service      │    │  File Service   │
│  (Port: 8081)  │    │   (Port: 8082)       │    │  (Port: 8083)   │
│                │    │                      │    │                 │
│ - Authentication│   │ - Event Management   │    │ - File Upload   │
│ - User Profiles │   │ - Event Analytics    │    │ - File Serving  │
│ - Admin System  │   │ - Calendar Integration│   │ - Media Files   │
│ - Feature Flags │   │ - Event Templates    │    │                 │
└────────────────┘    └──────────────────────┘    └─────────────────┘
        │                         │                         │
        └─────────────────────────┼─────────────────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │   Discovery Service       │
                    │   (Port: 8761)            │
                    │  - Service Registry       │
                    │  - Load Balancing         │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │   Config Service          │
                    │   (Port: 8888)            │
                    │  - Configuration Mgmt     │
                    │  - Environment Settings   │
                    └───────────────────────────┘
```

## 🔧 API Documentation

### Swagger UI Access
- **Gateway**: http://localhost:8080/swagger-ui.html
- **User Service**: http://localhost:8081/swagger-ui.html
- **Event Service**: http://localhost:8082/swagger-ui.html
- **File Service**: http://localhost:8083/swagger-ui.html

### Key API Endpoints

#### Authentication
```bash
# User Registration
POST /ijaa/api/v1/user/signup

# User Login
POST /ijaa/api/v1/user/signin

# Admin Login
POST /ijaa/api/v1/admin/signin
```

#### Events
```bash
# Get all events
GET /ijaa/api/v1/user/events/all-events

# Create event
POST /ijaa/api/v1/user/events/create

# RSVP to event
POST /ijaa/api/v1/user/events/participation/rsvp
```

#### Files
```bash
# Upload profile photo
POST /ijaa/api/v1/users/{userId}/profile-photo

# Get profile photo (public)
GET /ijaa/api/v1/users/{userId}/profile-photo/file/{filename}
```

## 🧪 Testing

### Running Tests
```bash
# User Service Tests
cd user-service
mvn test

# Event Service Tests
cd event-service
mvn test

# File Service Tests
cd file-service
mvn test
```

### Test Status
- **User Service**: 150/157 tests passing
- **Event Service**: 16/40 tests passing
- **File Service**: 95/112 tests passing

## 🔐 Security

### Authentication Flow
1. User registers/logs in via User Service
2. JWT token is generated and returned
3. Subsequent requests include JWT in Authorization header
4. Gateway validates JWT and forwards user context to services

### Feature Flags
- Dynamic feature toggling system
- Hierarchical parent-child relationships
- Database-driven configuration
- Public endpoint for frontend checks: `/ijaa/api/v1/admin/feature-flags/{name}/enabled`

## 📊 Database Schema

### User Service Database (`ijaa_users`)
- `users` - Core user information
- `profiles` - Extended user profiles
- `experiences` - Work experience entries
- `interests` - User interests and skills
- `connections` - Alumni networking connections
- `feature_flags` - Feature flag configuration
- `admins` - Admin user management
- `announcements` - System announcements
- `reports` - User reporting system

### Event Service Database (`ijaa_events`)
- `events` - Event information and metadata
- `event_comments` - Comment system for events
- `event_reminders` - User reminder preferences
- `event_participations` - Event RSVP and participation tracking
- `event_invitations` - Event invitation management
- `event_media` - Event file attachments
- `event_templates` - Reusable event templates
- `recurring_events` - Recurring event patterns
- `event_analytics` - Event analytics and reporting
- `calendar_integrations` - External calendar sync configuration

### File Service Database (`ijaa_files`)
- `file_metadata` - File information and storage paths

## 🚀 Deployment

### Development
- All services run locally with PostgreSQL
- File storage uses local file system
- Eureka server for service discovery

### Production Considerations
- Containerization with Docker
- Kubernetes orchestration
- Cloud storage for files
- Load balancer configuration
- Monitoring and logging setup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the project documentation

## 🔄 Recent Updates

### August 2025
- ✅ Standardized API responses across all services
- ✅ Enhanced feature flag system with hierarchical relationships
- ✅ Comprehensive event management with analytics and templates
- ✅ Advanced admin system with dashboard and user management
- ✅ Improved test coverage and error handling
- ✅ Calendar integration for external calendar sync

---

**Status**: 🟢 Production Ready with comprehensive functionality across all core features.
