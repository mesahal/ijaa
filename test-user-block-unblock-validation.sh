#!/bin/bash

echo "🧪 Testing User Block/Unblock API Validation Fixes"
echo "=================================================="

# Test 1: Cannot block already blocked user
echo "✅ Test 1: Cannot block already blocked user"
cd user-service && ./mvnw test -Dtest=AdminServiceTest#shouldThrowUserAlreadyBlockedExceptionWhenBlockingAlreadyBlockedUser

# Test 2: Cannot unblock already unblocked user
echo "✅ Test 2: Cannot unblock already unblocked user"
./mvnw test -Dtest=AdminServiceTest#shouldThrowUserAlreadyUnblockedExceptionWhenUnblockingAlreadyUnblockedUser

# Test 3: Successfully block user when not blocked
echo "✅ Test 3: Successfully block user when not blocked"
./mvnw test -Dtest=AdminServiceTest#shouldSuccessfullyBlockUserWhenUserIsNotBlocked

# Test 4: Successfully unblock user when blocked
echo "✅ Test 4: Successfully unblock user when blocked"
./mvnw test -Dtest=AdminServiceTest#shouldSuccessfullyUnblockUserWhenUserIsBlocked

# Test 5: Handle non-existent user for block
echo "✅ Test 5: Handle non-existent user for block"
./mvnw test -Dtest=AdminServiceTest#shouldThrowRuntimeExceptionWhenBlockingNonExistentUser

# Test 6: Handle non-existent user for unblock
echo "✅ Test 6: Handle non-existent user for unblock"
./mvnw test -Dtest=AdminServiceTest#shouldThrowRuntimeExceptionWhenUnblockingNonExistentUser

echo ""
echo "🎉 All User Block/Unblock Validation Tests Completed!"
echo "====================================================="
