# 🎉 User Event Management APIs

## 📋 Overview

The User Event Management APIs provide comprehensive functionality for alumni to create, manage, and participate in events within the IJAA (IIT Jahangirnagar Alumni Association) platform. These APIs support both user-level event management and admin-level oversight.

---

## 🔐 Authentication

All event APIs require **Bearer Token Authentication** with JWT tokens. Include the following header in all requests:

```
Authorization: Bearer <your-jwt-token>
```

**User Role Required**: `USER` for user event management
**Admin Role Required**: `ADMIN` for admin event management

---

## 📊 Data Models

### EventRequest
```json
{
  "title": "string (required)",
  "description": "string",
  "startDate": "datetime (required)",
  "endDate": "datetime (required)",
  "location": "string",
  "eventType": "string",
  "isOnline": "boolean (default: false)",
  "meetingLink": "string",
  "maxParticipants": "integer (required, min: 1)",
  "organizerName": "string (required)",
  "organizerEmail": "string (required, valid email)"
}
```

### EventResponse
```json
{
  "id": "long",
  "title": "string",
  "description": "string",
  "startDate": "datetime",
  "endDate": "datetime",
  "location": "string",
  "eventType": "string",
  "active": "boolean",
  "isOnline": "boolean",
  "meetingLink": "string",
  "maxParticipants": "integer",
  "currentParticipants": "integer",
  "organizerName": "string",
  "organizerEmail": "string",
  "createdByUsername": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

---

## 👤 User Event Management APIs

### 1. Get User's Events
**Retrieve all events created by the authenticated user**

```http
GET /api/v1/user/events/my-events
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "User events retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "startDate": "2024-12-25T18:00:00",
      "endDate": "2024-12-25T22:00:00",
      "location": "IIT Campus",
      "eventType": "MEETING",
      "active": true,
      "isOnline": false,
      "meetingLink": null,
      "maxParticipants": 100,
      "currentParticipants": 0,
      "organizerName": "John Doe",
      "organizerEmail": "john@example.com",
      "createdByUsername": "john.doe",
      "createdAt": "2024-12-01T10:00:00",
      "updatedAt": "2024-12-01T10:00:00"
    }
  ]
}
```

**Error Responses:**
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient privileges

---

### 2. Get User's Active Events
**Retrieve all active events created by the authenticated user**

```http
GET /api/v1/user/events/my-events/active
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "User active events retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "startDate": "2024-12-25T18:00:00",
      "endDate": "2024-12-25T22:00:00",
      "location": "IIT Campus",
      "eventType": "MEETING",
      "active": true,
      "isOnline": false,
      "meetingLink": null,
      "maxParticipants": 100,
      "currentParticipants": 0,
      "organizerName": "John Doe",
      "organizerEmail": "john@example.com",
      "createdByUsername": "john.doe",
      "createdAt": "2024-12-01T10:00:00",
      "updatedAt": "2024-12-01T10:00:00"
    }
  ]
}
```

---

### 3. Get All Active Events
**Retrieve all active events created by all users (public events)**

```http
GET /api/v1/user/events/all-events
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "All active events retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "startDate": "2024-12-25T18:00:00",
      "endDate": "2024-12-25T22:00:00",
      "location": "IIT Campus",
      "eventType": "MEETING",
      "active": true,
      "isOnline": false,
      "meetingLink": null,
      "maxParticipants": 100,
      "currentParticipants": 0,
      "organizerName": "John Doe",
      "organizerEmail": "john@example.com",
      "createdByUsername": "john.doe",
      "createdAt": "2024-12-01T10:00:00",
      "updatedAt": "2024-12-01T10:00:00"
    }
  ]
}
```

---

### 4. Get Event by ID (Public)
**Retrieve a specific event by ID (public access)**

```http
GET /api/v1/user/events/all-events/{eventId}
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "Event retrieved successfully",
  "code": "200",
  "data": {
    "id": 1,
    "title": "Alumni Meet 2024",
    "description": "Annual alumni gathering",
    "startDate": "2024-12-25T18:00:00",
    "endDate": "2024-12-25T22:00:00",
    "location": "IIT Campus",
    "eventType": "MEETING",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 100,
    "currentParticipants": 0,
    "organizerName": "John Doe",
    "organizerEmail": "john@example.com",
    "createdByUsername": "john.doe",
    "createdAt": "2024-12-01T10:00:00",
    "updatedAt": "2024-12-01T10:00:00"
  }
}
```

**Error Responses:**
- `404 Not Found`: Event not found

---

### 5. Get User's Event by ID
**Retrieve a specific event created by the authenticated user**

```http
GET /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "User event retrieved successfully",
  "code": "200",
  "data": {
    "id": 1,
    "title": "Alumni Meet 2024",
    "description": "Annual alumni gathering",
    "startDate": "2024-12-25T18:00:00",
    "endDate": "2024-12-25T22:00:00",
    "location": "IIT Campus",
    "eventType": "MEETING",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 100,
    "currentParticipants": 0,
    "organizerName": "John Doe",
    "organizerEmail": "john@example.com",
    "createdByUsername": "john.doe",
    "createdAt": "2024-12-01T10:00:00",
    "updatedAt": "2024-12-01T10:00:00"
  }
}
```

**Error Responses:**
- `404 Not Found`: Event not found or not owned by user

---

### 6. Create Event
**Create a new event for the authenticated user**

```http
POST /api/v1/user/events/create
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Alumni Meet 2024",
  "description": "Annual alumni gathering",
  "startDate": "2024-12-25T18:00:00",
  "endDate": "2024-12-25T22:00:00",
  "location": "IIT Campus",
  "eventType": "MEETING",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 100,
  "organizerName": "John Doe",
  "organizerEmail": "john@example.com"
}
```

**Response (201 Created)**
```json
{
  "message": "Event created successfully",
  "code": "201",
  "data": {
    "id": 1,
    "title": "Alumni Meet 2024",
    "description": "Annual alumni gathering",
    "startDate": "2024-12-25T18:00:00",
    "endDate": "2024-12-25T22:00:00",
    "location": "IIT Campus",
    "eventType": "MEETING",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 100,
    "currentParticipants": 0,
    "organizerName": "John Doe",
    "organizerEmail": "john@example.com",
    "createdByUsername": "john.doe",
    "createdAt": "2024-12-01T10:00:00",
    "updatedAt": "2024-12-01T10:00:00"
  }
}
```

**Error Responses:**
- `400 Bad Request`: Invalid event request (e.g., start date after end date)
- `401 Unauthorized`: Missing or invalid token

---

### 7. Update User's Event
**Update an event created by the authenticated user**

```http
PUT /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Updated Alumni Meet 2024",
  "description": "Updated annual alumni gathering",
  "startDate": "2024-12-25T18:00:00",
  "endDate": "2024-12-25T22:00:00",
  "location": "IIT Campus",
  "eventType": "MEETING",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 100,
  "organizerName": "John Doe",
  "organizerEmail": "john@example.com"
}
```

**Response (200 OK)**
```json
{
  "message": "Event updated successfully",
  "code": "200",
  "data": {
    "id": 1,
    "title": "Updated Alumni Meet 2024",
    "description": "Updated annual alumni gathering",
    "startDate": "2024-12-25T18:00:00",
    "endDate": "2024-12-25T22:00:00",
    "location": "IIT Campus",
    "eventType": "MEETING",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 100,
    "currentParticipants": 0,
    "organizerName": "John Doe",
    "organizerEmail": "john@example.com",
    "createdByUsername": "john.doe",
    "createdAt": "2024-12-01T10:00:00",
    "updatedAt": "2024-12-01T11:00:00"
  }
}
```

**Error Responses:**
- `404 Not Found`: Event not found or not owned by user
- `400 Bad Request`: Invalid event request

---

### 8. Delete User's Event
**Delete an event created by the authenticated user**

```http
DELETE /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "Event deleted successfully",
  "code": "200",
  "data": null
}
```

**Error Responses:**
- `404 Not Found`: Event not found or not owned by user

---

## 👨‍💼 Admin Event Management APIs

### 1. Get All Events (Admin)
**Retrieve all events (ADMIN only)**

```http
GET /api/v1/admin/events
Authorization: Bearer <admin-jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "Events retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "title": "New Year Celebration",
      "description": "Celebrate the new year with fireworks and music.",
      "startDate": "2025-01-01T10:00:00",
      "endDate": "2025-01-01T12:00:00",
      "location": "Central Park",
      "eventType": "CELEBRATION",
      "active": true,
      "isOnline": false,
      "meetingLink": null,
      "maxParticipants": 200,
      "currentParticipants": 0,
      "organizerName": "Admin User",
      "organizerEmail": "admin@ijaa.com",
      "createdByUsername": "admin",
      "createdAt": "2025-07-31T01:51:12.870989",
      "updatedAt": "2025-07-31T01:51:12.871015"
    }
  ]
}
```

**Error Responses:**
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient privileges (ADMIN required)

---

### 2. Create Event (Admin)
**Create a new event (ADMIN only)**

```http
POST /api/v1/admin/events
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json

