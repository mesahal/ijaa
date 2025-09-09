# IJAA (IIT JU Alumni Association) - Complete API Documentation

## Table of Contents
1. [Overview](#overview)
2. [Base URLs and Gateway](#base-urls-and-gateway)
3. [Authentication](#authentication)
4. [User Service APIs](#user-service-apis)
5. [Event Service APIs](#event-service-apis)
6. [File Service APIs](#file-service-apis)
7. [Config Service APIs](#config-service-apis)
8. [Discovery Service APIs](#discovery-service-apis)
9. [Health Check APIs](#health-check-apis)
10. [Error Responses](#error-responses)

## Overview

The IJAA platform is a comprehensive microservices-based alumni management system with the following services:

- **Gateway Service** (Port: 8080) - API Gateway and routing
- **User Service** (Port: 8081) - User management and authentication
- **Event Service** (Port: 8082) - Event management and features
- **File Service** (Port: 8083) - File upload and management
- **Config Service** (Port: 8888) - Configuration management
- **Discovery Service** (Port: 8761) - Service registry

## Base URLs and Gateway

### Gateway Base URL
```
http://localhost:8080/ijaa
```

### Direct Service URLs (Development Only)
```
User Service:    http://localhost:8081/api/v1/user
Event Service:   http://localhost:8082/api/v1/event
File Service:    http://localhost:8083/api/v1/file
Config Service:  http://localhost:8888/api/v1/config
Discovery Service: http://localhost:8761/api/v1/discovery
```

### Gateway Routing
All production requests should go through the gateway with the `/ijaa` prefix:
```
Gateway URL: http://localhost:8080/ijaa/api/v1/{service}/
```

## Authentication

### JWT Authentication System
The platform uses a comprehensive JWT authentication system with refresh tokens.

#### Login
```http
POST /ijaa/api/v1/user/signin
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Login successful",
  "code": "200",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": "USER_ABC123"
  }
}
```

**Cookie Set:** `refreshToken=<secure_token>` (HttpOnly, Secure, 7 days)

#### Token Refresh
```http
POST /ijaa/api/v1/user/refresh
Cookie: refreshToken=<refresh_token_value>
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Token refreshed successfully",
  "code": "200",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": "USER_ABC123"
  }
}
```

#### Logout
```http
POST /ijaa/api/v1/user/logout
Cookie: refreshToken=<refresh_token_value>
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Logout successful",
  "code": "200",
  "data": null
}
```

### Authorization Headers
For protected endpoints, include the access token:
```http
Authorization: Bearer <access_token>
```

## User Service APIs

### Authentication Endpoints

#### User Registration
```http
POST /ijaa/api/v1/user/signup
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "user@example.com"
}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "User registered successfully",
  "code": "201",
  "data": {
    "userId": "USER_ABC123",
    "username": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "role": "USER",
    "createdAt": "2024-12-06T10:30:00"
  }
}
```

#### Change Password
```http
POST /ijaa/api/v1/user/change-password
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "currentPassword": "oldpassword123",
  "newPassword": "newpassword123"
}
```
**Authorization:** USER role required  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Password changed successfully",
  "code": "200",
  "data": null
}
```

### Profile Management

#### Get User Profile
```http
GET /ijaa/api/v1/user/profile/{userId}
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** user.profile

**Response:**
```json
{
  "message": "Profile retrieved successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "username": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "email": "user@example.com",
    "phone": "+1234567890",
    "bio": "Software Engineer",
    "location": "New York, USA",
    "profilePhotoUrl": "http://localhost:8083/api/v1/file/users/USER_ABC123/profile-photo",
    "coverPhotoUrl": "http://localhost:8083/api/v1/file/users/USER_ABC123/cover-photo",
    "createdAt": "2024-12-06T10:30:00",
    "updatedAt": "2024-12-06T10:30:00"
  }
}
```

#### Update Profile
```http
PUT /ijaa/api/v1/user/profile
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "bio": "Senior Software Engineer",
  "location": "San Francisco, USA"
}
```
**Authorization:** USER role required  
**Feature Flag:** user.profile

**Response:**
```json
{
  "message": "Profile updated successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "bio": "Senior Software Engineer",
    "location": "San Francisco, USA",
    "updatedAt": "2024-12-06T11:30:00"
  }
}
```

### User Interests

#### Add Interest
```http
POST /ijaa/api/v1/user/interests
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "interest": "Machine Learning",
  "category": "Technology"
}
```
**Authorization:** USER role required  
**Feature Flag:** user.interests

**Response:**
```json
{
  "message": "Interest added successfully",
  "code": "201",
  "data": {
    "id": 1,
    "interest": "Machine Learning",
    "category": "Technology",
    "userId": "USER_ABC123"
  }
}
```

### Work Experience

#### Add Experience
```http
POST /ijaa/api/v1/user/experiences
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "company": "Google",
  "position": "Software Engineer",
  "startDate": "2020-01-01",
  "endDate": "2023-12-31",
  "description": "Developed web applications using React and Node.js"
}
```
**Authorization:** USER role required  
**Feature Flag:** user.experiences

**Response:**
```json
{
  "message": "Experience added successfully",
  "code": "201",
  "data": {
    "id": 1,
    "company": "Google",
    "position": "Software Engineer",
    "startDate": "2020-01-01",
    "endDate": "2023-12-31",
    "description": "Developed web applications using React and Node.js",
    "userId": "USER_ABC123"
  }
}
```

### Alumni Search

#### Search Alumni
```http
POST /ijaa/api/v1/user/alumni/search
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "query": "software engineer",
  "location": "New York",
  "graduationYear": 2020,
  "page": 0,
  "size": 10
}
```
**Authorization:** USER role required  
**Feature Flag:** user.search

**Response:**
```json
{
  "message": "Search completed successfully",
  "code": "200",
  "data": {
    "content": [
      {
        "userId": "USER_XYZ789",
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane@example.com",
        "location": "New York, USA",
        "graduationYear": 2020,
        "profilePhotoUrl": "http://localhost:8083/api/v1/file/users/USER_XYZ789/profile-photo"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

### Location Management

#### Get All Countries
```http
GET /ijaa/api/v1/user/location/countries
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** user.location

**Response:**
```json
{
  "message": "Countries fetched successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "name": "Afghanistan"
    },
    {
      "id": 2,
      "name": "Albania"
    }
  ]
}
```

#### Get Cities by Country
```http
GET /ijaa/api/v1/user/location/countries/{countryId}/cities
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** user.location

**Response:**
```json
{
  "message": "Cities fetched successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "name": "New York",
      "countryId": 1
    },
    {
      "id": 2,
      "name": "Los Angeles",
      "countryId": 1
    }
  ]
}
```

### User Settings

#### Get User Settings
```http
GET /ijaa/api/v1/user/settings/
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** None

**Response:**
```json
{
  "message": "User settings retrieved successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "theme": "DARK",
    "notifications": true,
    "emailNotifications": true
  }
}
```

#### Update User Settings
```http
PUT /ijaa/api/v1/user/settings/
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "theme": "LIGHT",
  "notifications": false,
  "emailNotifications": true
}
```
**Authorization:** USER role required  
**Feature Flag:** None

#### Update Profile Visibility
```http
PUT /ijaa/api/v1/user/visibility
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "userId": "USER_ABC123",
  "showPhone": true,
  "showLinkedIn": true,
  "showWebsite": false,
  "showEmail": true,
  "showFacebook": false
}
```
**Authorization:** USER role required  
**Feature Flag:** user.profile

**Response:**
```json
{
  "message": "User settings updated successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "theme": "LIGHT",
    "notifications": false,
    "emailNotifications": true
  }
}
```

### Admin Management

#### Get All Admins
```http
GET /ijaa/api/v1/user/admin/admins
Authorization: Bearer <admin_access_token>
```
**Authorization:** ADMIN role required  
**Feature Flag:** admin.features

**Response:**
```json
{
  "message": "Admins retrieved successfully",
  "code": "200",
  "data": [
    {
      "adminId": "ADMIN_123",
      "username": "admin@ijaa.com",
      "firstName": "Admin",
      "lastName": "User",
      "role": "ADMIN",
      "active": true,
      "createdAt": "2024-12-06T10:30:00"
    }
  ]
}
```

#### Admin Login
```http
POST /ijaa/api/v1/user/admin/login
Content-Type: application/json

{
  "email": "admin@ijaa.com",
  "password": "adminpassword123"
}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** admin.auth

#### Admin Registration
```http
POST /ijaa/api/v1/user/admin/admins/signup
Authorization: Bearer <admin_access_token>
Content-Type: application/json

{
  "username": "newadmin@ijaa.com",
  "password": "adminpassword123",
  "firstName": "New",
  "lastName": "Admin"
}
```
**Authorization:** ADMIN role required  
**Feature Flag:** admin.auth

**Response:**
```json
{
  "message": "Admin registered successfully",
  "code": "201",
  "data": {
    "adminId": "ADMIN_456",
    "username": "newadmin@ijaa.com",
    "firstName": "New",
    "lastName": "Admin",
    "role": "ADMIN",
    "active": true
  }
}
```

### Feature Flag Management

#### Get All Feature Flags
```http
GET /ijaa/api/v1/user/admin/feature-flags/
Authorization: Bearer <admin_access_token>
```
**Authorization:** ADMIN role required  
**Feature Flag:** admin.features

**Response:**
```json
{
  "message": "Feature flags retrieved successfully",
  "code": "200",
  "data": [
    {
      "name": "events",
      "enabled": true,
      "description": "Event management feature",
      "parent": null,
      "children": ["events.creation", "events.comments"]
    },
    {
      "name": "events.creation",
      "enabled": true,
      "description": "Event creation feature",
      "parent": "events",
      "children": []
    }
  ]
}
```

#### Check Feature Flag Status (Public)
```http
GET /ijaa/api/v1/user/admin/feature-flags/{name}/enabled
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Feature flag status retrieved successfully",
  "code": "200",
  "data": {
    "name": "events",
    "enabled": true
  }
}
```

## Event Service APIs

### Core Event Management

#### Get My Events
```http
GET /ijaa/api/v1/event/my-events
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events

**Response:**
```json
{
  "message": "User events retrieved successfully",
  "code": "200",
  "data": [
    {
      "eventId": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "location": "IIT JU Campus",
      "startDate": "2024-12-25T10:00:00",
      "endDate": "2024-12-25T18:00:00",
      "organizer": "USER_ABC123",
      "organizerName": "John Doe",
      "maxParticipants": 100,
      "currentParticipants": 25,
      "status": "ACTIVE",
      "bannerUrl": "http://localhost:8083/api/v1/file/events/1/banner",
      "createdAt": "2024-12-06T10:30:00"
    }
  ]
}
```

#### Get All Events
```http
GET /ijaa/api/v1/event/all-events
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events

**Response:**
```json
{
  "message": "All active events retrieved successfully",
  "code": "200",
  "data": [
    {
      "eventId": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "location": "IIT JU Campus",
      "startDate": "2024-12-25T10:00:00",
      "endDate": "2024-12-25T18:00:00",
      "organizer": "USER_ABC123",
      "organizerName": "John Doe",
      "maxParticipants": 100,
      "currentParticipants": 25,
      "status": "ACTIVE",
      "bannerUrl": "http://localhost:8083/api/v1/file/events/1/banner"
    }
  ]
}
```

#### Get Event by ID
```http
GET /ijaa/api/v1/event/all-events/{eventId}
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events

**Response:**
```json
{
  "message": "Event retrieved successfully",
  "code": "200",
  "data": {
    "eventId": 1,
    "title": "Alumni Meet 2024",
    "description": "Annual alumni gathering with networking opportunities",
    "location": "IIT JU Campus, Kalyani",
    "startDate": "2024-12-25T10:00:00",
    "endDate": "2024-12-25T18:00:00",
    "organizer": "USER_ABC123",
    "organizerName": "John Doe",
    "maxParticipants": 100,
    "currentParticipants": 25,
    "status": "ACTIVE",
    "bannerUrl": "http://localhost:8083/api/v1/file/events/1/banner",
    "createdAt": "2024-12-06T10:30:00",
    "updatedAt": "2024-12-06T10:30:00"
  }
}
```

#### Create Event
```http
POST /ijaa/api/v1/event/create
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Tech Talk: AI in 2024",
  "description": "Discussion about AI trends and opportunities",
  "location": "Virtual Meeting",
  "startDate": "2024-12-30T14:00:00",
  "endDate": "2024-12-30T16:00:00",
  "maxParticipants": 50,
  "eventType": "VIRTUAL"
}
```
**Authorization:** USER role required  
**Feature Flag:** events.creation

**Response:**
```json
{
  "message": "Event created successfully",
  "code": "201",
  "data": {
    "eventId": 2,
    "title": "Tech Talk: AI in 2024",
    "description": "Discussion about AI trends and opportunities",
    "location": "Virtual Meeting",
    "startDate": "2024-12-30T14:00:00",
    "endDate": "2024-12-30T16:00:00",
    "organizer": "USER_ABC123",
    "organizerName": "John Doe",
    "maxParticipants": 50,
    "currentParticipants": 0,
    "status": "ACTIVE",
    "eventType": "VIRTUAL",
    "createdAt": "2024-12-06T11:30:00"
  }
}
```

#### Update Event
```http
PUT /ijaa/api/v1/event/my-events/{eventId}
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "title": "Tech Talk: AI in 2024 - Updated",
  "description": "Updated discussion about AI trends and opportunities",
  "location": "Virtual Meeting Room",
  "startDate": "2024-12-30T15:00:00",
  "endDate": "2024-12-30T17:00:00",
  "maxParticipants": 75
}
```
**Authorization:** USER role required  
**Feature Flag:** events.creation

**Response:**
```json
{
  "message": "Event updated successfully",
  "code": "200",
  "data": {
    "eventId": 2,
    "title": "Tech Talk: AI in 2024 - Updated",
    "description": "Updated discussion about AI trends and opportunities",
    "location": "Virtual Meeting Room",
    "startDate": "2024-12-30T15:00:00",
    "endDate": "2024-12-30T17:00:00",
    "maxParticipants": 75,
    "updatedAt": "2024-12-06T12:00:00"
  }
}
```

#### Delete Event
```http
DELETE /ijaa/api/v1/event/my-events/{eventId}
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.creation

**Response:**
```json
{
  "message": "Event deleted successfully",
  "code": "200",
  "data": null
}
```

#### Search Events
```http
POST /ijaa/api/v1/event/search
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "query": "tech talk",
  "location": "Virtual",
  "startDate": "2024-12-01",
  "endDate": "2024-12-31",
  "eventType": "VIRTUAL",
  "page": 0,
  "size": 10
}
```
**Authorization:** USER role required  
**Feature Flag:** events.search

**Response:**
```json
{
  "message": "Events found successfully",
  "code": "200",
  "data": {
    "content": [
      {
        "eventId": 2,
        "title": "Tech Talk: AI in 2024 - Updated",
        "description": "Updated discussion about AI trends and opportunities",
        "location": "Virtual Meeting Room",
        "startDate": "2024-12-30T15:00:00",
        "endDate": "2024-12-30T17:00:00",
        "organizer": "USER_ABC123",
        "organizerName": "John Doe",
        "maxParticipants": 75,
        "currentParticipants": 0,
        "status": "ACTIVE"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

### Event Comments

#### Add Comment
```http
POST /ijaa/api/v1/event/comments/
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "eventId": 1,
  "content": "Looking forward to this event!",
  "parentCommentId": null
}
```
**Authorization:** USER role required  
**Feature Flag:** events.comments

**Response:**
```json
{
  "message": "Comment created successfully",
  "code": "201",
  "data": {
    "commentId": 1,
    "content": "Looking forward to this event!",
    "eventId": 1,
    "authorId": "USER_ABC123",
    "authorName": "John Doe",
    "parentCommentId": null,
    "likes": 0,
    "replies": [],
    "createdAt": "2024-12-06T12:00:00"
  }
}
```

#### Get Event Comments
```http
GET /ijaa/api/v1/event/comments/event/{eventId}?page=0&size=10
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.comments

**Response:**
```json
{
  "message": "Event comments retrieved successfully",
  "code": "200",
  "data": [
    {
      "commentId": 1,
      "content": "Looking forward to this event!",
      "eventId": 1,
      "authorId": "USER_ABC123",
      "authorName": "John Doe",
      "parentCommentId": null,
      "likes": 2,
      "replies": [
        {
          "commentId": 2,
          "content": "Me too!",
          "eventId": 1,
          "authorId": "USER_XYZ789",
          "authorName": "Jane Smith",
          "parentCommentId": 1,
          "likes": 0,
          "replies": [],
          "createdAt": "2024-12-06T12:15:00"
        }
      ],
      "createdAt": "2024-12-06T12:00:00"
    }
  ]
}
```

#### Like/Unlike Comment
```http
POST /ijaa/api/v1/event/comments/{commentId}/like
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.comments

**Response:**
```json
{
  "message": "Comment like status updated successfully",
  "code": "200",
  "data": {
    "commentId": 1,
    "likes": 3,
    "userLiked": true
  }
}
```

### Event Participation

#### RSVP to Event
```http
POST /ijaa/api/v1/event/participation/rsvp
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "eventId": 1,
  "status": "GOING",
  "message": "Excited to attend!"
}
```
**Authorization:** USER role required  
**Feature Flag:** events.participation

**Response:**
```json
{
  "message": "RSVP successful",
  "code": "200",
  "data": {
    "participationId": 1,
    "eventId": 1,
    "userId": "USER_ABC123",
    "status": "GOING",
    "message": "Excited to attend!",
    "rsvpDate": "2024-12-06T12:00:00"
  }
}
```

#### Get My Participations
```http
GET /ijaa/api/v1/event/participation/my-participations
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.participation

**Response:**
```json
{
  "message": "Participations retrieved successfully",
  "code": "200",
  "data": [
    {
      "participationId": 1,
      "eventId": 1,
      "eventTitle": "Alumni Meet 2024",
      "userId": "USER_ABC123",
      "status": "GOING",
      "message": "Excited to attend!",
      "rsvpDate": "2024-12-06T12:00:00"
    }
  ]
}
```

### Event Invitations

#### Send Invitation
```http
POST /ijaa/api/v1/event/invitations/send
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "eventId": 1,
  "invitedUserIds": ["USER_XYZ789", "USER_DEF456"],
  "message": "You're invited to our alumni meet!"
}
```
**Authorization:** USER role required  
**Feature Flag:** events.invitations

**Response:**
```json
{
  "message": "Invitations sent successfully",
  "code": "200",
  "data": [
    {
      "invitationId": 1,
      "eventId": 1,
      "eventTitle": "Alumni Meet 2024",
      "invitedUserId": "USER_XYZ789",
      "invitedByUserId": "USER_ABC123",
      "status": "PENDING",
      "message": "You're invited to our alumni meet!",
      "sentAt": "2024-12-06T12:00:00"
    }
  ]
}
```

#### Get My Invitations
```http
GET /ijaa/api/v1/event/invitations/my-invitations
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.invitations

**Response:**
```json
{
  "message": "Invitations retrieved successfully",
  "code": "200",
  "data": [
    {
      "invitationId": 1,
      "eventId": 1,
      "eventTitle": "Alumni Meet 2024",
      "invitedUserId": "USER_ABC123",
      "invitedByUserId": "USER_XYZ789",
      "invitedByName": "Jane Smith",
      "status": "PENDING",
      "message": "You're invited to our alumni meet!",
      "sentAt": "2024-12-06T12:00:00",
      "read": false
    }
  ]
}
```

#### Accept Invitation
```http
POST /ijaa/api/v1/event/invitations/{eventId}/accept
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.invitations

**Response:**
```json
{
  "message": "Invitation accepted successfully",
  "code": "200",
  "data": "Invitation accepted"
}
```

### Event Banner

#### Upload Event Banner
```http
POST /ijaa/api/v1/event/banner/{eventId}
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

file: [banner_image_file]
```
**Authorization:** USER role required  
**Feature Flag:** events.banner

**Response:**
```json
{
  "message": "Banner uploaded successfully",
  "code": "200",
  "data": {
    "eventId": 1,
    "bannerUrl": "http://localhost:8083/api/v1/file/events/1/banner",
    "fileName": "banner_1.jpg",
    "fileSize": 1024000,
    "uploadedAt": "2024-12-06T12:00:00"
  }
}
```

#### Get Event Banner
```http
GET /ijaa/api/v1/event/banner/{eventId}
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.banner

**Response:**
```json
{
  "message": "Banner URL retrieved successfully",
  "code": "200",
  "data": {
    "eventId": 1,
    "bannerUrl": "http://localhost:8083/api/v1/file/events/1/banner",
    "fileName": "banner_1.jpg"
  }
}
```

### Advanced Event Search

#### Advanced Search
```http
POST /ijaa/api/v1/event/advanced-search/advanced
Authorization: Bearer <access_token>
Content-Type: application/json

{
  "query": "tech",
  "location": "New York",
  "startDate": "2024-12-01",
  "endDate": "2024-12-31",
  "eventType": "VIRTUAL",
  "organizer": "John",
  "minParticipants": 10,
  "maxParticipants": 100,
  "page": 0,
  "size": 10
}
```
**Authorization:** USER role required  
**Feature Flag:** events.search

**Response:**
```json
{
  "message": "Events found successfully",
  "code": "200",
  "data": {
    "content": [
      {
        "eventId": 2,
        "title": "Tech Talk: AI in 2024 - Updated",
        "description": "Updated discussion about AI trends and opportunities",
        "location": "Virtual Meeting Room",
        "startDate": "2024-12-30T15:00:00",
        "endDate": "2024-12-30T17:00:00",
        "organizer": "USER_ABC123",
        "organizerName": "John Doe",
        "maxParticipants": 75,
        "currentParticipants": 0,
        "status": "ACTIVE"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0
  }
}
```

#### Get Trending Events
```http
GET /ijaa/api/v1/event/advanced-search/trending?limit=10
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** events.search

**Response:**
```json
{
  "message": "Trending events retrieved successfully",
  "code": "200",
  "data": [
    {
      "eventId": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "location": "IIT JU Campus",
      "startDate": "2024-12-25T10:00:00",
      "endDate": "2024-12-25T18:00:00",
      "organizer": "USER_ABC123",
      "organizerName": "John Doe",
      "maxParticipants": 100,
      "currentParticipants": 25,
      "status": "ACTIVE",
      "engagementScore": 85.5
    }
  ]
}
```

## File Service APIs

### User Files

#### Upload Profile Photo
```http
POST /ijaa/api/v1/file/users/{userId}/profile-photo
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

file: [profile_photo_file]
```
**Authorization:** USER role required  
**Feature Flag:** file.upload

**Response:**
```json
{
  "message": "Profile photo uploaded successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "fileUrl": "http://localhost:8083/api/v1/file/users/USER_ABC123/profile-photo",
    "fileName": "profile_photo.jpg",
    "fileSize": 512000,
    "uploadedAt": "2024-12-06T12:00:00"
  }
}
```

#### Get Profile Photo URL
```http
GET /ijaa/api/v1/file/users/{userId}/profile-photo
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** file.access

**Response:**
```json
{
  "message": "Profile photo URL retrieved successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "fileUrl": "http://localhost:8083/api/v1/file/users/USER_ABC123/profile-photo",
    "fileName": "profile_photo.jpg"
  }
}
```

#### Delete Profile Photo
```http
DELETE /ijaa/api/v1/file/users/{userId}/profile-photo
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** file.upload

**Response:**
```json
{
  "message": "Profile photo deleted successfully",
  "code": "200",
  "data": null
}
```

#### Upload Cover Photo
```http
POST /ijaa/api/v1/file/users/{userId}/cover-photo
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

file: [cover_photo_file]
```
**Authorization:** USER role required  
**Feature Flag:** file.upload

**Response:**
```json
{
  "message": "Cover photo uploaded successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "fileUrl": "http://localhost:8083/api/v1/file/users/USER_ABC123/cover-photo",
    "fileName": "cover_photo.jpg",
    "fileSize": 1024000,
    "uploadedAt": "2024-12-06T12:00:00"
  }
}
```

### Event Banners

#### Upload Event Banner
```http
POST /ijaa/api/v1/file/events/{eventId}/banner
Authorization: Bearer <access_token>
Content-Type: multipart/form-data

file: [banner_image_file]
```
**Authorization:** USER role required  
**Feature Flag:** file.upload

**Response:**
```json
{
  "message": "Event banner uploaded successfully",
  "code": "200",
  "data": {
    "eventId": 1,
    "fileUrl": "http://localhost:8083/api/v1/file/events/1/banner",
    "fileName": "banner_1.jpg",
    "fileSize": 2048000,
    "uploadedAt": "2024-12-06T12:00:00"
  }
}
```

#### Get Event Banner URL
```http
GET /ijaa/api/v1/file/events/{eventId}/banner
Authorization: Bearer <access_token>
```
**Authorization:** USER role required  
**Feature Flag:** file.access

**Response:**
```json
{
  "message": "Event banner URL retrieved successfully",
  "code": "200",
  "data": {
    "eventId": 1,
    "fileUrl": "http://localhost:8083/api/v1/file/events/1/banner",
    "fileName": "banner_1.jpg"
  }
}
```

### Public File Access

#### Serve Profile Photo (Public)
```http
GET /ijaa/api/v1/file/users/{userId}/profile-photo/file/{fileName}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:** Binary file content with appropriate headers

#### Serve Cover Photo (Public)
```http
GET /ijaa/api/v1/file/users/{userId}/cover-photo/file/{fileName}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:** Binary file content with appropriate headers

#### Serve Event Banner (Public)
```http
GET /ijaa/api/v1/file/events/{eventId}/banner/file/{fileName}
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:** Binary file content with appropriate headers

## Config Service APIs

### Health Check
```http
GET /ijaa/api/v1/config/health/status
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Config service is healthy",
  "code": "200",
  "data": {
    "status": "healthy",
    "service": "Config Service",
    "timestamp": "2024-12-06T12:00:00"
  }
}
```

## Discovery Service APIs

### Health Check
```http
GET /ijaa/api/v1/discovery/health/status
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Discovery service is healthy",
  "code": "200",
  "data": {
    "status": "healthy",
    "service": "Discovery Service",
    "timestamp": "2024-12-06T12:00:00"
  }
}
```

## Health Check APIs

### User Service Health
```http
GET /ijaa/api/v1/user/health/status
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "User service is healthy",
  "code": "200",
  "data": {
    "status": "healthy",
    "service": "User Service",
    "timestamp": "2024-12-06T12:00:00",
    "version": "1.0.0"
  }
}
```

### Event Service Health
```http
GET /ijaa/api/v1/event/health/status
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "Event service is healthy",
  "code": "200",
  "data": {
    "status": "healthy",
    "service": "Event Service",
    "timestamp": "2024-12-06T12:00:00",
    "version": "1.0.0"
  }
}
```

### File Service Health
```http
GET /ijaa/api/v1/file/health/status
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

**Response:**
```json
{
  "message": "File service is healthy",
  "code": "200",
  "data": {
    "status": "healthy",
    "service": "File Service",
    "timestamp": "2024-12-06T12:00:00",
    "version": "1.0.0"
  }
}
```

## Error Responses

### Standard Error Format
All APIs return errors in the following format:

```json
{
  "message": "Error description",
  "code": "HTTP_STATUS_CODE",
  "data": null,
  "timestamp": "2024-12-06T12:00:00"
}
```

### Common HTTP Status Codes
- **200** - Success
- **201** - Created
- **400** - Bad Request
- **401** - Unauthorized
- **403** - Forbidden
- **404** - Not Found
- **409** - Conflict
- **500** - Internal Server Error

### Example Error Responses

#### 401 Unauthorized
```json
{
  "message": "Invalid or expired token",
  "code": "401",
  "data": null,
  "timestamp": "2024-12-06T12:00:00"
}
```

#### 404 Not Found
```json
{
  "message": "Event not found",
  "code": "404",
  "data": null,
  "timestamp": "2024-12-06T12:00:00"
}
```

#### 400 Bad Request
```json
{
  "message": "Validation failed",
  "code": "400",
  "data": {
    "errors": [
      {
        "field": "email",
        "message": "Email format is invalid"
      }
    ]
  },
  "timestamp": "2024-12-06T12:00:00"
}
```

## Feature Flags

The system uses feature flags to control access to various features. Check feature availability using:

```http
GET /ijaa/api/v1/user/admin/feature-flags/{feature-name}/enabled
```
**Authorization:** None (Public endpoint)  
**Feature Flag:** None

### Common Feature Flags
- `events` - Event management
- `events.creation` - Event creation
- `events.comments` - Event comments
- `events.participation` - Event participation
- `events.invitations` - Event invitations
- `events.banner` - Event banners
- `file-upload.profile-photo` - Profile photo upload
- `file-upload.cover-photo` - Cover photo upload
- `file-download` - File download
- `file-delete` - File deletion
- `search` - Basic search
- `search.advanced-filters` - Advanced search

## Rate Limiting

The gateway implements rate limiting to prevent abuse:
- **Authentication endpoints**: 5 requests per minute per IP
- **General API endpoints**: 100 requests per minute per user
- **File upload endpoints**: 10 requests per minute per user

## CORS Configuration

The gateway is configured to allow requests from:
- `http://localhost:3000` (Development frontend)
- `https://ijaa-frontend.vercel.app` (Production frontend)

## WebSocket Support

Real-time features are planned for future releases:
- Live event updates
- Real-time notifications
- Chat functionality

## Recent Updates (December 2024)

### Location Resource Cleanup
- **Simplified Location APIs**: Removed unnecessary endpoints, kept only essential country and city list endpoints
- **Database Schema Optimization**: Countries table now only contains `id` and `name` columns, Cities table contains `id`, `name`, and `countryId`
- **Removed Endpoints**:
  - `GET /location/countries/search` - Search countries
  - `GET /location/countries/{id}` - Get country by ID
  - `GET /location/countries/iso2/{iso2}` - Get country by ISO2
  - `GET /location/countries/{countryId}/cities/search` - Search cities by country
  - `GET /location/cities/search` - Search cities globally
  - `GET /location/cities/{id}` - Get city by ID

### Security Enhancements
- **Admin Login Security**: Added `@RequiresFeature("admin.auth")` to admin login endpoint
- **Profile Access Control**: Added `@PreAuthorize("hasRole('USER')")` to profile/{userId} endpoint
- **Profile Visibility Security**: Added `@RequiresFeature("user.profile")` to profile visibility updates
- **Enhanced Authorization**: Proper role-based access control and feature flag integration

### API Response Changes
- **Simplified Location Responses**: Country and city responses now contain only essential fields
- **Consistent Error Handling**: Standardized error responses across all endpoints
- **Updated Documentation**: Swagger documentation reflects simplified response structures

---

**Last Updated:** December 6, 2024  
**Version:** 1.1.0  
**Contact:** For API support, contact the development team.
