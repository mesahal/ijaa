# File Service - IJAA Platform

## Overview

The File Service is a microservice responsible for handling user profile and cover photo uploads, storage, and retrieval in the IJAA (IIT Jodhpur Alumni Association) platform.

## Features

- **Profile Photo Management**: Upload, update, and delete user profile photos
- **Cover Photo Management**: Upload, update, and delete user cover photos
- **File Validation**: Validates file types (JPG, JPEG, PNG, WEBP) and size limits (5MB max)
- **Automatic File Replacement**: Replaces old photos when new ones are uploaded
- **Static File Serving**: Serves uploaded files as static resources
- **Configurable Storage**: Easy configuration for local file system or cloud storage
- **Comprehensive Error Handling**: Proper exception handling and error responses

## API Endpoints

### Profile Photo Endpoints

#### Upload/Update Profile Photo
```
POST /api/v1/users/{userId}/profile-photo
Content-Type: multipart/form-data

Parameters:
- userId (path): User ID
- file (form): Image file (JPG, JPEG, PNG, WEBP, max 5MB)
```

#### Get Profile Photo URL
```
GET /api/v1/users/{userId}/profile-photo

Parameters:
- userId (path): User ID

Response:
{
  "success": true,
  "message": "Profile photo URL retrieved successfully",
  "data": {
    "photoUrl": "/uploads/profile/filename.jpg",
    "message": "Profile photo found",
    "exists": true
  }
}
```

#### Delete Profile Photo
```
DELETE /api/v1/users/{userId}/profile-photo

Parameters:
- userId (path): User ID
```

### Cover Photo Endpoints

#### Upload/Update Cover Photo
```
POST /api/v1/users/{userId}/cover-photo
Content-Type: multipart/form-data

Parameters:
- userId (path): User ID
- file (form): Image file (JPG, JPEG, PNG, WEBP, max 5MB)
```

#### Get Cover Photo URL
```
GET /api/v1/users/{userId}/cover-photo

Parameters:
- userId (path): User ID

Response:
{
  "success": true,
  "message": "Cover photo URL retrieved successfully",
  "data": {
    "photoUrl": "/uploads/cover/filename.jpg",
    "message": "Cover photo found",
    "exists": true
  }
}
```

#### Delete Cover Photo
```
DELETE /api/v1/users/{userId}/cover-photo

Parameters:
- userId (path): User ID
```

## Configuration

### Application Properties

```yaml
spring:
  application:
    name: file-service
  datasource:
    url: jdbc:postgresql://localhost:5432/ijaa_files
    username: root
    password: Admin@123
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB

server:
  port: 8083

# File storage configuration
file:
  storage:
    base-path: /uploads
    profile-photos-path: /uploads/profile
    cover-photos-path: /uploads/cover
    allowed-image-types: jpg,jpeg,png,webp
    max-file-size-mb: 5
```

### Environment Variables

- `FILE_STORAGE_BASE_PATH`: Base path for file storage
- `FILE_STORAGE_PROFILE_PHOTOS_PATH`: Path for profile photos
- `FILE_STORAGE_COVER_PHOTOS_PATH`: Path for cover photos
- `FILE_STORAGE_MAX_FILE_SIZE_MB`: Maximum file size in MB

## Database Schema

### User Entity
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    profile_photo_path VARCHAR(255),
    cover_photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## File Storage

### Local File System
- **Profile Photos**: `/uploads/profile/`
- **Cover Photos**: `/uploads/cover/`
- **File Naming**: UUID-based unique filenames
- **Directory Creation**: Automatic directory creation if not exists

### Future Cloud Storage Support
The service is designed to easily switch to cloud storage providers:
- AWS S3
- Google Cloud Storage
- Azure Blob Storage
- DigitalOcean Spaces

## Error Handling

### Common Error Responses

#### Invalid File Type
```json
{
  "success": false,
  "message": "File type not allowed. Allowed types: jpg, jpeg, png, webp",
  "data": null,
  "timestamp": 1640995200000
}
```

#### File Too Large
```json
{
  "success": false,
  "message": "File size exceeds maximum limit of 5MB",
  "data": null,
  "timestamp": 1640995200000
}
```

#### User Not Found
```json
{
  "success": false,
  "message": "User not found with userId: user123",
  "data": null,
  "timestamp": 1640995200000
}
```

## Development Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker (optional)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd file-service
   ```

2. **Create database**
   ```sql
   CREATE DATABASE ijaa_files;
   ```

3. **Configure application.yml**
   Update database connection and file storage paths in `src/main/resources/application.yml`

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Create upload directories**
   ```bash
   mkdir -p /uploads/profile /uploads/cover
   ```

### Testing

#### Run Unit Tests
```bash
./mvnw test
```

#### Run Integration Tests
```bash
./mvnw test -Dtest="*IntegrationTest"
```

#### Run All Tests with Coverage
```bash
./mvnw test jacoco:report
```

## Testing Examples

### Upload Profile Photo (cURL)
```bash
curl -X POST \
  http://localhost:8083/api/v1/users/user123/profile-photo \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/photo.jpg'
```

### Get Profile Photo URL (cURL)
```bash
curl -X GET \
  http://localhost:8083/api/v1/users/user123/profile-photo
```

### Delete Profile Photo (cURL)
```bash
curl -X DELETE \
  http://localhost:8083/api/v1/users/user123/profile-photo
```

## Integration with Other Services

### User Service Integration
The file service integrates with the user service by:
- Storing file paths in the user database
- Providing photo URLs for user profiles
- Handling user creation for file operations

### Gateway Service Integration
The file service is accessible through the API gateway at:
- `http://localhost:8000/api/v1/users/{userId}/profile-photo`
- `http://localhost:8000/api/v1/users/{userId}/cover-photo`

## Security Considerations

- **File Type Validation**: Only allows image files
- **File Size Limits**: Prevents large file uploads
- **Unique Filenames**: UUID-based naming prevents conflicts
- **Path Traversal Protection**: Validates file paths
- **CORS Configuration**: Proper CORS setup for web clients

## Monitoring and Logging

### Log Levels
- **DEBUG**: File operations, path creation
- **INFO**: Upload/download operations
- **WARN**: File deletion failures
- **ERROR**: Storage exceptions, validation errors

### Metrics
- File upload success/failure rates
- Storage usage statistics
- Response time metrics

## Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/file-service-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: file-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: file-service
  template:
    metadata:
      labels:
        app: file-service
    spec:
      containers:
      - name: file-service
        image: ijaa/file-service:latest
        ports:
        - containerPort: 8083
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "production"
```

## Troubleshooting

### Common Issues

1. **File Upload Fails**
   - Check file size limits
   - Verify file type is allowed
   - Ensure upload directories exist

2. **Photos Not Displaying**
   - Verify static resource configuration
   - Check file permissions
   - Validate file paths in database

3. **Database Connection Issues**
   - Verify PostgreSQL is running
   - Check connection credentials
   - Ensure database exists

### Log Analysis
```bash
# View application logs
tail -f logs/file-service.log

# Search for upload errors
grep "ERROR.*upload" logs/file-service.log

# Monitor file operations
grep "INFO.*upload\|INFO.*download" logs/file-service.log
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is part of the IJAA platform and follows the same licensing terms.