{
  "title": "New Year Celebration",
  "description": "Celebrate the new year with fireworks and music.",
  "startDate": "2025-01-01T10:00:00",
  "endDate": "2025-01-01T12:00:00",
  "location": "Central Park",
  "eventType": "CELEBRATION",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 200,
  "organizerName": "Admin User",
  "organizerEmail": "admin@ijaa.com"
}
```

**Response (201 Created)**
```json
{
  "message": "Event created successfully",
  "code": "201",
  "data": {
    "id": 1,
    "title": "New Year Celebration",
    "description": "Celebrate the new year with fireworks and music.",
    "startDate": "2025-01-01T10:00:00",
    "endDate": "2025-01-01T12:00:00",
    "location": "Central Park",
    "eventType": "CELEBRATION",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 200,
    "currentParticipants": 0,
    "organizerName": "Admin User",
    "organizerEmail": "admin@ijaa.com",
    "createdByUsername": "admin",
    "createdAt": "2025-07-31T01:51:12.870989",
    "updatedAt": "2025-07-31T01:51:12.871015"
  }
}
```

**Error Responses:**
- `400 Bad Request`: Invalid event request
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient privileges (ADMIN required)

---

### 3. Update Event (Admin)
**Update an existing event (ADMIN only)**

```http
PUT /api/v1/admin/events/{eventId}
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json

