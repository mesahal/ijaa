# 🏛️ IJAA Backend System - Complete Context

## 🧾 Project Overview

**IJAA (IIT Jahangirnagar Alumni Association)** is a microservices-based backend system designed to connect and engage alumni from IIT, Jahangirnagar University. The system provides a comprehensive platform for alumni networking, communication, and community building with advanced admin management capabilities.

### Main Features/Modules:
- **User Authentication & Authorization** - JWT-based secure authentication for both users and admins
- **Admin Management System** - Multi-role admin system with hierarchical permissions
- **Alumni Profile Management** - Comprehensive user profiles with professional details and privacy controls
- **Alumni Search & Discovery** - Advanced search functionality for finding fellow alumni
- **Connection Management** - Alumni networking and connection requests
- **Experience & Interest Management** - Professional experience and personal interests tracking
- **Feature Flag System** - Dynamic feature toggling for system management
- **API Gateway** - Centralized routing and security with CORS support
- **Service Discovery** - Microservices registration and discovery

---

## 🛠️ Tech Stack

### Core Technologies:
- **Programming Language**: Java 17
- **Backend Framework**: Spring Boot 3.4.3
- **Microservices Framework**: Spring Cloud 2024.0.0
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **API Format**: REST APIs with OpenAPI/Swagger documentation
- **Authentication**: JWT (JSON Web Tokens) with BCrypt password encoding
- **Security**: Spring Security with method-level authorization

