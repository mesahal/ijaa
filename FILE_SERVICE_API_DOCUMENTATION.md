# File Service API Documentation

## Overview
The File Service handles file upload, management, and serving for the IJAA (IIT JU Alumni Association) microservices system. It provides endpoints for user profile photos, cover photos, and event banners with proper authentication and public file serving capabilities.

## Base Information
- **Service Name**: File Service
- **Port**: 8083 (Direct), 8080 (Gateway)
- **Base URL**: `/api/v1/file`
- **Gateway URL**: `/ijaa/api/v1/file`
- **Authentication**: JWT Bearer Token (except public file serving endpoints)

## API Endpoints

### 1. User Profile Photo Management

#### 1.1 Upload Profile Photo
**Endpoint**: `POST /ijaa/api/v1/file/users/{userId}/profile-photo`

**Description**: Upload or update a user's profile photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-upload.profile-photo`

**Request**:
- **Method**: POST
- **Content-Type**: multipart/form-data
- **Path Parameters**:
  - `userId` (string): User ID (e.g., "e76dbb07-7790-4862-95b0-e0aa96f7b2a3")
- **Body Parameters**:
  - `file` (file): Profile photo file (JPG, JPEG, PNG, WEBP, max 5MB)

**Response**:
```json
{
  "success": true,
  "message": "Profile photo uploaded successfully",
  "data": {
    "message": "Profile photo uploaded successfully",
    "fileUrl": "/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg",
    "fileName": "abc123.jpg",
    "fileSize": 12345
  },
  "timestamp": 1640995200000
}
```

**Error Responses**:
- **400**: Invalid file type or size
- **500**: File storage error

---

#### 1.2 Get Profile Photo URL
**Endpoint**: `GET /ijaa/api/v1/file/users/{userId}/profile-photo`

**Description**: Retrieve the URL of a user's profile photo. Returns null if no photo exists.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-download`

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `userId` (string): User ID

**Response (Photo Exists)**:
```json
{
  "success": true,
  "message": "Profile photo URL retrieved successfully",
  "data": {
    "photoUrl": "/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg",
    "message": "Profile photo found",
    "exists": true
  },
  "timestamp": 1640995200000
}
```

**Response (No Photo)**:
```json
{
  "success": true,
  "message": "Profile photo URL retrieved successfully",
  "data": {
    "photoUrl": null,
    "message": "No profile photo found",
    "exists": false
  },
  "timestamp": 1640995200000
}
```

---

#### 1.3 Delete Profile Photo
**Endpoint**: `DELETE /ijaa/api/v1/file/users/{userId}/profile-photo`

**Description**: Delete a user's profile photo. Removes the file from storage and clears the database reference.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-delete`

**Request**:
- **Method**: DELETE
- **Path Parameters**:
  - `userId` (string): User ID

**Response**:
```json
{
  "success": true,
  "message": "Profile photo deleted successfully",
  "data": null,
  "timestamp": 1640995200000
}
```

---

#### 1.4 Serve Profile Photo File (Public)
**Endpoint**: `GET /ijaa/api/v1/file/users/{userId}/profile-photo/file/{fileName}`

**Description**: Serve the actual profile photo file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**

**Authentication**: None (Public)

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `userId` (string): User ID
  - `fileName` (string): File name (e.g., "abc123.jpg")

**Response**:
- **200**: Image file (Content-Type: image/jpeg)
- **404**: Profile photo file not found

**Browser URL Example**:
```
http://localhost:8080/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg
```

---

### 2. User Cover Photo Management

#### 2.1 Upload Cover Photo
**Endpoint**: `POST /ijaa/api/v1/file/users/{userId}/cover-photo`

**Description**: Upload or update a user's cover photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-upload.cover-photo`

**Request**:
- **Method**: POST
- **Content-Type**: multipart/form-data
- **Path Parameters**:
  - `userId` (string): User ID
- **Body Parameters**:
  - `file` (file): Cover photo file (JPG, JPEG, PNG, WEBP, max 5MB)

