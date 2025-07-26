# 📘 Alumni Association Web Application - IIT Jahangirnagar University

## 💡 Overview
This is a full-stack web application designed to connect and engage alumni from IIT, Jahangirnagar University. It facilitates networking, communication, and event organization among alumni. The application includes features such as sign-up/sign-in (with email and social login), user profiles, event creation and registration with payments, group and 1-on-1 chats, audio/video calls, and a powerful alumni search system.

---
## 🛠️ Tech Stack

- **Language**: JavaScript (Frontend), Java (Backend)
- **Frontend Framework**: React.js (using Bolt.new for rapid UI generation)
- **Backend Framework**: Spring Boot
- **Database**: PostgreSQL
- **Authentication Method**: JWT-based authentication (with role-based access control)
- **Social Login Integration**: Google and Facebook OAuth
- **External APIs**:
    - Firebase (for real-time messaging)
    - WebRTC (for audio/video calling)
    - Stripe/Razorpay (for online payments)

---
ijaa-system/  
├── discovery-server/ # Eureka service registry (port 8761)  
├── config-server/ # Centralized configuration (port 8071)  
├── edge-server/ # API Gateway (port 8000)  
└── user-service/ # User management (port 8081)

---
## 📁 Backend Folder Structure

## Folder Structure
### Edge Server (Gateway)
edgeserver/  
├── src/main/java/com/wallet/edgeserver/  
│ ├── config/  
│ │ └── GatewayConfig.java # Route definitions  
│ ├── domain/  
│ │ ├── common/  
│ │ │ └── ApiResponse.java # Standard API response format  
│ │ ├── dto/  
│ │ │ └── CurrentUserContext.java # User context transfer object  
│ │ └── enums/  
│ │ └── JwtClaimsEnum.java # JWT claim constants  
│ ├── exceptions/ # Custom exceptions  
│ ├── filter/  
│ │ └── AuthenticationFilter.java # JWT validation filter  
│ ├── handler/ # Exception handlers  
│ └── utils/ # JWT and serialization utils  
└── src/main/resources/  
└── application.yml # Gateway configuration  

### User Service
user-service/  
├── src/main/java/com/wallet/user/  
│ ├── common/ # Configs and utils  
│ ├── domain/ # DTOs and entities  
│ ├── entity/ # JPA entities  
│ ├── presenter/rest/api/ # REST controllers  
│ ├── repository/ # Spring Data repositories  
│ └── service/ # Business logic  
└── src/main/resources/  
└── application.yml # Service configuration  

---
## User Service Entities

CurrentUserContext (DTO): { username: String }  
Experience: { id: Long, title: String, company: String, period: String, description: String }  
Profile: { id: Long, name: String, profession: String, location: String, batch: String, bio: String, phone: String, linkedin: String, twitter: String, facebook: String, instagram: String, github: String, website: String, skills: List<String>, experience: List<Experience>, createdAt: LocalDateTime, updatedAt: LocalDateTime }  
User: { id: Long, username: String, password: String }

## Gateway Service DTOs
CurrentUserContext (DTO): { username: String }  

---
## 🔐 Auth & Security

- JWT token-based authentication system
- OAuth integration for Facebook and Google login
- Role-based access (Admin, Alumni, Guest)
- Secure password encryption using BCrypt
- CSRF & CORS protection
- Email verification for new users

---
## ⚠️ Constraints

- Follow a layered architecture for the backend (Controller → Service → Repository)
- Avoid using unnecessary external libraries unless required
- Keep codebase modular and maintainable
---

