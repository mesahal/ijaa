# File URL Solution - IJAA File Service

## Problem Description

The file service was returning URLs with full file system paths like `/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file//home/sahal/ijaa-uploads/cover/7dd13a8d-5097-40bc-a07b-affe7222df28.jpeg` for profile and cover photos. This was happening because:

1. **Database stored full paths**: Some users had full file system paths stored in the database instead of just filenames
2. **URL generation bug**: The service was directly using the stored path without extracting just the filename
3. **Frontend compatibility**: The frontend expects clean, web-accessible URLs without file system paths

## Solution Implemented

### 1. Fixed URL Generation and Path Handling

**Before:**
```java
// Bug: Using full path from database directly
String photoUrl = "/ijaa/api/v1/users/" + userId + "/profile-photo/file/" + user.getProfilePhotoPath();
// Result: "/ijaa/api/v1/users/user123/profile-photo/file//home/sahal/ijaa-uploads/profile/abc123.jpg"
```

**After:**
```java
// Fix: Extract just the filename from stored path
String fileName = extractFileName(user.getProfilePhotoPath());
String photoUrl = "/ijaa/api/v1/users/" + userId + "/profile-photo/file/" + fileName;
// Result: "/ijaa/api/v1/users/user123/profile-photo/file/abc123.jpg"
```

### 2. Added Path Extraction Utility

Added a utility method to safely extract filenames from stored paths:

```java
/**
 * Extracts just the filename from a path, handling both full paths and just filenames
 */
private String extractFileName(String path) {
    if (path == null) {
        return null;
    }
    // If it's already just a filename (no path separators), return as is
    if (!path.contains("/") && !path.contains("\\")) {
        return path;
    }
    // Extract filename from path
    return Paths.get(path).getFileName().toString();
}
```

### 3. Fixed File Serving Endpoints

Updated file serving endpoints to use extracted filenames:

```java
@GetMapping("/{userId}/profile-photo/file/{fileName}")
public ResponseEntity<Resource> getProfilePhotoFile(String userId, String fileName)

@GetMapping("/{userId}/cover-photo/file/{fileName}")
public ResponseEntity<Resource> getCoverPhotoFile(String userId, String fileName)
```

### 4. Updated Gateway Configuration

Modified the gateway to handle file serving routes without authentication:

```java
// File service routes - for user profile and cover photo management
.route(p-> p
        .path("/ijaa/api/v1/users/**")
        .and()
        .not(route -> route.path("/ijaa/api/v1/users/*/profile-photo/file/**", "/ijaa/api/v1/users/*/cover-photo/file/**"))
        .filters(f-> f
                .filter(filter.apply(new AuthenticationFilter.Config()))
                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
        .uri("lb://FILE-SERVICE"))
// File serving routes - public endpoints for serving image files
.route(p-> p
        .path("/ijaa/api/v1/users/*/profile-photo/file/**", "/ijaa/api/v1/users/*/cover-photo/file/**")
        .filters(f-> f
                .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
        .uri("lb://FILE-SERVICE"))
```

### 5. Enhanced Service Layer

Updated service methods to handle path extraction:

```java
// Profile photo URL generation with path extraction
public PhotoUrlResponse getProfilePhotoUrl(String userId) {
    // ... user validation ...
    String fileName = extractFileName(user.getProfilePhotoPath());
    String photoUrl = "/ijaa/api/v1/users/" + userId + "/profile-photo/file/" + fileName;
    return new PhotoUrlResponse(photoUrl, "Profile photo found", true);
}

// Cover photo URL generation with path extraction  
public PhotoUrlResponse getCoverPhotoUrl(String userId) {
    // ... user validation ...
    String fileName = extractFileName(user.getCoverPhotoPath());
    String photoUrl = "/ijaa/api/v1/users/" + userId + "/cover-photo/file/" + fileName;
    return new PhotoUrlResponse(photoUrl, "Cover photo found", true);
}
```

## API Endpoints

### Get Profile Photo URL
```
GET /ijaa/api/v1/users/{userId}/profile-photo
```

**Response:**
```json
{
  "success": true,
  "message": "Profile photo URL retrieved successfully",
  "data": {
    "photoUrl": "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg",
    "message": "Profile photo found",
    "exists": true
  },
  "timestamp": 1756066464022
}
```

### Get Cover Photo URL
```
GET /ijaa/api/v1/users/{userId}/cover-photo
```

**Response:**
```json
{
  "success": true,
  "message": "Cover photo URL retrieved successfully",
  "data": {
    "photoUrl": "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg",
    "message": "Cover photo found",
    "exists": true
  },
  "timestamp": 1756066464022
}
```

### Serve Profile Photo File (Public)
```
GET /ijaa/api/v1/users/{userId}/profile-photo/file/{fileName}
```

**Response:** Image file with appropriate content type

### Serve Cover Photo File (Public)
```
GET /ijaa/api/v1/users/{userId}/cover-photo/file/{fileName}
```

**Response:** Image file with appropriate content type

