# IJAA API Endpoints - Complete List
**Base URL**: `http://localhost:8000/ijaa`

## 🔐 Authentication APIs
**Base Path**: `/api/v1/auth/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/auth/login` | User authentication | `user.login` |
| POST | `http://localhost:8000/ijaa/api/v1/auth/register` | User registration | `user.registration` |
| POST | `http://localhost:8000/ijaa/api/v1/auth/refresh` | Refresh access token | `user.refresh` |
| POST | `http://localhost:8000/ijaa/api/v1/auth/logout` | Logout and invalidate refresh token | `user.logout` |
| POST | `http://localhost:8000/ijaa/api/v1/auth/change-password` | Password management | `user.password-change` |

## 👤 User Management APIs
**Base Path**: `/api/v1/users/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}` | Get user profile | `user.profile` |
| PUT | `http://localhost:8000/ijaa/api/v1/users/{userId}` | Update profile information | `user.profile` |
| PUT | `http://localhost:8000/ijaa/api/v1/users/{userId}/profile` | Update profile visibility | `user.profile` |
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}/experiences` | Get user experiences | `user.experiences` |
| POST | `http://localhost:8000/ijaa/api/v1/users/{userId}/experiences` | Add experience | `user.experiences` |
| PUT | `http://localhost:8000/ijaa/api/v1/users/{userId}/experiences/{experienceId}` | Update experience | `user.experiences` |
| DELETE | `http://localhost:8000/ijaa/api/v1/users/{userId}/experiences/{experienceId}` | Delete experience | `user.experiences` |
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}/interests` | Get user interests | `user.interests` |
| POST | `http://localhost:8000/ijaa/api/v1/users/{userId}/interests` | Add interest | `user.interests` |
| PUT | `http://localhost:8000/ijaa/api/v1/users/{userId}/interests/{interestId}` | Update interest | `user.interests` |
| DELETE | `http://localhost:8000/ijaa/api/v1/users/{userId}/interests/{interestId}` | Delete interest | `user.interests` |
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}/settings` | Get user settings | `user.settings` |
| PUT | `http://localhost:8000/ijaa/api/v1/users/{userId}/settings` | Update user settings | `user.settings` |
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}/settings/theme` | Get current user theme | `user.settings` |
| GET | `http://localhost:8000/ijaa/api/v1/users/{userId}/settings/themes` | Get available theme options | `user.settings` |
| POST | `http://localhost:8000/ijaa/api/v1/users/search` | Alumni search functionality | `alumni.search` |
| GET | `http://localhost:8000/ijaa/api/v1/users/search/metadata` | Get alumni search metadata | `alumni.search` |

## 🛡️ Admin Management APIs
**Base Path**: `/api/v1/admin/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/admin/login` | Admin authentication | `admin.auth` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/logout` | Admin logout | `admin.logout` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/admins` | Admin registration | `admin.auth` |
| GET  | `http://localhost:8000/ijaa/api/v1/admin/admins` | Get all admins | `admin.features` |
| PUT  | `http://localhost:8000/ijaa/api/v1/admin/change-password` | Change admin password | `admin.auth` |
| GET  | `http://localhost:8000/ijaa/api/v1/admin/profile` | Get admin profile | `admin.auth` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/admins/{adminId}/deactivate` | Deactivate admin | `admin.features` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/admins/{adminId}/activate` | Activate admin | `admin.features` |
| GET  | `http://localhost:8000/ijaa/api/v1/admin/dashboard` | Dashboard statistics | `admin.features` |
| GET  | `http://localhost:8000/ijaa/api/v1/admin/users` | Get all users | `admin.user-management` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/users/{userId}/block` | Block user | `admin.user-management` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/users/{userId}/unblock` | Unblock user | `admin.user-management` |
| DELETE | `http://localhost:8000/ijaa/api/v1/admin/users/{userId}` | Delete user | `admin.user-management` |

## 🚩 Feature Flag Management APIs
**Base Path**: `/api/v1/admin/feature-flags/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/` | Get all feature flags | `admin.features` |
| GET | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/{name}` | Get specific feature flag | `admin.features` |
| POST | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/` | Create new feature flag | `admin.features` |
| PUT | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/{name}` | Update feature flag | `admin.features` |
| DELETE | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/{name}` | Delete feature flag | `admin.features` |
| GET | `http://localhost:8000/ijaa/api/v1/admin/feature-flags/{name}/enabled` | Check feature flag status (Public) | `system.health` |

