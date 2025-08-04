# 🗄️ Test Data Summary for IJAA APIs

## 📊 Generated Test Data Overview

The system now automatically generates realistic test data when the application starts (if no data exists). This ensures all APIs return meaningful responses for testing and development.

---

## 👥 **User Accounts**

### **Admin Users:**
- **Email:** `admin@ijaa.com` | **Password:** `admin123` | **Role:** `ADMIN`
- **Email:** `events@ijaa.com` | **Password:** `events123` | **Role:** `USER`

### **Regular Users (10 users):**
| Username | Password | User ID |
|----------|----------|---------|
| `john.doe` | `password123` | `USER_1000` |
| `jane.smith` | `password123` | `USER_1001` |
| `mike.johnson` | `password123` | `USER_1002` |
| `sarah.wilson` | `password123` | `USER_1003` |
| `david.brown` | `password123` | `USER_1004` |
| `emma.davis` | `password123` | `USER_1005` |
| `james.miller` | `password123` | `USER_1006` |
| `lisa.garcia` | `password123` | `USER_1007` |
| `robert.rodriguez` | `password123` | `USER_1008` |
| `maria.martinez` | `password123` | `USER_1009` |

---

## 👤 **User Profiles**

Each user has a complete profile with:

### **Sample Profile Data:**
- **Name:** John Doe, Jane Smith, Mike Johnson, etc.
- **Profession:** Software Engineer, Data Scientist, Product Manager, etc.
- **Location:** Various cities in Bangladesh (Dhaka, Chittagong, Sylhet, etc.)
- **Company:** Top tech companies (Google, Microsoft, Amazon, Meta, etc.)
- **Batch:** 2015-2024 (distributed across users)
- **Contact Info:** Phone, LinkedIn, Website, Email, Facebook
- **Bio:** Realistic professional descriptions
- **Connections:** 10-55 connections per user

### **Profile Visibility Settings:**
- All contact information visible by default
- Customizable privacy settings

---

## 💼 **Professional Experience**

Each user has one professional experience entry:

### **Sample Experience Data:**
- **Titles:** Senior Software Engineer, Lead Developer, Technical Lead, etc.
- **Companies:** Google, Microsoft, Amazon, Meta, Apple, etc.
- **Periods:** 2020-2024, 2019-2023, 2018-2022, etc.
- **Descriptions:** Detailed professional experience descriptions

---

## 🎯 **User Interests**

Each user has 3 interests from a pool of 20 realistic options:

### **Interest Categories:**
- **Technology:** Machine Learning, AI, Web Development, Mobile Development
- **Professional:** Cloud Computing, Data Science, Cybersecurity, DevOps
- **Creative:** UI/UX Design, Game Development, Writing, Photography
- **Business:** Startups, Entrepreneurship, Public Speaking, Mentoring
- **Personal:** Open Source, Travel

---

## 🔍 **Alumni Search Data**

Comprehensive searchable alumni profiles with:

### **Searchable Fields:**
- **Name:** John Doe, Jane Smith, etc.
- **Department:** Computer Science, Electrical Engineering, Mechanical Engineering, etc.
- **Profession:** Software Engineer, Data Scientist, Product Manager, etc.
- **Company:** Google, Microsoft, Amazon, Meta, etc.
- **Location:** Various cities in Bangladesh
- **Batch:** 2015-2024
- **Skills:** Java, Python, JavaScript, React, Node.js, etc.

### **Skills by User:**
1. **Java Stack:** Java, Spring Boot, Microservices, Docker, Kubernetes
2. **Python ML:** Python, Machine Learning, TensorFlow, Data Analysis, SQL
3. **JavaScript Full Stack:** JavaScript, React, Node.js, MongoDB, AWS
4. **Mobile Development:** Java, Android, Kotlin, Firebase, Google Cloud
5. **Microsoft Stack:** C#, .NET, Azure, SQL Server, Entity Framework
6. **Python Web:** Python, Django, PostgreSQL, Redis, Nginx
7. **Vue.js Stack:** JavaScript, Vue.js, Express.js, MySQL, Docker
8. **Enterprise Java:** Java, Spring, Hibernate, Oracle, Maven
9. **Python Backend:** Python, Flask, MongoDB, Docker, Kubernetes
10. **Angular Stack:** TypeScript, Angular, Node.js, PostgreSQL, AWS