## Frontend Integration

### Before (Not Working)
```javascript
// This would not work through the gateway
const imageUrl = "/uploads/profile/abc123.jpg";
```

### After (Working)
```javascript
// This works perfectly through the gateway
const imageUrl = "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg";
```

## Security Considerations

1. **Public File Serving**: File serving endpoints are public (no authentication required) to allow image display
2. **User Validation**: File serving validates that the user exists and has the photo
3. **File Path Security**: Uses proper file path resolution to prevent directory traversal attacks
4. **Content Type**: Returns appropriate content type headers for image files
5. **No Internal Path Exposure**: **CRITICAL** - Internal server file paths are never exposed in API responses

### Security Improvement: Removed Internal File Paths

**Before (Security Risk):**
```json
{
  "success": true,
  "data": {
    "message": "Profile photo uploaded successfully",
    "filePath": "/home/sahal/ijaa-uploads/profile/abc123.jpg",  // ❌ EXPOSES INTERNAL PATH
    "fileUrl": "/ijaa/api/v1/users/user123/profile-photo/file/abc123.jpg",
    "fileName": "abc123.jpg",
    "fileSize": 12345
  }
}
```

**After (Secure):**
```json
{
  "success": true,
  "data": {
    "message": "Profile photo uploaded successfully",
    "fileUrl": "/ijaa/api/v1/users/user123/profile-photo/file/abc123.jpg",  // ✅ ONLY PUBLIC URL
    "fileName": "abc123.jpg",
    "fileSize": 12345
  }
}
```

**Why This Matters:**
- **Information Disclosure**: Internal file paths reveal server structure
- **Directory Traversal Risk**: Attackers could use path information for attacks
- **Server Configuration Exposure**: Reveals where files are stored on the server
- **Security Best Practice**: Only expose public URLs, never internal paths

## Testing

### Unit Tests
- Updated existing tests to expect new URL format
- Added tests for new file serving methods
- Comprehensive error handling tests

### Integration Tests
- Added tests for file serving endpoints
- Verified public access (no authentication required)
- Tested error scenarios (file not found, user not found)

## Specific Fix for File System Path Issue

### Problem Identified
The API was returning URLs with full file system paths embedded:
```json
{
  "success": true,
  "data": {
    "photoUrl": "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file//home/sahal/ijaa-uploads/cover/7dd13a8d-5097-40bc-a07b-affe7222df28.jpeg"
  }
}
```

### Root Cause
1. **Database stored full paths**: Some users had full file system paths stored instead of just filenames
2. **Service bug**: The `getProfilePhotoUrl()` and `getCoverPhotoUrl()` methods were using stored paths directly without extracting filenames
3. **Upload bug**: The profile photo upload method had a bug in the old file deletion logic

### Solution Applied
1. **Added path extraction**: Created `extractFileName()` method to safely extract filenames from any path format
2. **Fixed URL generation**: Updated both profile and cover photo URL methods to use extracted filenames
3. **Fixed upload bug**: Corrected the profile photo upload deletion logic
4. **Updated validation**: File serving endpoints now validate against extracted filenames

### Result
Now the API returns clean, frontend-compatible URLs:
```json
{
  "success": true,
  "data": {
    "photoUrl": "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/7dd13a8d-5097-40bc-a07b-affe7222df28.jpeg"
  }
}
```

## Benefits

1. **Frontend Compatibility**: URLs now work correctly through the gateway
2. **Security**: Proper validation and security measures in place
3. **Scalability**: Easy to extend for other file types
4. **Maintainability**: Clean separation between URL generation and file serving
5. **Performance**: Direct file serving without additional processing
6. **Data Integrity**: Handles legacy data with full paths gracefully

## Migration Guide

### For Frontend Developers

1. **Update Image URLs**: Use the new URL format returned by the API
2. **No Authentication**: File serving endpoints don't require authentication
3. **Error Handling**: Handle 404 responses for missing images

### For Backend Developers

1. **URL Format**: All photo URLs now follow the gateway pattern
2. **File Serving**: New endpoints handle actual file serving
3. **Testing**: Updated test expectations to match new URL format

## Example Usage

### Frontend Implementation
```javascript
// Get profile photo URL
const response = await fetch('/ijaa/api/v1/users/user123/profile-photo');
const data = await response.json();

if (data.data.exists) {
    // Use the URL directly in img src
    const imgElement = document.createElement('img');
    imgElement.src = data.data.photoUrl; // This will work through the gateway
    document.body.appendChild(imgElement);
}
```

### Backend Integration
```java
// The service now returns gateway-compatible URLs
PhotoUrlResponse response = fileService.getProfilePhotoUrl(userId);
// response.getPhotoUrl() returns: "/ijaa/api/v1/users/user123/profile-photo/file/abc123.jpg"
```

## Conclusion

This solution provides a robust, secure, and scalable approach to file serving in the IJAA microservices architecture. The frontend can now properly display images using URLs that work seamlessly through the API Gateway, while maintaining proper security and validation measures.