## 🌍 Location APIs
**Base Path**: `/api/v1/locations/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/locations/countries` | Get all countries | `user.location` |
| GET | `http://localhost:8000/ijaa/api/v1/locations/countries/{countryId}/cities` | Get cities by country | `user.location` |

## 🎉 Event Management APIs
**Base Path**: `/api/v1/events/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/events/my-events` | Get user's created events | `events` |
| GET | `http://localhost:8000/ijaa/api/v1/events/my-events/active` | Get user's active events | `events` |
| GET | `http://localhost:8000/ijaa/api/v1/events/all-events` | Get all active events | `events` |
| GET | `http://localhost:8000/ijaa/api/v1/events/all-events/{eventId}` | Get specific event details | `events` |
| GET | `http://localhost:8000/ijaa/api/v1/events/my-events/{eventId}` | Get user's specific event | `events` |
| POST | `http://localhost:8000/ijaa/api/v1/events/create` | Create new event | `events.creation` |
| PUT | `http://localhost:8000/ijaa/api/v1/events/my-events/{eventId}` | Update user's event | `events.update` |
| DELETE | `http://localhost:8000/ijaa/api/v1/events/my-events/{eventId}` | Delete user's event | `events.delete` |
| POST | `http://localhost:8000/ijaa/api/v1/events/search` | Search events with filters | `search` |

## 📝 Event Posts APIs
**Base Path**: `/api/v1/events/posts/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/events/posts` | Create event post (multipart/form-data) | `events.posts.create` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/posts/event/{eventId}` | Get event posts (paginated) | `events.posts` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/posts/event/{eventId}/all` | Get all event posts | `events.posts` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/posts/{postId}` | Get specific post | `events.posts` |
| PUT  | `http://localhost:8000/ijaa/api/v1/events/posts/{postId}` | Update post | `events.posts.update` |
| DELETE | `http://localhost:8000/ijaa/api/v1/events/posts/{postId}` | Delete post | `events.posts.delete` |
| POST | `http://localhost:8000/ijaa/api/v1/events/posts/{postId}/like` | Toggle post like | `events.posts.like` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/posts/user/{username}` | Get user's posts (paginated) | `events.posts` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/posts/user/{username}/event/{eventId}` | Get user's posts for event | `events.posts` |

## 🎫 Event Participation APIs
**Base Path**: `/api/v1/events/participation/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/events/participation/rsvp` | RSVP to events | `events.participation` |
| PUT | `http://localhost:8000/ijaa/api/v1/events/participation/{eventId}/rsvp` | Update RSVP status | `events.participation` |
| DELETE | `http://localhost:8000/ijaa/api/v1/events/participation/{eventId}/rsvp` | Cancel RSVP | `events.participation` |
| GET | `http://localhost:8000/ijaa/api/v1/events/participation/{eventId}/my-participation` | Get user's participation for event | `events.participation` |
| GET | `http://localhost:8000/ijaa/api/v1/events/participation/{eventId}/participants` | Get event participants | `events.participation` |
| GET | `http://localhost:8000/ijaa/api/v1/events/participation/{eventId}/participants/{status}` | Get participants by status | `events.participation` |
| GET | `http://localhost:8000/ijaa/api/v1/events/participation/my-participations` | Get user's event participations | `events.participation` |

## 💬 Event Comments APIs
**Base Path**: `/api/v1/events/comments/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/events/comments/` | Add post comment | `events.comments` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/comments/post/{postId}` | Get post comments with nested replies | `events.comments` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/comments/{commentId}` | Get specific comment | `events.comments` |
| PUT  | `http://localhost:8000/ijaa/api/v1/events/comments/{commentId}` | Update comment | `events.comments` |
| DELETE | `http://localhost:8000/ijaa/api/v1/events/comments/{commentId}` | Delete comment | `events.comments` |
| POST | `http://localhost:8000/ijaa/api/v1/events/comments/{commentId}/like` | Toggle comment like | `events.comments` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/comments/recent/post/{postId}` | Get recent comments for post | `events.comments` |
| GET  | `http://localhost:8000/ijaa/api/v1/events/comments/popular/post/{postId}` | Get popular comments for post | `events.comments` |

## 🖼️ Event Banner APIs
**Base Path**: `/api/v1/events/banner/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/events/banner/{eventId}` | Upload/Update event banner | `events.banner` |
| GET | `http://localhost:8000/ijaa/api/v1/events/banner/{eventId}` | Get event banner URL | `events.banner` |
| DELETE | `http://localhost:8000/ijaa/api/v1/events/banner/{eventId}` | Delete event banner | `events.banner` |