**Response**:
```json
{
  "success": true,
  "message": "Cover photo uploaded successfully",
  "data": {
    "message": "Cover photo uploaded successfully",
    "fileUrl": "/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg",
    "fileName": "abc123.jpg",
    "fileSize": 12345
  },
  "timestamp": 1640995200000
}
```

**Error Responses**:
- **400**: Invalid file type or size
- **500**: File storage error

---

#### 2.2 Get Cover Photo URL
**Endpoint**: `GET /ijaa/api/v1/file/users/{userId}/cover-photo`

**Description**: Retrieve the URL of a user's cover photo. Returns null if no photo exists.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-download`

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `userId` (string): User ID

**Response (Photo Exists)**:
```json
{
  "success": true,
  "message": "Cover photo URL retrieved successfully",
  "data": {
    "photoUrl": "/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg",
    "message": "Cover photo found",
    "exists": true
  },
  "timestamp": 1640995200000
}
```

**Response (No Photo)**:
```json
{
  "success": true,
  "message": "Cover photo URL retrieved successfully",
  "data": {
    "photoUrl": null,
    "message": "No cover photo found",
    "exists": false
  },
  "timestamp": 1640995200000
}
```

---

#### 2.3 Delete Cover Photo
**Endpoint**: `DELETE /ijaa/api/v1/file/users/{userId}/cover-photo`

**Description**: Delete a user's cover photo. Removes the file from storage and clears the database reference.

**Authentication**: Required (JWT Bearer Token)

**Feature Flag**: `file-delete`

**Request**:
- **Method**: DELETE
- **Path Parameters**:
  - `userId` (string): User ID

**Response**:
```json
{
  "success": true,
  "message": "Cover photo deleted successfully",
  "data": null,
  "timestamp": 1640995200000
}
```

---

#### 2.4 Serve Cover Photo File (Public)
**Endpoint**: `GET /ijaa/api/v1/file/users/{userId}/cover-photo/file/{fileName}`

**Description**: Serve the actual cover photo file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**

**Authentication**: None (Public)

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `userId` (string): User ID
  - `fileName` (string): File name (e.g., "abc123.jpg")

**Response**:
- **200**: Image file (Content-Type: image/jpeg)
- **404**: Cover photo file not found

**Browser URL Example**:
```
http://localhost:8080/ijaa/api/v1/file/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg
```

---

### 3. Event Banner Management

#### 3.1 Upload Event Banner
**Endpoint**: `POST /ijaa/api/v1/file/events/{eventId}/banner`

**Description**: Upload or update an event banner image. Supports JPG, JPEG, PNG, WEBP formats up to 5MB.

**Authentication**: Required (JWT Bearer Token)

**Request**:
- **Method**: POST
- **Content-Type**: multipart/form-data
- **Path Parameters**:
  - `eventId` (string): Event ID (e.g., "1")
- **Body Parameters**:
  - `file` (file): Event banner file (JPG, JPEG, PNG, WEBP, max 5MB)

**Response**:
```json
{
  "success": true,
  "message": "Event banner uploaded successfully",
  "data": {
    "message": "Event banner uploaded successfully",
    "fileUrl": "/ijaa/api/v1/file/events/1/banner/file/abc123.jpg",
    "fileName": "abc123.jpg",
    "fileSize": 12345
  },
  "timestamp": 1640995200000
}
```

**Error Responses**:
- **400**: Invalid file type or size
- **500**: File storage error

---

#### 3.2 Get Event Banner URL
**Endpoint**: `GET /ijaa/api/v1/file/events/{eventId}/banner`

**Description**: Retrieve the URL of an event banner. Returns null if no banner exists.

**Authentication**: Required (JWT Bearer Token)

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `eventId` (string): Event ID

**Response (Banner Exists)**:
```json
{
  "success": true,
  "message": "Event banner URL retrieved successfully",
  "data": {
    "photoUrl": "/ijaa/api/v1/file/events/1/banner/file/abc123.jpg",
    "message": "Event banner found",
    "exists": true
  },
  "timestamp": 1640995200000
}
```