---

## 🤝 **Connection Data**

### **Connection Types:**
- **Accepted Connections:** 5 connections (user1→user2, user2→user3, etc.)
- **Pending Connections:** 3 connections (user6→user7, user7→user8, user8→user9)

### **Connection Status:**
- `ACCEPTED` - Mutual connections
- `PENDING` - Connection requests awaiting approval

---

## ⚙️ **Feature Flags**

10 feature flags for testing admin functionality:

### **Enabled Features (5):**
- `NEW_UI` - Enable new user interface design
- `CHAT_FEATURE` - Enable real-time chat functionality
- `VIDEO_CALLING` - Enable video calling feature
- `PAYMENT_INTEGRATION` - Enable payment processing integration
- `SOCIAL_LOGIN` - Enable social media login options

### **Disabled Features (5):**
- `ADVANCED_SEARCH` - Enable advanced search with filters
- `NOTIFICATIONS` - Enable push notifications
- `ANALYTICS_DASHBOARD` - Enable analytics dashboard for admins
- `MOBILE_APP` - Enable mobile application features
- `API_RATE_LIMITING` - Enable API rate limiting for security

---

## 🧪 **API Testing Scenarios**

### **Authentication APIs:**
```bash
# User Login
POST /api/v1/user/signin
{
  "username": "john.doe",
  "password": "password123"
}

# Admin Login
POST /api/v1/admin/login
{
  "email": "admin@ijaa.com",
  "password": "admin123"
}
```

### **Profile APIs:**
```bash
# Get User Profile
GET /api/v1/user/profile/USER_1000

# Get User Experiences
GET /api/v1/user/experiences/USER_1000

# Get User Interests
GET /api/v1/user/interests/USER_1000
```

### **Alumni Search APIs:**
```bash
# Search Alumni
POST /api/v1/user/alumni/search
{
  "searchQuery": "Software Engineer",
  "batch": "2020",
  "location": "Dhaka"
}

# Get Search Results
GET /api/v1/user/alumni/search?searchQuery=Java&profession=Software Engineer
```

### **Admin APIs:**
```bash
# Get All Users
GET /api/v1/admin/users

# Get All Admins
GET /api/v1/admin/admins

# Get Feature Flags
GET /api/v1/admin/feature-flags

# Get Dashboard Stats
GET /api/v1/admin/dashboard
```

---

## 📈 **Data Statistics**

| Entity | Count | Description |
|--------|-------|-------------|
| **Users** | 10 | Regular alumni users |
| **Admins** | 2 | System administrators |
| **Profiles** | 10 | Complete user profiles |
| **Experiences** | 10 | Professional experience entries |
| **Interests** | 30 | User interests (3 per user) |
| **Alumni Search** | 10 | Searchable alumni profiles |
| **Connections** | 8 | User connections (5 accepted, 3 pending) |
| **Feature Flags** | 10 | System feature toggles |

---

## 🚀 **Getting Started**

1. **Start the application** - Data will be automatically seeded
2. **Use Swagger UI** - Test all APIs with realistic data
3. **Login with test accounts** - Use the provided credentials
4. **Explore all endpoints** - Every API now returns meaningful data

### **Quick Test:**
```bash
# 1. Start the application
./mvnw spring-boot:run

# 2. Access Swagger UI
http://localhost:8081/swagger-ui.html

# 3. Test user login
POST /api/v1/user/signin
{
  "username": "john.doe",
  "password": "password123"
}

# 4. Test admin login
POST /api/v1/admin/login
{
  "email": "admin@ijaa.com",
  "password": "admin123"
}
```

---

## ✅ **Benefits**

- **Realistic Testing:** All APIs return meaningful, realistic data
- **Complete Coverage:** Every entity has test data
- **Consistent Data:** Structured, related data across all entities
- **Easy Development:** No need to manually create test data
- **Swagger Ready:** Perfect for API documentation and testing
- **Non-Destructive:** Only seeds if no data exists

The test data provides a complete, realistic environment for testing all IJAA APIs and features! 