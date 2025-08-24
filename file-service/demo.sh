#!/bin/bash

echo "🚀 IJAA File Service Demo"
echo "========================="

# Check if the service is running
echo "📡 Checking if file service is running..."
if curl -s http://localhost:8083/actuator/health > /dev/null 2>&1; then
    echo "✅ File service is running on port 8083"
else
    echo "❌ File service is not running. Please start it first:"
    echo "   cd file-service && ./mvnw spring-boot:run"
    exit 1
fi

echo ""
echo "📋 Available Endpoints:"
echo "  POST /api/v1/users/{userId}/profile-photo  - Upload profile photo"
echo "  POST /api/v1/users/{userId}/cover-photo    - Upload cover photo"
echo "  GET  /api/v1/users/{userId}/profile-photo  - Get profile photo URL"
echo "  GET  /api/v1/users/{userId}/cover-photo    - Get cover photo URL"
echo "  DELETE /api/v1/users/{userId}/profile-photo - Delete profile photo"
echo "  DELETE /api/v1/users/{userId}/cover-photo   - Delete cover photo"

echo ""
echo "🧪 Test User ID: demo-user-123"

# Test getting profile photo URL (should return no photo)
echo ""
echo "📸 Testing get profile photo URL (no photo exists)..."
curl -s -X GET "http://localhost:8083/api/v1/users/demo-user-123/profile-photo" | jq '.'

# Test getting cover photo URL (should return no photo)
echo ""
echo "🖼️  Testing get cover photo URL (no photo exists)..."
curl -s -X GET "http://localhost:8083/api/v1/users/demo-user-123/cover-photo" | jq '.'

echo ""
echo "✅ Demo completed successfully!"
echo ""
echo "💡 To test file uploads, you can use tools like Postman or curl with multipart/form-data"
echo "   Example: curl -X POST -F 'file=@/path/to/image.jpg' http://localhost:8083/api/v1/users/demo-user-123/profile-photo"
