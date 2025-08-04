# 🧪 IJAA API Testing Guide with Test Data

## 🚀 Quick Start

The system now automatically generates realistic test data when you start the application. Here's how to test all APIs:

---

## 📋 **Available Test Accounts**

### **Admin Users:**
```json
{
  "email": "admin@ijaa.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

### **Regular Users (10 users):**
```json
{
  "username": "john.doe",
  "password": "password123"
}
```

---

## 🔐 **Authentication Testing**

### **1. User Login**
```bash
curl -X POST "http://localhost:8081/api/v1/user/signin" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "message": "Login successful",
  "code": "200",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "USER_1000"
  }
}
```

### **2. Admin Login**
```bash
curl -X POST "http://localhost:8081/api/v1/admin/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@ijaa.com",
    "password": "admin123"
  }'
```

**Expected Response:**
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

---

## 👤 **Profile Management Testing**

### **3. Get User Profile**
```bash
curl -X GET "http://localhost:8081/api/v1/user/profile/USER_1000" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Profile retrieved successfully",
  "code": "200",
  "data": {
    "id": 1,
    "username": "user1",
    "userId": "USER_1000",
    "name": "John Doe",
    "profession": "Software Engineer",
    "location": "Dhaka, Bangladesh",
    "bio": "Passionate software engineer with experience in modern technologies.",
    "phone": "+880-1001-100000",
    "linkedIn": "linkedin.com/in/johndoe",
    "website": "https://johndoe.com",
    "batch": "2015",
    "email": "johndoe@gmail.com",
    "facebook": "facebook.com/johndoe",
    "showPhone": true,
    "showLinkedIn": true,
    "showWebsite": true,
    "showEmail": true,
    "showFacebook": true,
    "connections": 10,
    "createdAt": "2025-08-02T15:30:00",
    "updatedAt": "2025-08-02T15:30:00"
  }
}
```

### **4. Update Profile Visibility**
```bash
curl -X PUT "http://localhost:8081/api/v1/user/visibility" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "showPhone": false,
    "showLinkedIn": true,
    "showWebsite": true,
    "showEmail": false,
    "showFacebook": true
  }'
```

---

## 💼 **Experience Management Testing**

### **5. Get User Experiences**
```bash
curl -X GET "http://localhost:8081/api/v1/user/experiences/USER_1000" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Experiences retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "username": "user1",
      "userId": "USER_1000",
      "title": "Senior Software Engineer",
      "company": "Google",
      "period": "2020-2024",
      "description": "Led development of scalable web applications using modern technologies. Collaborated with cross-functional teams to deliver high-quality software solutions.",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    }
  ]
}
```

### **6. Add New Experience**
```bash
curl -X POST "http://localhost:8081/api/v1/user/experiences" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Software Engineer",
    "company": "Microsoft",
    "period": "2018-2020",
    "description": "Developed and maintained web applications using React and Node.js."
  }'
```

---

## 🎯 **Interest Management Testing**

### **7. Get User Interests**
```bash
curl -X GET "http://localhost:8081/api/v1/user/interests/USER_1000" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Interests retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "username": "user1",
      "userId": "USER_1000",
      "interest": "Machine Learning",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    },
    {
      "id": 2,
      "username": "user1",
      "userId": "USER_1000",
      "interest": "Web Development",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    },
    {
      "id": 3,
      "username": "user1",
      "userId": "USER_1000",
      "interest": "Cloud Computing",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    }
  ]
}
```

### **8. Add New Interest**
```bash
curl -X POST "http://localhost:8081/api/v1/user/interests" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "interest": "Artificial Intelligence"
  }'
```

---

## 🔍 **Alumni Search Testing**

### **9. Search Alumni (POST)**
```bash
curl -X POST "http://localhost:8081/api/v1/user/alumni/search" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "searchQuery": "Software Engineer",
    "batch": "2020",
    "location": "Dhaka"
  }'
