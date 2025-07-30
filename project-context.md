# 🏛️ IJAA Backend System - Complete Context

## 🧾 Project Overview

**IJAA (IIT Jahangirnagar Alumni Association)** is a microservices-based backend system designed to connect and engage alumni from IIT, Jahangirnagar University. The system provides a comprehensive platform for alumni networking, communication, and community building.

### Main Features/Modules:
- **User Authentication & Authorization** - JWT-based secure authentication
- **Alumni Profile Management** - Comprehensive user profiles with professional details
- **Alumni Search & Discovery** - Advanced search functionality for finding fellow alumni
- **Connection Management** - Alumni networking and connection requests
- **API Gateway** - Centralized routing and security
- **Service Discovery** - Microservices registration and discovery

---

## 🛠️ Tech Stack

### Core Technologies:
- **Programming Language**: Java 17
- **Backend Framework**: Spring Boot 3.4.3
- **Microservices Framework**: Spring Cloud 2024.0.0
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **API Format**: REST APIs
- **Authentication**: JWT (JSON Web Tokens)

### Key Dependencies:
- **Spring Boot Starters**: Web, Security, Data JPA, Actuator
- **Spring Cloud**: Gateway, Eureka Client
- **JWT**: `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- **Database**: PostgreSQL driver
- **Utilities**: Lombok, Jakarta Validation
- **Security**: BCrypt password encoding

---

## 📁 Project Structure

### Microservices Architecture:
```
ijaa/
├── discovery-service/     # Eureka Service Registry (Port: 8761)
├── config-service/        # Centralized Configuration (Port: 8071)
├── gateway-service/       # API Gateway (Port: 8000)
└── user-service/         # User Management (Port: 8081)
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
- **`JWTService`** - JWT token creation and validation
- **`ProfileService`** - User profile management and updates
- **`AlumniSearchService`** - Alumni search and discovery functionality
- **`UserDetailsServiceImpl`** - Spring Security user details implementation

### Gateway Service Components:
- **`AuthenticationFilter`** - JWT validation and user context propagation
- **`RouteValidator`** - Determines which routes require authentication
- **`JwtUtil`** - JWT utility functions for token validation
- **`GatewayExceptionHandler`** - Global exception handling for gateway

### Core Entities:
- **`User`** - Core user authentication entity
- **`Profile`** - Extended user profile information
- **`AlumniSearch`** - Searchable alumni profile data
- **`Connection`** - Alumni networking connections
- **`Experience`** - Professional experience entries
- **`Interest`** - User interests and skills

---

## 🔐 Authentication & Authorization

### JWT-Based Authentication:
- **Token Generation**: Uses HMAC-SHA256 with Base64-encoded secret
- **Token Expiration**: 1 hour (3600 seconds)
- **Claims**: Username stored in JWT claims
- **Password Security**: BCrypt with strength 12

### Authentication Flow:
1. **Login**: `POST /api/auth/signin` → Validates credentials → Returns JWT token
2. **Registration**: `POST /api/auth/signup` → Creates user → Returns JWT token
3. **Gateway Validation**: All secured routes validate JWT in `AuthenticationFilter`
4. **User Context**: Validated user context propagated via `X-USER_ID` header

### Security Configuration:
- **CSRF**: Disabled for API endpoints
- **Session Management**: Stateless (JWT-based)
- **Password Encoding**: BCrypt with strength 12
- **CORS**: Configured for frontend domains

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

#### Profile Entity:
```java
Profile {
  id: Long
  username: String (unique)
  userId: String (unique)
  name: String
  profession: String
  location: String
  bio: String
  phone: String
  linkedIn: String
  website: String
  batch: String
  email: String
  facebook: String
  showPhone: Boolean
  showLinkedIn: Boolean
  showWebsite: Boolean
  showEmail: Boolean
  showFacebook: Boolean
  connections: Integer
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
  bio: String
  connections: Integer
  skills: List<String>
  isVisible: Boolean
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

### Database Configuration:
- **Database**: PostgreSQL
- **Connection**: `jdbc:postgresql://localhost:5432/ijaa`
- **Hibernate**: DDL auto-update enabled
- **Dialect**: PostgreSQL dialect

---

## 🌐 API Flow

### Authentication Flow Example:

#### 1. User Registration:
```http
POST /api/auth/signup
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
POST /api/auth/signin
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

#### 3. Authenticated Request:
```http
GET /api/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-USER_ID: eyJ1c2VybmFtZSI6ImpvaG4uZG9lIn0=
```

---

## ⚠️ Error Handling

### Global Exception Handling:
- **`BaseExceptionHandler`** - Common exception handling logic
- **`UserExceptionHandler`** - User service specific exceptions
- **`GatewayExceptionHandler`** - Gateway service exceptions

### Custom Exceptions:
- **`AuthenticationFailedException`** - Invalid credentials
- **`UserAlreadyExistsException`** - Duplicate username
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

### Planned Integrations (from project context):
- **Firebase** - Real-time messaging
- **WebRTC** - Audio/video calling
- **Stripe/Razorpay** - Payment processing
- **Google/Facebook OAuth** - Social login

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

### Key Configuration Files:
- `application.yml` - Service-specific configuration
- `pom.xml` - Maven dependencies and build configuration
- JWT secret configured in application properties

---

## 🔧 Development Guidelines

### Architecture Principles:
- **Layered Architecture**: Controller → Service → Repository
- **Microservices**: Independent, loosely coupled services
- **API Gateway**: Centralized routing and security
- **Service Discovery**: Dynamic service registration

### Code Organization:
- **Domain-Driven Design**: Clear separation of concerns
- **DTO Pattern**: Separate request/response objects
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic encapsulation

### Security Best Practices:
- **JWT Tokens**: Stateless authentication
- **BCrypt**: Secure password hashing
- **CORS**: Proper cross-origin configuration
- **Input Validation**: Jakarta validation annotations 