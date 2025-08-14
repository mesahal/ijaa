#!/bin/bash

echo "🧪 Testing Admin Management API Validation Fixes"
echo "================================================"

# Test 1: Admin cannot deactivate themselves
echo "✅ Test 1: Admin cannot deactivate themselves"
cd user-service && ./mvnw test -Dtest=AdminServiceTest#shouldThrowAdminSelfDeactivationExceptionWhenAdminTriesToDeactivateThemselves

# Test 2: Cannot activate already active admin
echo "✅ Test 2: Cannot activate already active admin"
./mvnw test -Dtest=AdminServiceTest#shouldThrowAdminAlreadyActiveExceptionWhenActivatingAlreadyActiveAdmin

# Test 3: Cannot deactivate already inactive admin
echo "✅ Test 3: Cannot deactivate already inactive admin"
./mvnw test -Dtest=AdminServiceTest#shouldThrowAdminAlreadyInactiveExceptionWhenDeactivatingAlreadyInactiveAdmin

# Test 4: Successfully deactivate admin when not self-deactivation
echo "✅ Test 4: Successfully deactivate admin when not self-deactivation"
./mvnw test -Dtest=AdminServiceTest#shouldSuccessfullyDeactivateAdminWhenValidRequestAndNotSelfDeactivation

# Test 5: Successfully activate admin when admin is inactive
echo "✅ Test 5: Successfully activate admin when admin is inactive"
./mvnw test -Dtest=AdminServiceTest#shouldSuccessfullyActivateAdminWhenValidRequestAndAdminIsInactive

echo ""
echo "🎉 All Admin Validation Tests Completed!"
echo "========================================"