### Key Dependencies:
- **Spring Boot Starters**: Web, Security, Data JPA, Actuator
- **Spring Cloud**: Gateway, Eureka Client
- **JWT**: `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- **Database**: PostgreSQL driver
- **Utilities**: Lombok, Jakarta Validation
- **Security**: BCrypt password encoding (strength 12)
- **Documentation**: SpringDoc OpenAPI 3

---

## 📁 Project Structure

### Microservices Architecture:
```
ijaa/
├── discovery-service/     # Eureka Service Registry (Port: 8761)
├── config-service/        # Centralized Configuration (Port: 8071)
├── gateway-service/       # API Gateway (Port: 8000)
└── user-service/         # User & Admin Management (Port: 8081)
```

### Service Layer Structure:
```
src/main/java/com/ijaa/
├── presenter/rest/api/    # REST Controllers (API endpoints)
├── service/              # Business Logic Layer
├── repository/           # Data Access Layer
├── domain/              # Entities, DTOs, Requests/Responses
│   ├── entity/          # JPA Entities
│   ├── dto/             # Data Transfer Objects
│   ├── request/         # API Request DTOs
│   ├── response/        # API Response DTOs
│   ├── enums/           # Enumerations (AdminRole, ConnectionStatus)
│   └── common/          # Shared DTOs (ApiResponse, PagedResponse)
├── common/              # Shared Components
│   ├── config/          # Configuration Classes
│   ├── exceptions/      # Custom Exceptions
│   ├── handler/         # Exception Handlers
│   └── utils/           # Utility Classes
└── filter/              # Gateway Filters (Gateway Service)
```

---

## 🧩 Key Components

### User Service Components:
- **`AuthService`** - Handles user registration, login, and JWT token generation
- **`AdminService`** - Admin authentication, registration, and management
- **`JWTService`** - JWT token creation and validation
- **`ProfileService`** - User profile management and updates
- **`AlumniSearchService`** - Alumni search and discovery functionality
- **`FeatureFlagService`** - Dynamic feature flag management
- **`UserDetailsServiceImpl`** - Spring Security user details implementation
- **`AdminUserDetailsService`** - Spring Security admin user details implementation

### Gateway Service Components:
- **`AuthenticationFilter`** - JWT validation and user context propagation
- **`RouteValidator`** - Determines which routes require authentication
- **`JwtUtil`** - JWT utility functions for token validation
- **`GatewayExceptionHandler`** - Global exception handling for gateway

### Core Entities:
- **`User`** - Core user authentication entity
- **`Admin`** - Admin user entity with role-based access
- **`Profile`** - Extended user profile information with privacy controls
- **`AlumniSearch`** - Searchable alumni profile data with skills
- **`Connection`** - Alumni networking connections with status tracking
- **`Experience`** - Professional experience entries
- **`Interest`** - User interests and skills
- **`FeatureFlag`** - Dynamic feature toggling system

---

## 🔐 Authentication & Authorization

### Dual Authentication System:
- **User Authentication**: Username/password-based for alumni
- **Admin Authentication**: Email/password-based with role-based access control

### JWT-Based Authentication:
- **Token Generation**: Uses HMAC-SHA256 with Base64-encoded secret
- **Token Expiration**: 1 hour (3600 seconds)
- **Claims**: Username/email stored in JWT claims
- **Password Security**: BCrypt with strength 12

### Admin Role Hierarchy:
- **`USER`** - Regular user access for alumni
- **`ADMIN`** - Administrative access for system management

### Authentication Flow:
1. **User Login**: `POST /api/v1/user/signin` → Validates credentials → Returns JWT token
2. **User Registration**: `POST /api/v1/user/signup` → Creates user → Returns JWT token
3. **Admin Login**: `POST /api/v1/admin/login` → Validates admin credentials → Returns JWT token
4. **Admin Registration**: `POST /api/v1/admin/signup` → Creates admin (restricted) → Returns JWT token
5. **Gateway Validation**: All secured routes validate JWT in `AuthenticationFilter`
6. **User Context**: Validated user context propagated via `X-USER_ID` header

### Security Configuration:
- **CSRF**: Disabled for API endpoints
- **Session Management**: Stateless (JWT-based)
- **Password Encoding**: BCrypt with strength 12
- **CORS**: Configured for frontend domains (localhost:3000, Vercel, ngrok)
- **Method Security**: `@PreAuthorize` annotations for role-based access

---

## 🗄️ Database Model Summary

### Core Entities & Relationships:

#### User Entity:
```java
User {
  id: Long
  userId: String (unique)
  username: String (unique)
  password: String (BCrypt encoded)
}
```

#### Admin Entity:
```java
Admin {
  id: Long
  name: String
  email: String (unique)
  passwordHash: String (BCrypt encoded)
  role: AdminRole (USER, ADMIN)
  active: Boolean
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

#### Profile Entity:
```java
Profile {
  id: Long
  username: String (unique)
  userId: String (unique)
  name: String
  profession: String
  location: String
  bio: String (TEXT)
  phone: String
  linkedIn: String
  website: String
  batch: String
  email: String
  facebook: String
  showPhone: Boolean (default: true)
  showLinkedIn: Boolean (default: true)
  showWebsite: Boolean (default: true)
  showEmail: Boolean (default: true)
  showFacebook: Boolean (default: true)
  connections: Integer (default: 0)
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

#### AlumniSearch Entity:
```java
AlumniSearch {
  id: Long
  username: String (unique)
  name: String
  batch: String
  department: String
  profession: String
  company: String
  location: String
  avatar: String
  bio: String (TEXT)
  connections: Integer (default: 0)
  skills: List<String> (ElementCollection)
  isVisible: Boolean (default: true)
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

#### Connection Entity:
```java
Connection {
  id: Long
  requesterUsername: String
  receiverUsername: String
  status: ConnectionStatus (PENDING/ACCEPTED/REJECTED)
  createdAt: LocalDateTime
}
```

#### Experience Entity:
```java
Experience {
  id: Long
  username: String
  userId: String
  title: String
  company: String
  period: String
  description: String (TEXT)
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

#### Interest Entity:
```java
Interest {
  id: Long
  username: String
  userId: String
  interest: String
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

#### FeatureFlag Entity:
```java
FeatureFlag {
  id: Long
  featureName: String (unique)
  enabled: Boolean (default: false)
  description: String
  createdAt: LocalDateTime
  updatedAt: LocalDateTime
}
```

### Database Configuration:
- **Database**: PostgreSQL
- **Connection**: `jdbc:postgresql://localhost:5432/ijaa`
- **Hibernate**: DDL auto-update enabled
- **Dialect**: PostgreSQL dialect
- **Auditing**: JPA auditing enabled for timestamps

---

## 🌐 API Flow

### User Authentication Flow Example:

#### 1. User Registration:
```http
POST /api/v1/user/signup
Content-Type: application/json

{
  "username": "john.doe",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "message": "Registration successful",
  "code": "201",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "USER_ABC123XYZ"
  }
}
```

#### 2. User Login:
```http
POST /api/v1/user/signin
Content-Type: application/json

{
  "username": "john.doe",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "message": "Login successful",
  "code": "200",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "USER_ABC123XYZ"
  }
}
```

### Admin Authentication Flow Example:

#### 1. Admin Login:
```http
POST /api/v1/admin/login
Content-Type: application/json

{
  "email": "admin@ijaa.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "message": "Admin login successful",
  "code": "200",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "adminId": 1,
    "name": "Administrator",
    "email": "admin@ijaa.com",
    "role": "ADMIN",
    "active": true
  }
}
```

#### 2. Authenticated Request:
```http
GET /api/v1/user/profile/{userId}
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-USER_ID: eyJ1c2VybmFtZSI6ImpvaG4uZG9lIn0=
```

---

## 📋 API Endpoints Summary

### User Management APIs:
- `POST /api/v1/user/signin` - User login
- `POST /api/v1/user/signup` - User registration
- `GET /api/v1/user/profile/{userId}` - Get user profile
- `PUT /api/v1/user/basic` - Update basic profile info
- `PUT /api/v1/user/visibility` - Update profile visibility settings

### Experience Management:
- `GET /api/v1/user/experiences/{userId}` - Get user experiences
- `POST /api/v1/user/experiences` - Add experience
- `DELETE /api/v1/user/experiences/{userId}` - Delete experience

### Interest Management:
- `GET /api/v1/user/interests/{userId}` - Get user interests
- `POST /api/v1/user/interests` - Add interest
- `DELETE /api/v1/user/interests/{userId}` - Delete interest

### Alumni Search:
- `POST /api/v1/user/alumni/search` - Search alumni (POST)
- `GET /api/v1/user/alumni/search` - Search alumni (GET with query params)

### Admin Authentication:
- `POST /api/v1/admin/login` - Admin login
- `POST /api/v1/admin/signup` - Admin registration (restricted)
- `GET /api/v1/admin/profile` - Get admin profile
- `GET /api/v1/admin/dashboard` - Get dashboard statistics

### Admin Management (Role-based):
- `GET /api/v1/admin/admins` - Get all admins (ADMIN)
- `PUT /api/v1/admin/admins/{adminId}/role` - Update admin role (ADMIN)
- `POST /api/v1/admin/admins/{adminId}/deactivate` - Deactivate admin (ADMIN)
- `POST /api/v1/admin/admins/{adminId}/activate` - Activate admin (ADMIN)

### User Management (Admin):
- `GET /api/v1/admin/users` - Get all users (ADMIN)
- `POST /api/v1/admin/users/{userId}/block` - Block user (ADMIN)
- `POST /api/v1/admin/users/{userId}/unblock` - Unblock user (ADMIN)
- `DELETE /api/v1/admin/users/{userId}` - Delete user (ADMIN)

### Event Management (Admin):
- `GET /api/v1/admin/events` - Get all events (ADMIN)
- `POST /api/v1/admin/events` - Create event (ADMIN)
- `PUT /api/v1/admin/events/{eventId}` - Update event (ADMIN)
- `DELETE /api/v1/admin/events/{eventId}` - Delete event (ADMIN)

### Announcement Management (Admin):
- `GET /api/v1/admin/announcements` - Get all announcements (ADMIN)
- `POST /api/v1/admin/announcements` - Create announcement (ADMIN)
- `DELETE /api/v1/admin/announcements/{announcementId}` - Delete announcement (ADMIN)

### Report Management (Admin):
- `GET /api/v1/admin/reports` - Get all reports (ADMIN)
- `POST /api/v1/admin/reports/{reportId}/resolve` - Resolve report (ADMIN)

### Feature Flag Management:
- `GET /api/v1/admin/feature-flags` - Get all feature flags (ADMIN)
- `PUT /api/v1/admin/feature-flags/{featureName}` - Update feature flag (ADMIN)

---

## ⚠️ Error Handling

### Global Exception Handling:
- **`BaseExceptionHandler`** - Common exception handling logic
- **`UserExceptionHandler`** - User service specific exceptions
- **`AdminExceptionHandler`** - Admin service specific exceptions
- **`GatewayExceptionHandler`** - Gateway service exceptions

### Custom Exceptions:
- **`AuthenticationFailedException`** - Invalid credentials
- **`UserAlreadyExistsException`** - Duplicate username
- **`AdminAlreadyExistsException`** - Duplicate admin email
- **`AdminNotFoundException`** - Admin not found
- **`InsufficientPrivilegesException`** - Role-based access violations
- **`UserContextException`** - User context issues
- **`MissingAuthorizationHeaderException`** - Missing auth header

### Standard API Response Format:
```java
ApiResponse<T> {
  message: String
  code: String
  data: T
}
```

---

## 📦 External Services

### Current Integrations:
- **Eureka Service Registry** - Service discovery and registration
- **Spring Cloud Config** - Centralized configuration (configured but commented out)
- **PostgreSQL Database** - Primary data storage
- **Swagger/OpenAPI** - API documentation and testing

### Planned Integrations (from SRS):
- **Firebase** - Real-time messaging and authentication
- **WebRTC** - Audio/video calling
- **Stripe/Razorpay/bKash** - Payment processing
- **Google/Facebook OAuth** - Social login
- **AWS S3** - Media storage
- **Socket.IO** - Real-time chat functionality

---

## 🚀 Build & Deployment

### Development Setup:
```bash
# Start services in order:
1. discovery-service (port 8761)
2. config-service (port 8071) 
3. user-service (port 8081)
4. gateway-service (port 8000)
```

### Build Commands:
```bash
# User Service
cd user-service
./mvnw clean install
./mvnw spring-boot:run

# Gateway Service  
cd gateway-service
./mvnw clean install
./mvnw spring-boot:run

# Discovery Service
cd discovery-service
./mvnw spring-boot:run

# Config Service
cd config-service
./mvnw spring-boot:run
```

### Environment Configuration:
- **Database**: PostgreSQL on localhost:5432
- **Service Discovery**: Eureka on localhost:8761
- **API Gateway**: localhost:8000
- **User Service**: localhost:8081
- **CORS**: Configured for localhost:3000, Vercel, and ngrok

### Key Configuration Files:
- `application.yml` - Service-specific configuration
- `pom.xml` - Maven dependencies and build configuration
- JWT secret configured in application properties
- Admin data seeder creates default SUPER_ADMIN

---

## 🔧 Development Guidelines

### Architecture Principles:
- **Layered Architecture**: Controller → Service → Repository
- **Microservices**: Independent, loosely coupled services
- **API Gateway**: Centralized routing and security
- **Service Discovery**: Dynamic service registration
- **Role-Based Access Control**: Simple USER and ADMIN role system

### Code Organization:
- **Domain-Driven Design**: Clear separation of concerns
- **DTO Pattern**: Separate request/response objects
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic encapsulation
- **Exception Handling**: Centralized error management

### Security Best Practices:
- **JWT Tokens**: Stateless authentication
- **BCrypt**: Secure password hashing (strength 12)
- **CORS**: Proper cross-origin configuration
- **Input Validation**: Jakarta validation annotations
- **Method Security**: `@PreAuthorize` for role-based access
- **Admin Seeding**: Automatic default admin creation

### API Documentation:
- **OpenAPI 3**: Comprehensive API documentation
- **Swagger UI**: Interactive API testing interface
- **Example Objects**: Detailed request/response examples
- **Security Schemes**: Bearer token authentication documentation

---

## 🎯 Business Logic Summary

### Core Features Implemented:
1. **Dual Authentication System** - Separate user and admin authentication flows
2. **Simple Role-Based System** - USER and ADMIN roles with clear permissions
3. **Profile Management** - Comprehensive user profiles with privacy controls
4. **Alumni Search** - Advanced search with filtering and pagination
5. **Connection System** - Alumni networking with request/accept/reject flow
6. **Experience & Interest Tracking** - Professional and personal information management
7. **Feature Flag System** - Dynamic feature toggling for system management
8. **Admin Dashboard** - Statistics and management interface

### Planned Features (from SRS):
1. **Event Management** - Event creation, registration, and management
2. **Group System** - Alumni groups with chat functionality
3. **Real-time Chat** - WebSocket-based messaging system
4. **Audio/Video Calling** - WebRTC-based communication
5. **Payment Integration** - Event registration payments
6. **Social Login** - OAuth integration with Google/Facebook
7. **Media Management** - File upload and storage system
8. **Notification System** - Email and push notifications

### System Scalability:
- **Microservices Architecture** - Independent service scaling
- **Database Optimization** - Proper indexing and query optimization
- **Caching Strategy** - Redis integration planned
- **Load Balancing** - Gateway-level load balancing
- **Monitoring** - Actuator endpoints for health checks 