**Response (No Banner)**:
```json
{
  "success": true,
  "message": "Event banner URL retrieved successfully",
  "data": {
    "photoUrl": null,
    "message": "No event banner found",
    "exists": false
  },
  "timestamp": 1640995200000
}
```

---

#### 3.3 Delete Event Banner
**Endpoint**: `DELETE /ijaa/api/v1/file/events/{eventId}/banner`

**Description**: Delete an event banner image.

**Authentication**: Required (JWT Bearer Token)

**Request**:
- **Method**: DELETE
- **Path Parameters**:
  - `eventId` (string): Event ID

**Response**:
```json
{
  "success": true,
  "message": "Event banner deleted successfully",
  "data": null,
  "timestamp": 1640995200000
}
```

**Error Responses**:
- **404**: Event banner not found
- **500**: File storage error

---

#### 3.4 Serve Event Banner File (Public)
**Endpoint**: `GET /ijaa/api/v1/file/events/{eventId}/banner/file/{fileName}`

**Description**: Serve the actual event banner file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**

**Authentication**: None (Public)

**Request**:
- **Method**: GET
- **Path Parameters**:
  - `eventId` (string): Event ID
  - `fileName` (string): File name (e.g., "abc123.jpg")

**Response**:
- **200**: Image file (Content-Type: image/jpeg)
- **404**: Event banner file not found

**Browser URL Example**:
```
http://localhost:8080/ijaa/api/v1/file/events/1/banner/file/abc123.jpg
```

---

### 4. Health Check Endpoints

#### 4.1 Basic Health Check
**Endpoint**: `GET /ijaa/api/v1/file/health/status`

**Description**: Check if the File Service is running.

**Authentication**: None (Public)

**Request**:
- **Method**: GET

**Response**:
```json
{
  "status": "healthy",
  "service": "File Service",
  "message": "File Service is running successfully",
  "timestamp": "2024-01-15T10:30:00",
  "version": "1.0.0",
  "javaVersion": "17.0.2",
  "springVersion": "3.x"
}
```

---

#### 4.2 Database Health Check
**Endpoint**: `GET /ijaa/api/v1/file/health/database`

**Description**: Check database connectivity and basic operations.

**Authentication**: None (Public)

**Request**:
- **Method**: GET

**Response**:
```json
{
  "status": "healthy",
  "message": "Database connection successful",
  "database": "PostgreSQL",
  "metrics": {
    "eventBanners": 5
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## Data Transfer Objects (DTOs)

### FileUploadResponse
```json
{
  "message": "string",
  "fileUrl": "string",
  "fileName": "string",
  "fileSize": "number"
}
```

### PhotoUrlResponse
```json
{
  "photoUrl": "string",
  "message": "string",
  "exists": "boolean"
}
```

### ApiResponse<T>
```json
{
  "success": "boolean",
  "message": "string",
  "data": "T",
  "timestamp": "number"
}
```

---

## File Upload Specifications

### Supported File Types
- **JPG/JPEG**: Joint Photographic Experts Group
- **PNG**: Portable Network Graphics
- **WEBP**: Web Picture format

### File Size Limits
- **Maximum Size**: 5MB per file
- **Validation**: Server-side validation for file type and size

### File Storage
- **Storage Type**: File system storage
- **Directory Structure**: Organized by file type (profile-photos, cover-photos, event-banners)
- **File Naming**: Unique filenames generated using UUID + timestamp

---

## Authentication & Authorization

### Protected Endpoints
All file management endpoints (upload, get URL, delete) require JWT Bearer token authentication:
```
Authorization: Bearer <jwt_token>
```

### Public Endpoints
File serving endpoints are public and do not require authentication:
- `GET /ijaa/api/v1/file/users/{userId}/profile-photo/file/{fileName}`
- `GET /ijaa/api/v1/file/users/{userId}/cover-photo/file/{fileName}`
- `GET /ijaa/api/v1/file/events/{eventId}/banner/file/{fileName}`

### Feature Flags
Certain operations require specific feature flags to be enabled:
- `file-upload.profile-photo`: Profile photo uploads
- `file-upload.cover-photo`: Cover photo uploads
- `file-download`: File URL retrieval
- `file-delete`: File deletion

---

## Error Handling

### Common Error Responses

#### 400 Bad Request
```json
{
  "success": false,
  "message": "File is required. Please provide a valid image file.",
  "data": null,
  "timestamp": 1640995200000
}
```

#### 404 Not Found
```json
{
  "success": false,
  "message": "Profile photo file not found",
  "data": null,
  "timestamp": 1640995200000
}
```

#### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Failed to upload profile photo: File storage error",
  "data": null,
  "timestamp": 1640995200000
}
```