## 🔍 Advanced Event Search API
**Base Path**: `/api/v1/events/advanced-search/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/events/advanced-search/advanced` | Unified advanced event search with comprehensive filters (location, organizer, event type, dates, online status, upcoming events, pagination, sorting, limit) | `search.advanced-filters` |

### Search Parameters
The unified search endpoint supports the following parameters in the request body:
- **query**: General search text (searches title and description)
- **location**: Filter by event location
- **eventType**: Filter by event type
- **startDate**: Filter events starting after this date
- **endDate**: Filter events ending before this date
- **organizerName**: Filter by organizer name
- **isOnline**: Filter by online/offline events
- **privacy**: Filter by privacy setting (PUBLIC, PRIVATE, INVITE_ONLY)
- **minParticipants**: Minimum participant count
- **maxParticipants**: Maximum participant count
- **upcomingOnly**: Boolean to filter only upcoming events
- **sortBy**: Sort field (CREATED_AT, START_DATE, POPULARITY, DISTANCE)
- **sortOrder**: Sort order (ASC, DESC)
- **page**: Page number for pagination (default: 0)
- **size**: Page size (default: 20)
- **limit**: Optional limit for results (alternative to pagination)

## 📁 File Management APIs
**Base Path**: `/api/v1/files/users/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/profile-photo` | Upload profile photo | `file-upload.profile-photo` |
| GET | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/profile-photo` | Get profile photo URL | `file-download` |
| DELETE | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/profile-photo` | Delete profile photo | `file-delete` |
| POST | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/cover-photo` | Upload cover photo | `file-upload.cover-photo` |
| GET | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/cover-photo` | Get cover photo URL | `file-download` |
| DELETE | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/cover-photo` | Delete cover photo | `file-delete` |
| GET | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/profile-photo/file/**` | Serve profile photo file (Public) | `file-download` |
| GET | `http://localhost:8000/ijaa/api/v1/files/users/{userId}/cover-photo/file/**` | Serve cover photo file (Public) | `file-download` |

## 🎨 Event File APIs
**Base Path**: `/api/v1/files/events/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| POST | `http://localhost:8000/ijaa/api/v1/files/events/{eventId}/banner` | Upload event banner | `events.banner` |
| GET | `http://localhost:8000/ijaa/api/v1/files/events/{eventId}/banner` | Get event banner URL | `file-download` |
| DELETE | `http://localhost:8000/ijaa/api/v1/files/events/{eventId}/banner` | Delete event banner | `file-delete` |
| GET | `http://localhost:8000/ijaa/api/v1/files/events/{eventId}/banner/file/{fileName}` | Serve event banner file (Public) | `file-download` |

## 🖼️ Post Media APIs
**Base Path**: `/api/v1/files/posts/`

| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET  | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media` | Get all post media (protected) | `events.posts.media` |
| POST | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media` | Upload post media (multipart/form-data) | `events.posts.media` |
| GET  | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media/{fileName}` | Get post media URL | `file-download` |
| GET  | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media/file/{fileName}` | Serve post media file (Public) | `file-download` |
| DELETE | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media/{fileName}` | Delete specific post media (requires X-Username header) | `file-delete` |
| DELETE | `http://localhost:8000/ijaa/api/v1/files/posts/{postId}/media` | Delete all post media for a post (requires X-Username header) | `file-delete` |

**Note**: DELETE endpoints require both `Authorization` header and `X-Username` header for proper authentication and authorization.

## 🚧 Gateway Token Blacklist APIs
**Base Path**: `/api/v1/token/`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `http://localhost:8000/ijaa/api/v1/token/blacklist` | Blacklist a specific access token (body: `{ token, userId, userType }`) |
| POST | `http://localhost:8000/ijaa/api/v1/token/blacklist-all` | Blacklist all tokens for a user (body: `{ userId, userType }`) |
| GET  | `http://localhost:8000/ijaa/api/v1/token/is-blacklisted?token=...` | Check if a token is blacklisted |

## 🏥 Health Check APIs

### User Service Health Endpoints
| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/health/status` | Basic health check | `system.health` |
| GET | `http://localhost:8000/ijaa/api/v1/health/database` | Database health check | `system.health` |

### Event Service Health Endpoints
| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/events/health/status` | Basic health check | `system.health` |
| GET | `http://localhost:8000/ijaa/api/v1/events/health/database` | Database health check | `system.health` |

