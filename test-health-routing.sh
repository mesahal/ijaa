#!/bin/bash

echo "🧪 Testing Health Endpoint Routing..."
echo "====================================="

BASE_URL="http://localhost:8000"

echo ""
echo "1️⃣ Testing User Service Health Endpoints (NEW):"
echo "   GET /ijaa/api/v1/health/user/status"
curl -s "$BASE_URL/ijaa/api/v1/health/user/status" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/user/status")"

echo ""
echo "   GET /ijaa/api/v1/health/user/database"
curl -s "$BASE_URL/ijaa/api/v1/health/user/database" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/user/database")"

echo ""
echo "2️⃣ Testing Event Service Health Endpoints (NEW):"
echo "   GET /ijaa/api/v1/health/event/status"
curl -s "$BASE_URL/ijaa/api/v1/health/event/status" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/event/status")"

echo ""
echo "   GET /ijaa/api/v1/health/event/database"
curl -s "$BASE_URL/ijaa/api/v1/health/event/database" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/event/database")"

echo ""
echo "3️⃣ Testing File Service Health Endpoints (NEW):"
echo "   GET /ijaa/api/v1/health/file/status"
curl -s "$BASE_URL/ijaa/api/v1/health/file/status" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/file/status")"

echo ""
echo "   GET /ijaa/api/v1/health/file/database"
curl -s "$BASE_URL/ijaa/api/v1/health/file/database" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/file/database")"

echo ""
echo "4️⃣ Testing Legacy Health Endpoints (Still go to User Service):"
echo "   GET /ijaa/api/v1/health/status"
curl -s "$BASE_URL/ijaa/api/v1/health/status" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/status")"

echo ""
echo "   GET /ijaa/api/v1/health/database"
curl -s "$BASE_URL/ijaa/api/v1/health/database" | jq '.service' 2>/dev/null || echo "   Response: $(curl -s "$BASE_URL/ijaa/api/v1/health/database")"

echo ""
echo "✅ Health Endpoint Routing Test Complete!"
echo ""
echo "Current Routing Configuration:"
echo "- /ijaa/api/v1/health/user/** → User Service (NEW - Service-specific)"
echo "- /ijaa/api/v1/health/event/** → Event Service (NEW - Service-specific)"
echo "- /ijaa/api/v1/health/file/** → File Service (NEW - Service-specific)"
echo "- /ijaa/api/v1/health/** → User Service (Legacy - Generic route)"
echo ""
echo "Expected Results:"
echo "- User Service endpoints should show 'User Service is running'"
echo "- Event Service endpoints should show 'Event Service is running'"
echo "- File Service endpoints should show 'File Service is running'"
echo "- Legacy endpoints should show 'User Service is running'"
echo ""
echo "Note: Make sure to restart the gateway service after configuration changes!"