---

## Frontend Integration Examples

### Upload Profile Photo
```javascript
const formData = new FormData();
formData.append('file', fileInput.files[0]);

const response = await fetch('/ijaa/api/v1/file/users/USER_ABC123/profile-photo', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  body: formData
});

const data = await response.json();
console.log('File URL:', data.data.fileUrl);
```

### Get Profile Photo URL
```javascript
const response = await fetch('/ijaa/api/v1/file/users/USER_ABC123/profile-photo', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});

const data = await response.json();
if (data.data.exists) {
  const imageUrl = `http://localhost:8080${data.data.photoUrl}`;
  document.getElementById('profile-photo').src = imageUrl;
}
```

### Display Image Directly
```html
<img src="http://localhost:8080/ijaa/api/v1/file/users/USER_ABC123/profile-photo/file/abc123.jpg" 
     alt="Profile Photo" 
     onerror="this.src='/default-avatar.png'" />
```

---

## Gateway Routing

All requests are routed through the gateway at port 8080 with the `/ijaa` prefix. The gateway handles:

1. **Authentication**: JWT token validation for protected endpoints
2. **Public Access**: Direct access to file serving endpoints
3. **Load Balancing**: Distribution across multiple file service instances
4. **CORS**: Cross-origin resource sharing configuration

### Gateway URL Structure
```
http://localhost:8080/ijaa/api/v1/file/{endpoint}
```

---

## Configuration

### Environment Variables
```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ijaa_file_db
SPRING_DATASOURCE_USERNAME=ijaa_user
SPRING_DATASOURCE_PASSWORD=ijaa_password

# JWT Configuration
JWT_SECRET=your-secret-key

# Eureka Configuration
EUREKA_INSTANCE_HOSTNAME=localhost
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/

# Production Server URL
PRODUCTION_SERVER_URL=http://localhost:8000
```

### File Storage Configuration
```yaml
file:
  upload:
    path: /tmp/uploads
    max-size: 10MB
```

---

## Testing

### Health Check Test
```bash
curl http://localhost:8080/ijaa/api/v1/file/health/status
```

### Upload Test
```bash
curl -X POST \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/image.jpg" \
  http://localhost:8080/ijaa/api/v1/file/users/USER_ABC123/profile-photo
```

### Public File Access Test
```bash
curl http://localhost:8080/ijaa/api/v1/file/users/USER_ABC123/profile-photo/file/abc123.jpg
```

---

## Swagger Documentation

The File Service provides interactive API documentation via Swagger UI:

- **Local Development**: `http://localhost:8083/swagger-ui.html`
- **Gateway Access**: `http://localhost:8080/swagger-ui.html`

The Swagger documentation includes:
- Complete API endpoint descriptions
- Request/response examples
- Authentication requirements
- Feature flag dependencies
- Interactive testing capabilities

---

## Version History

### v1.0.0 (Current)
- ✅ Complete file upload and management system
- ✅ User profile and cover photo support
- ✅ Event banner management
- ✅ Public file serving endpoints
- ✅ JWT authentication integration
- ✅ Feature flag system
- ✅ Health check endpoints
- ✅ Swagger API documentation
- ✅ Gateway routing support
- ✅ **Fixed URL generation** - All URLs now use correct `/ijaa/api/v1/file/` prefix

---

## Support

For technical support or questions about the File Service API, please refer to:
- **Project Documentation**: `project-context.md`
- **API Documentation**: This file
- **Swagger UI**: Interactive API documentation
- **Health Endpoints**: Service status monitoring