### File Service Health Endpoints
| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/files/health/status` | Basic health check | `system.health` |
| GET | `http://localhost:8000/ijaa/api/v1/files/health/database` | Database health check | `system.health` |

### Config Service Health Endpoints (Critical Infrastructure)
| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/config/health/status` | Basic health check | None |
| GET | `http://localhost:8000/ijaa/api/v1/config/health/config` | Configuration health check | None |
| GET | `http://localhost:8000/ijaa/api/v1/config/health/test` | Test endpoint | None |

### Discovery Service Health Endpoints (Critical Infrastructure)
| Method | Endpoint | Description | Feature Flag |
|--------|----------|-------------|--------------|
| GET | `http://localhost:8000/ijaa/api/v1/discovery/health/status` | Basic health check | None |
| GET | `http://localhost:8000/ijaa/api/v1/discovery/health/registry` | Service registry health check | None |
| GET | `http://localhost:8000/ijaa/api/v1/discovery/health/test` | Test endpoint | None |

## 📊 API Summary

- **Total Endpoints**: 82
- **Authentication Required**: 68
- **Public Endpoints**: 14
- **Feature Flags**: 44 hierarchical flags
- **Services**: 5 (User, Event, File, Config, Discovery)

## 🔑 Authentication

- **Access Token**: 60-minute expiration
- **Refresh Token**: 7-day expiration (HttpOnly cookie)
- **Header**: `Authorization: Bearer <token>`

### Standard Response Wrapper
- All endpoints return a consistent wrapper unless otherwise noted:
```json
{
  "message": "<human readable message>",
  "code": "<http status code as string>",
  "data": <payload or null>
}
```

### Content Types and Auth
- JSON endpoints: `Content-Type: application/json`
- File upload endpoints: `Content-Type: multipart/form-data`
- Protected routes require `Authorization: Bearer <accessToken>` header.
- Feature flags listed per endpoint must be enabled.
- Some endpoints require additional headers (e.g., `X-Username` for user-specific operations)

## 🌐 Public Endpoints (No Authentication)

1. `GET /ijaa/api/v1/admin/feature-flags/{name}/enabled`
2. `POST /ijaa/api/v1/auth/refresh`
3. `GET /ijaa/api/v1/files/users/*/profile-photo/file/**`
4. `GET /ijaa/api/v1/files/users/*/cover-photo/file/**`
5. `GET /ijaa/api/v1/files/events/{eventId}/banner/file/{fileName}`
6. `GET /ijaa/api/v1/files/posts/*/media/file/**`
7. All Config Service health endpoints
8. All Discovery Service health endpoints

---

# Detailed Examples

Below are request/response examples to integrate each API group. Replace host with your environment. Base prefix is `/ijaa`.

## Authentication

- Login
  - POST `/api/v1/auth/login`
  - Request
```json
{
  "username": "john.doe",
  "password": "password123"
}
```
  - Response
```json
{
  "message": "Login successful",
  "code": "200",
  "data": {
    "accessToken": "<JWT>",
    "tokenType": "Bearer",
    "userId": "USER_ABC123"
  }
}
```

- Register
  - POST `/api/v1/auth/register`
  - Request
```json
{
  "username": "john.doe",
  "password": "password123"
}
```
  - Response: same shape as Login with new `userId`.

- Refresh
  - POST `/api/v1/auth/refresh`
  - Reads refresh token from HttpOnly cookie or body field `{"refreshToken": "..."}`
  - Response
```json
{
  "message": "Token refreshed successfully",
  "code": "200",
  "data": {
    "accessToken": "<JWT>",
    "tokenType": "Bearer",
    "userId": "USER_ABC123"
  }
}
```

- Logout (protected)
  - POST `/api/v1/auth/logout`
  - Side effects: access token blacklisted via gateway; refresh token revoked; clears refresh cookie.
  - Response
```json
{
  "message": "Logout successful",
  "code": "200",
  "data": null
}
```

## User Profile

- Get Profile (protected)
  - GET `/api/v1/users/{userId}`
  - Response
```json
{
  "message": "Profile fetched successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "name": "John Doe",
    "profession": "Software Engineer",
    "cityId": 1,
    "countryId": 1,
    "cityName": "New York",
    "countryName": "United States",
    "bio": "...",
    "phone": "+1-555-...",
    "linkedIn": "https://...",
    "website": "https://...",
    "batch": "2020",
    "facebook": "https://...",
    "email": "john@example.com",
    "showPhone": true,
    "showLinkedIn": true,
    "showWebsite": true,
    "showEmail": true,
    "showFacebook": false,
    "connections": 150
  }
}
```

