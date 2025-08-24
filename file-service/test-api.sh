#!/bin/bash

echo "Testing File Service API Error Handling..."

# Test 1: No file provided - should return 400 with proper error message
echo "Test 1: Testing no file scenario..."
curl -X 'POST' \
  'http://localhost:8083/api/v1/users/test-user-123/cover-photo' \
  -H 'accept: application/json' \
  -H 'Content-Type: multipart/form-data' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo -e "\n\nTest 2: Testing with empty file..."
# Test 2: Empty file - should return 400
curl -X 'POST' \
  'http://localhost:8083/api/v1/users/test-user-123/cover-photo' \
  -H 'accept: application/json' \
  -F 'file=@/dev/null' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

echo -e "\n\nTest 3: Testing with valid file..."
# Test 3: Valid file - should work
echo "test content" > test.txt
curl -X 'POST' \
  'http://localhost:8083/api/v1/users/test-user-123/cover-photo' \
  -H 'accept: application/json' \
  -F 'file=@test.txt' \
  -w "\nHTTP Status: %{http_code}\n" \
  -s

rm -f test.txt
echo -e "\n\nAPI testing completed."
