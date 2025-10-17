#!/bin/bash

# Test script for file service endpoints
echo "Testing File Service Endpoints..."

# Test the test endpoint
echo "1. Testing test endpoint..."
curl -X GET "http://localhost:8083/api/v1/files/posts/test" \
  -H "Content-Type: application/json" \
  -v

echo -e "\n\n2. Testing get all post media endpoint..."
curl -X GET "http://localhost:8083/api/v1/files/posts/29/media" \
  -H "Content-Type: application/json" \
  -v

echo -e "\n\n3. Testing upload post media endpoint (this should fail without proper auth)..."
curl -X POST "http://localhost:8083/api/v1/files/posts/29/media" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@test_image.png" \
  -F "mediaType=IMAGE" \
  -H "X-Username: testuser" \
  -v

echo -e "\n\nTest completed!"
