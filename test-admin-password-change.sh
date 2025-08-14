#!/bin/bash

# Admin Password Change API Test Script
# This script tests the admin password change functionality

echo "🔐 Testing Admin Password Change API"
echo "====================================="

# Base URL
BASE_URL="http://localhost:8081/api/v1/admin"

# Test admin credentials (you may need to adjust these based on your setup)
ADMIN_EMAIL="admin@ijaa.com"
ADMIN_PASSWORD="admin123"

echo "📋 Step 1: Admin Login to get JWT token"
echo "----------------------------------------"

# Login to get JWT token
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"$ADMIN_EMAIL\",
    \"password\": \"$ADMIN_PASSWORD\"
  }")

echo "Login Response: $LOGIN_RESPONSE"

# Extract JWT token (basic extraction - in production, use proper JSON parsing)
TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo "❌ Failed to get JWT token. Please check admin credentials."
    echo "💡 You may need to create an admin account first or check the credentials."
    exit 1
fi

echo "✅ JWT Token obtained: ${TOKEN:0:20}..."

echo ""
echo "📋 Step 2: Test Password Change API"
echo "-----------------------------------"

# Test password change
PASSWORD_CHANGE_RESPONSE=$(curl -s -X PUT "$BASE_URL/change-password" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{
    \"currentPassword\": \"$ADMIN_PASSWORD\",
    \"newPassword\": \"newSecurePassword123\",
    \"confirmPassword\": \"newSecurePassword123\"
  }")

echo "Password Change Response: $PASSWORD_CHANGE_RESPONSE"

# Check if password change was successful
if echo "$PASSWORD_CHANGE_RESPONSE" | grep -q '"code":"200"'; then
    echo "✅ Password change successful!"
    
    echo ""
    echo "📋 Step 3: Verify new password works"
    echo "------------------------------------"
    
    # Try to login with new password
    NEW_LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d "{
        \"email\": \"$ADMIN_EMAIL\",
        \"password\": \"newSecurePassword123\"
      }")
    
    echo "New Login Response: $NEW_LOGIN_RESPONSE"
    
    if echo "$NEW_LOGIN_RESPONSE" | grep -q '"token"'; then
        echo "✅ New password works correctly!"
        
        echo ""
        echo "📋 Step 4: Change password back to original"
        echo "-------------------------------------------"
        
        # Get new token for password change back
        NEW_TOKEN=$(echo "$NEW_LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
        
        # Change password back to original
        REVERT_RESPONSE=$(curl -s -X PUT "$BASE_URL/change-password" \
          -H "Content-Type: application/json" \
          -H "Authorization: Bearer $NEW_TOKEN" \
          -d "{
            \"currentPassword\": \"newSecurePassword123\",
            \"newPassword\": \"$ADMIN_PASSWORD\",
            \"confirmPassword\": \"$ADMIN_PASSWORD\"
          }")
        
        echo "Revert Response: $REVERT_RESPONSE"
        
        if echo "$REVERT_RESPONSE" | grep -q '"code":"200"'; then
            echo "✅ Password successfully reverted to original!"
        else
            echo "❌ Failed to revert password"
        fi
    else
        echo "❌ New password login failed"
    fi
else
    echo "❌ Password change failed"
    echo "💡 This might be expected if the admin account doesn't exist or credentials are incorrect"
fi

echo ""
echo "🎉 Admin Password Change API Test Complete!"
echo "==========================================="