- Update Profile (protected)
  - PUT `/api/v1/users/{userId}`
  - Request body matches `ProfileDto`.

- Update Profile Visibility (protected)
  - PUT `/api/v1/users/{userId}/profile`
  - Request body contains the `show*` boolean fields from `ProfileDto`.

## User Experiences

- Add Experience (protected)
  - POST `/api/v1/users/{userId}/experiences`
  - Request
```json
{
  "userId": "USER_ABC123",
  "title": "Senior Software Engineer",
  "company": "Tech Corp",
  "period": "2022-2024",
  "description": "..."
}
```
  - Response contains created `ExperienceDto`.

## User Interests

- Add Interest (protected)
  - POST `/api/v1/users/{userId}/interests`
  - Request
```json
{ "interest": "Machine Learning" }
```

## User Settings

- Get Settings (protected)
  - GET `/api/v1/users/{userId}/settings`
  - Response
```json
{
  "message": "User settings retrieved successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123",
    "theme": "DEVICE"
  }
}
```

- Update Settings (protected)
  - PUT `/api/v1/users/{userId}/settings`
  - Request
```json
{ "theme": "LIGHT" }
```

## Alumni Search (protected)

- Search
  - POST `/api/v1/users/search`
  - Request (see `AlumniSearchRequest`)
```json
{
  "searchQuery": "software engineer",
  "batch": "2020",
  "profession": "Technology",
  "cityId": 163,
  "countryId": 101,
  "sortBy": "relevance",
  "page": 0,
  "size": 12
}
```

## Admin

- Login
  - POST `/api/v1/admin/login`
  - Response
```json
{
  "message": "Admin login successful",
  "code": "200",
  "data": {
    "accessToken": "<JWT>",
    "refreshToken": "<random>",
    "adminId": 1,
    "name": "Administrator",
    "email": "admin@ijaa.com",
    "role": "ADMIN",
    "active": true
  }
}
```

## Events

- Create Event (protected)
  - POST `/api/v1/events/create`
  - Request matches `EventRequest`.

## Event Posts (protected)

- Create Post
  - POST `/api/v1/events/posts` (multipart/form-data)
  - Fields: `eventId` (number), `content` (string, optional), `files[]` (0..n)
  - Response returns `EventPostResponse` including `mediaFiles` and `recentComments`.

## Event Comments (protected)

- Add Comment
  - POST `/api/v1/events/comments/`
  - Request
```json
{
  "postId": 1,
  "content": "Great post!",
  "authorName": "John Doe",
  "userId": "USER_ABC123"
}
```

## Event Participation (protected)

- RSVP
  - POST `/api/v1/events/participation/rsvp`
  - Request
```json
{ "eventId": 1, "status": "GOING", "message": "See you there" }
```

## File: User Photos

- Upload Profile Photo (protected)
  - POST `/api/v1/files/users/{userId}/profile-photo` (multipart/form-data)
  - Parts: `file`
  - Response
```json
{
  "message": "Profile photo uploaded successfully",
  "code": "200",
  "data": {
    "message": "Profile photo uploaded successfully",
    "fileUrl": "/ijaa/api/v1/files/users/USER_ABC123/profile-photo/file/abc.jpg",
    "fileName": "abc.jpg",
    "fileSize": 12345
  }
}
```

## File: Event Banner (public serve)

- GET `/api/v1/files/events/{eventId}/banner/file/{fileName}`
  - Returns the binary file with appropriate content type.

## File: Post Media

- Upload (protected)
  - POST `/api/v1/files/posts/{postId}/media` (multipart/form-data)
  - Parts: `file`, `mediaType` (IMAGE|VIDEO)

- Serve (public)
  - GET `/api/v1/files/posts/{postId}/media/file/{fileName}`
  - Returns binary content.

## Gateway Token Blacklist

- Blacklist Access Token
  - POST `/api/v1/token/blacklist`
  - Request
```json
{ "token": "<accessToken>", "userId": "USER_ABC123", "userType": "USER" }
```

## Health

- GET `/api/v1/config/health/status`
  - Response
```json
{ "status": "healthy", "service": "Config Service", "message": "...", "timestamp": "..." }
```

---

**Generated**: October 2025  
**Base URL**: `http://localhost:8000/ijaa`  
**Gateway Port**: 8000  
**Total APIs**: 89 endpoints across 5 services