```

### **10. Search Alumni (GET)**
```bash
curl -X GET "http://localhost:8081/api/v1/user/alumni/search?searchQuery=Java&profession=Software Engineer" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Alumni search completed successfully",
  "code": "200",
  "data": {
    "content": [
      {
        "id": 1,
        "username": "user1",
        "name": "John Doe",
        "batch": "2015",
        "department": "Computer Science & Engineering",
        "profession": "Software Engineer",
        "company": "Google",
        "location": "Dhaka, Bangladesh",
        "avatar": "https://ui-avatars.com/api/?name=John+Doe&background=random",
        "bio": "Experienced software engineer with expertise in modern technologies. Passionate about innovation and continuous learning.",
        "connections": 10,
        "skills": ["Java", "Spring Boot", "Microservices", "Docker", "Kubernetes"],
        "isVisible": true
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "numberOfElements": 1
  }
}
```

---

## 👨‍💼 **Admin Management Testing**

### **11. Get Admin Profile**
```bash
curl -X GET "http://localhost:8081/api/v1/admin/profile" \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

### **12. Get Dashboard Statistics**
```bash
curl -X GET "http://localhost:8081/api/v1/admin/dashboard" \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Dashboard stats retrieved successfully",
  "code": "200",
  "data": {
    "totalUsers": 12,
    "activeUsers": 10,
    "totalEvents": 25,
    "activeEvents": 8,
    "totalAnnouncements": 45,
    "pendingReports": 3,
    "blockedUsers": 5
  }
}
```

### **13. Get All Users (Admin)**
```bash
curl -X GET "http://localhost:8081/api/v1/admin/users" \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

### **14. Get All Admins (Admin)**
```bash
curl -X GET "http://localhost:8081/api/v1/admin/admins" \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

### **15. Get Feature Flags (Admin)**
```bash
curl -X GET "http://localhost:8081/api/v1/admin/feature-flags" \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

**Expected Response:**
```json
{
  "message": "Feature flags retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "featureName": "NEW_UI",
      "enabled": true,
      "description": "Enable new user interface design",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    },
    {
      "id": 2,
      "featureName": "CHAT_FEATURE",
      "enabled": true,
      "description": "Enable real-time chat functionality",
      "createdAt": "2025-08-02T15:30:00",
      "updatedAt": "2025-08-02T15:30:00"
    }
  ]
}
```

---

## 🧪 **Swagger UI Testing**

### **Access Swagger UI:**
1. Start the application: `./mvnw spring-boot:run`
2. Open browser: `http://localhost:8081/swagger-ui.html`
3. Use the test accounts above to authenticate
4. Test all endpoints with realistic data

### **Quick Test Sequence:**
1. **User Login** → Get token
2. **Get Profile** → View user data
3. **Get Experiences** → View professional experience
4. **Get Interests** → View user interests
5. **Search Alumni** → Find other users
6. **Admin Login** → Get admin token
7. **Get Dashboard** → View admin statistics
8. **Get Feature Flags** → View system features

---

## 📊 **Test Data Summary**

| Entity | Count | Test Accounts |
|--------|-------|---------------|
| **Users** | 12 | john.doe, jane.smith, mike.johnson, etc. |
| **Admins** | 1 | admin@ijaa.com |
| **Profiles** | 12 | Complete user profiles with contact info |
| **Experiences** | 12 | Professional experience entries |
| **Interests** | 36 | User interests (3 per user) |
| **Alumni Search** | 12 | Searchable alumni profiles |
| **Connections** | 8 | User connections (5 accepted, 3 pending) |
| **Feature Flags** | 10 | System feature toggles |

---

## ✅ **Benefits of Test Data**

- **Realistic Testing:** All APIs return meaningful, realistic data
- **Complete Coverage:** Every entity has test data
- **Consistent Data:** Structured, related data across all entities
- **Easy Development:** No need to manually create test data
- **Swagger Ready:** Perfect for API documentation and testing
- **Non-Destructive:** Only seeds if no data exists

The test data provides a complete, realistic environment for testing all IJAA APIs and features! 