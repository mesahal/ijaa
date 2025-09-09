# Health Endpoint Routing Guide

## Overview
The IJAA Gateway Service now provides service-specific health endpoints that route to different microservices, ensuring each service responds with its own health information.

## Current Routing Configuration

### ✅ Service-Specific Health Endpoints

#### 1. User Service Health
- **URL Pattern**: `/ijaa/api/v1/health/user/**`
- **Target Service**: `lb://user-service`
- **Response**: User Service health information
- **Example**: 
  ```bash
  curl http://localhost:8000/ijaa/api/v1/health/user/status
  curl http://localhost:8000/ijaa/api/v1/health/user/database
  ```

#### 2. Event Service Health
- **URL Pattern**: `/ijaa/api/v1/health/event/**`
- **Target Service**: `lb://event`
- **Response**: Event Service health information
- **Example**:
  ```bash
  curl http://localhost:8000/ijaa/api/v1/health/event/status
  curl http://localhost:8000/ijaa/api/v1/health/event/database
  ```

#### 3. File Service Health
- **URL Pattern**: `/ijaa/api/v1/health/file/**`
- **Target Service**: `lb://file-service`
- **Response**: File Service health information
- **Example**:
  ```bash
  curl http://localhost:8000/ijaa/api/v1/health/file/status
  curl http://localhost:8000/ijaa/api/v1/health/file/database
  ```

### 🔄 Legacy Health Endpoints (Backward Compatibility)
- **URL Pattern**: `/ijaa/api/v1/health/**` (generic)
- **Target Service**: `lb://user-service`
- **Response**: User Service health information
- **Example**:
  ```bash
  curl http://localhost:8000/ijaa/api/v1/health/status
  curl http://localhost:8000/ijaa/api/v1/health/database
  ```

## Gateway Configuration

### Route Order (Important!)
The routes are configured in the correct order to ensure proper precedence:

1. **Service-specific health routes** (come first)
2. **Generic health route** (for backward compatibility)
3. **Catch-all route** (for other API patterns)

### Key Configuration Details

```java
// Service-specific routes - MUST come before generic route
.route(p-> p
    .path("/ijaa/api/v1/health/user/**")
    .filters(f-> f
        .rewritePath("/ijaa/api/v1/health/user/(?<segment>.*)","/api/v1/health/${segment}")
        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
    .uri("lb://user-service"))

.route(p-> p
    .path("/ijaa/api/v1/health/event/**")
    .filters(f-> f
        .rewritePath("/ijaa/api/v1/health/event/(?<segment>.*)","/api/v1/health/${segment}")
        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
    .uri("lb://event"))

.route(p-> p
    .path("/ijaa/api/v1/health/file/**")
    .filters(f-> f
        .rewritePath("/ijaa/api/v1/health/file/(?<segment>.*)","/api/v1/health/${segment}")
        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
    .uri("lb://file-service"))

// Generic health route (for backward compatibility)
.route(p-> p
    .path("/ijaa/api/v1/health/**")
    .filters(f-> f
        .rewritePath("/ijaa/(?<segment>.*)","/${segment}")
        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
    .uri("lb://user-service"))
```

## Testing

### Test Script
Use the provided test script to verify all endpoints:
```bash
./test-health-routing.sh
```

### Manual Testing
Test each service-specific endpoint:

```bash
# User Service
curl http://localhost:8000/ijaa/api/v1/health/user/status
curl http://localhost:8000/ijaa/api/v1/health/user/database

# Event Service
curl http://localhost:8000/ijaa/api/v1/health/event/status
curl http://localhost:8000/ijaa/api/v1/health/event/database

# File Service
curl http://localhost:8000/ijaa/api/v1/health/file/status
curl http://localhost:8000/ijaa/api/v1/health/file/database

# Legacy endpoints
curl http://localhost:8000/ijaa/api/v1/health/status
curl http://localhost:8000/ijaa/api/v1/health/database
```

## Expected Responses

### User Service Health
```json
{
  "status": "healthy",
  "service": "User Service",
  "message": "User Service is running successfully",
  "timestamp": "2025-09-04T23:26:04.244272111",
  "version": "1.0.0",
  "javaVersion": "21.0.8",
  "springVersion": "3.x"
}
```

### Event Service Health
```json
{
  "status": "healthy",
  "service": "Event Service",
  "message": "Event Service is running successfully",
  "timestamp": "2025-09-04T23:26:04.244272111",
  "version": "1.0.0",
  "javaVersion": "21.0.8",
  "springVersion": "3.x"
}
```

### File Service Health
```json
{
  "status": "healthy",
  "service": "File Service",
  "message": "File Service is running successfully",
  "timestamp": "2025-09-04T23:26:04.244272111",
  "version": "1.0.0",
  "javaVersion": "21.0.8",
  "springVersion": "3.x"
}
```

## Troubleshooting

### Common Issues

1. **All endpoints show same service**: Gateway service needs to be restarted after configuration changes
2. **404 errors**: Check if the target services are running and registered with Eureka
3. **Routing conflicts**: Ensure route order is correct (service-specific routes must come before generic routes)

### Verification Steps

1. **Check Gateway Logs**: Look for routing information in gateway service logs
2. **Verify Service Registration**: Ensure all services are registered with Eureka
3. **Test Direct Endpoints**: Test each service directly to verify they're responding
4. **Check Route Order**: Verify the route configuration order in GatewayConfig.java

## Deployment Notes

### Required Actions
1. **Restart Gateway Service**: After configuration changes
2. **Verify Service Discovery**: Ensure Eureka is running and services are registered
3. **Test All Endpoints**: Use the test script to verify routing works correctly

### Production Considerations
- Service discovery will use `lb://service-name` instead of hardcoded localhost:port
- Health check endpoints are public (no authentication required)
- Consider adding rate limiting for health endpoints in production