{
  "title": "Updated New Year Celebration",
  "description": "Updated celebration with fireworks and music.",
  "startDate": "2025-01-01T10:00:00",
  "endDate": "2025-01-01T12:00:00",
  "location": "Central Park",
  "eventType": "CELEBRATION",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 200,
  "organizerName": "Admin User",
  "organizerEmail": "admin@ijaa.com"
}
```

**Response (200 OK)**
```json
{
  "message": "Event updated successfully",
  "code": "200",
  "data": {
    "id": 1,
    "title": "Updated New Year Celebration",
    "description": "Updated celebration with fireworks and music.",
    "startDate": "2025-01-01T10:00:00",
    "endDate": "2025-01-01T12:00:00",
    "location": "Central Park",
    "eventType": "CELEBRATION",
    "active": true,
    "isOnline": false,
    "meetingLink": null,
    "maxParticipants": 200,
    "currentParticipants": 0,
    "organizerName": "Admin User",
    "organizerEmail": "admin@ijaa.com",
    "createdByUsername": "admin",
    "createdAt": "2025-07-31T01:51:12.870989",
    "updatedAt": "2025-07-31T01:51:12.871015"
  }
}
```

**Error Responses:**
- `404 Not Found`: Event not found
- `400 Bad Request`: Invalid event request
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient privileges (ADMIN required)

---

### 4. Delete Event (Admin)
**Delete an event (ADMIN only)**

```http
DELETE /api/v1/admin/events/{eventId}
Authorization: Bearer <admin-jwt-token>
```

**Response (200 OK)**
```json
{
  "message": "Event deleted successfully",
  "code": "200",
  "data": null
}
```

**Error Responses:**
- `404 Not Found`: Event not found
- `401 Unauthorized`: Missing or invalid token
- `403 Forbidden`: Insufficient privileges (ADMIN required)

---

## 🔧 Validation Rules

### EventRequest Validation:
- **title**: Required, non-blank string
- **startDate**: Required, must be a valid datetime
- **endDate**: Required, must be a valid datetime
- **maxParticipants**: Required, minimum value of 1
- **organizerName**: Required, non-blank string
- **organizerEmail**: Required, must be a valid email format
- **startDate** must be before **endDate**

### Business Rules:
- Users can only manage their own events
- Admins can manage all events
- Events are active by default
- Online events require a meeting link
- Maximum participants must be at least 1

---

## 🚨 Error Handling

### Common Error Responses:

**401 Unauthorized**
```json
{
  "message": "Missing Authorization Header",
  "code": "401",
  "data": null
}
```

**403 Forbidden**
```json
{
  "message": "Access denied",
  "code": "403",
  "data": null
}
```

**404 Not Found**
```json
{
  "message": "Event not found",
  "code": "404",
  "data": null
}
```

**400 Bad Request**
```json
{
  "message": "Start date cannot be after end date",
  "code": "400",
  "data": null
}
```

---

## 📝 Usage Examples

### Creating an Online Event
```http
POST /api/v1/user/events/create
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Virtual Alumni Meet",
  "description": "Online gathering for remote alumni",
  "startDate": "2024-12-30T19:00:00",
  "endDate": "2024-12-30T21:00:00",
  "location": "Virtual",
  "eventType": "VIRTUAL_MEETING",
  "isOnline": true,
  "meetingLink": "https://meet.google.com/abc-defg-hij",
  "maxParticipants": 50,
  "organizerName": "Jane Smith",
  "organizerEmail": "jane@example.com"
}
```

### Updating Event Details
```http
PUT /api/v1/user/events/my-events/1
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "title": "Updated Alumni Meet 2024",
  "description": "Updated description with new details",
  "startDate": "2024-12-25T18:00:00",
  "endDate": "2024-12-25T22:00:00",
  "location": "Updated IIT Campus Location",
  "eventType": "MEETING",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 150,
  "organizerName": "John Doe",
  "organizerEmail": "john@example.com"
}
```

---

## 🔗 Related APIs

- **User Authentication**: `/api/v1/user/signin`, `/api/v1/user/signup`
- **Admin Authentication**: `/api/v1/admin/login`
- **Profile Management**: `/api/v1/user/profile/{userId}`
- **Alumni Search**: `/api/v1/user/alumni/search`

---

## 📚 Additional Resources

- **Swagger Documentation**: Available at `/swagger-ui.html` when running the application
- **API Gateway**: All requests go through the gateway at port 8000
- **Service Discovery**: Events are managed by the user-service on port 8081

---

*Last Updated: December 2024*
*Version: 1.0* 