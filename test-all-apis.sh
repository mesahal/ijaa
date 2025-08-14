#!/bin/bash

# 🧪 IJAA Comprehensive API Testing Script
# This script tests all APIs across User Service and Event Service

set -e

echo "🧪 Starting Comprehensive API Testing for IJAA Microservices"
echo "=========================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to test API endpoint
test_api() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    
    print_status "Testing: $description"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Content-Type: application/json" \
            -d "$data" 2>/dev/null || echo -e "\n000")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" 2>/dev/null || echo -e "\n000")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        print_success "✅ $description (HTTP $http_code)"
        echo "Response: $body" | head -c 200
        echo "..."
    else
        print_error "❌ $description (HTTP $http_code)"
        echo "Response: $body"
    fi
    echo
}

# Function to test API with authentication
test_api_with_auth() {
    local method=$1
    local url=$2
    local data=$3
    local description=$4
    local token=$5
    
    print_status "Testing: $description"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $token" \
            -d "$data" 2>/dev/null || echo -e "\n000")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Authorization: Bearer $token" 2>/dev/null || echo -e "\n000")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        print_success "✅ $description (HTTP $http_code)"
        echo "Response: $body" | head -c 200
        echo "..."
    else
        print_error "❌ $description (HTTP $http_code)"
        echo "Response: $body"
    fi
    echo
}

# Check if services are running
check_services() {
    print_status "Checking service availability..."
    
    # Check User Service
    if curl -s http://localhost:8081/actuator/health >/dev/null 2>&1; then
        print_success "User Service is running on port 8081"
    else
        print_error "User Service is not running on port 8081"
        exit 1
    fi
    
    # Check Event Service
    if curl -s http://localhost:8082/actuator/health >/dev/null 2>&1; then
        print_success "Event Service is running on port 8082"
    else
        print_error "Event Service is not running on port 8082"
        exit 1
    fi
    
    echo
}

# Test User Service APIs
test_user_service() {
    echo "🔐 Testing User Service APIs"
    echo "============================"
    
    # Test user registration
    test_api "POST" "http://localhost:8081/api/v1/user/auth/signup" \
        '{"username":"testuser123","password":"password123","email":"testuser123@example.com"}' \
        "User Registration"
    
    # Test user login
    test_api "POST" "http://localhost:8081/api/v1/user/auth/signin" \
        '{"username":"john.doe","password":"password123"}' \
        "User Login"
    
    # Test admin registration
    test_api "POST" "http://localhost:8081/api/v1/admin/auth/signup" \
        '{"name":"Test Admin","email":"admin@test.com","password":"admin123","role":"ADMIN"}' \
        "Admin Registration"
    
    # Test admin login
    test_api "POST" "http://localhost:8081/api/v1/admin/auth/signin" \
        '{"email":"admin@test.com","password":"admin123"}' \
        "Admin Login"
    
    # Test get all users (admin endpoint)
    test_api "GET" "http://localhost:8081/api/v1/admin/users" \
        "" \
        "Get All Users (Admin)"
    
    # Test get user profile
    test_api "GET" "http://localhost:8081/api/v1/user/profile" \
        "" \
        "Get User Profile"
    
    # Test update user profile
    test_api "PUT" "http://localhost:8081/api/v1/user/profile" \
        '{"name":"John Doe Updated","profession":"Senior Software Engineer","location":"Mumbai, India","bio":"Updated bio"}' \
        "Update User Profile"
    
    # Test alumni search
    test_api "GET" "http://localhost:8081/api/v1/user/alumni/search?query=software" \
        "" \
        "Alumni Search"
    
    # Test get user interests
    test_api "GET" "http://localhost:8081/api/v1/user/interests" \
        "" \
        "Get User Interests"
    
    # Test add user interest
    test_api "POST" "http://localhost:8081/api/v1/user/interests" \
        '{"interest":"Blockchain"}' \
        "Add User Interest"
    
    # Test get user experiences
    test_api "GET" "http://localhost:8081/api/v1/user/experiences" \
        "" \
        "Get User Experiences"
    
    # Test add user experience
    test_api "POST" "http://localhost:8081/api/v1/user/experiences" \
        '{"title":"Senior Developer","company":"TechCorp","period":"2023-Present","description":"Leading development team"}' \
        "Add User Experience"
    
    # Test get connections
    test_api "GET" "http://localhost:8081/api/v1/user/connections" \
        "" \
        "Get User Connections"
    
    # Test send connection request
    test_api "POST" "http://localhost:8081/api/v1/user/connections/send" \
        '{"receiverUsername":"jane.smith"}' \
        "Send Connection Request"
    
    # Test get announcements
    test_api "GET" "http://localhost:8081/api/v1/admin/announcements" \
        "" \
        "Get Announcements"
    
    # Test create announcement
    test_api "POST" "http://localhost:8081/api/v1/admin/announcements" \
        '{"title":"Test Announcement","content":"This is a test announcement","category":"GENERAL"}' \
        "Create Announcement"
    
    # Test get reports
    test_api "GET" "http://localhost:8081/api/v1/admin/reports" \
        "" \
        "Get Reports"
    
    # Test create report
    test_api "POST" "http://localhost:8081/api/v1/user/reports" \
        '{"title":"Test Report","description":"This is a test report","category":"TECHNICAL"}' \
        "Create Report"
    
    # Test feature flags
    test_api "GET" "http://localhost:8081/api/v1/admin/feature-flags" \
        "" \
        "Get Feature Flags"
    
    # Test update feature flag
    test_api "PUT" "http://localhost:8081/api/v1/admin/feature-flags/NEW_UI" \
        '{"enabled":true,"description":"Updated description"}' \
        "Update Feature Flag"
    
    echo
}

# Test Event Service APIs
test_event_service() {
    echo "🎉 Testing Event Service APIs"
    echo "============================"
    
    # Test get all events
    test_api "GET" "http://localhost:8082/api/v1/events" \
        "" \
        "Get All Events"
    
    # Test get event by ID
    test_api "GET" "http://localhost:8082/api/v1/events/1" \
        "" \
        "Get Event by ID"
    
    # Test create event
    test_api "POST" "http://localhost:8082/api/v1/events/create" \
        '{"title":"Test Event","description":"This is a test event","startDate":"2024-12-25T10:00:00","endDate":"2024-12-25T12:00:00","location":"Test Location","eventType":"NETWORKING","privacy":"PUBLIC"}' \
        "Create Event"
    
    # Test update event
    test_api "PUT" "http://localhost:8082/api/v1/events/1" \
        '{"title":"Updated Event Title","description":"Updated description"}' \
        "Update Event"
    
    # Test search events
    test_api "GET" "http://localhost:8082/api/v1/events/search?query=alumni" \
        "" \
        "Search Events"
    
    # Test get event participations
    test_api "GET" "http://localhost:8082/api/v1/event-participations/event/1" \
        "" \
        "Get Event Participations"
    
    # Test RSVP to event
    test_api "POST" "http://localhost:8082/api/v1/event-participations/rsvp" \
        '{"eventId":1,"status":"CONFIRMED","message":"Looking forward to it!"}' \
        "RSVP to Event"
    
    # Test get event invitations
    test_api "GET" "http://localhost:8082/api/v1/event-invitations/user/john.doe" \
        "" \
        "Get User Event Invitations"
    
    # Test send event invitation
    test_api "POST" "http://localhost:8082/api/v1/event-invitations/send" \
        '{"eventId":1,"invitedUsername":"jane.smith","personalMessage":"Hope you can join us!"}' \
        "Send Event Invitation"
    
    # Test get event comments
    test_api "GET" "http://localhost:8082/api/v1/event-comments/event/1" \
        "" \
        "Get Event Comments"
    
    # Test add event comment
    test_api "POST" "http://localhost:8082/api/v1/event-comments" \
        '{"eventId":1,"content":"This is a test comment"}' \
        "Add Event Comment"
    
    # Test get event media
    test_api "GET" "http://localhost:8082/api/v1/event-media/event/1" \
        "" \
        "Get Event Media"
    
    # Test upload event media
    test_api "POST" "http://localhost:8082/api/v1/event-media" \
        '{"eventId":1,"fileName":"test.jpg","fileUrl":"https://example.com/test.jpg","fileType":"image/jpeg","caption":"Test image"}' \
        "Upload Event Media"
    
    # Test get event reminders
    test_api "GET" "http://localhost:8082/api/v1/event-reminders/user/john.doe" \
        "" \
        "Get User Event Reminders"
    
    # Test create event reminder
    test_api "POST" "http://localhost:8082/api/v1/event-reminders" \
        '{"eventId":1,"reminderTime":"2024-12-25T09:00:00","reminderType":"EMAIL","customMessage":"Event starts in 1 hour!"}' \
        "Create Event Reminder"
    
    # Test get recurring events
    test_api "GET" "http://localhost:8082/api/v1/recurring-events" \
        "" \
        "Get Recurring Events"
    
    # Test create recurring event
    test_api "POST" "http://localhost:8082/api/v1/recurring-events" \
        '{"title":"Weekly Meeting","description":"Weekly team meeting","startDate":"2024-12-25T10:00:00","endDate":"2024-12-25T11:00:00","recurrenceType":"WEEKLY","recurrenceInterval":1}' \
        "Create Recurring Event"
    
    # Test get event templates
    test_api "GET" "http://localhost:8082/api/v1/event-templates" \
        "" \
        "Get Event Templates"
    
    # Test create event template
    test_api "POST" "http://localhost:8082/api/v1/event-templates" \
        '{"templateName":"test_template","name":"Test Template","title":"Test Template","description":"Test template description","eventType":"NETWORKING"}' \
        "Create Event Template"
    
    # Test get event analytics
    test_api "GET" "http://localhost:8082/api/v1/event-analytics" \
        "" \
        "Get Event Analytics"
    
    # Test get event analytics by event ID
    test_api "GET" "http://localhost:8082/api/v1/event-analytics/event/1" \
        "" \
        "Get Event Analytics by Event ID"
    
    # Test get calendar integrations
    test_api "GET" "http://localhost:8082/api/v1/calendar-integrations/user/john.doe" \
        "" \
        "Get User Calendar Integrations"
    
    # Test create calendar integration
    test_api "POST" "http://localhost:8082/api/v1/calendar-integrations" \
        '{"calendarType":"GOOGLE","calendarName":"Test Calendar","calendarUrl":"https://calendar.google.com/test"}' \
        "Create Calendar Integration"
    
    echo
}

# Test Advanced Search APIs
test_advanced_search() {
    echo "🔍 Testing Advanced Search APIs"
    echo "==============================="
    
    # Test advanced event search
    test_api "POST" "http://localhost:8082/api/v1/events/advanced-search" \
        '{"eventType":"NETWORKING","startDate":"2024-01-01","endDate":"2024-12-31","location":"Mumbai","privacy":"PUBLIC"}' \
        "Advanced Event Search"
    
    # Test advanced user search
    test_api "POST" "http://localhost:8081/api/v1/user/alumni/advanced-search" \
        '{"profession":"Software Engineer","location":"Mumbai","batch":"2019","interests":["Java","Spring Boot"]}' \
        "Advanced User Search"
    
    echo
}

# Test Dashboard and Analytics APIs
test_dashboard_apis() {
    echo "📊 Testing Dashboard and Analytics APIs"
    echo "======================================="
    
    # Test dashboard stats
    test_api "GET" "http://localhost:8081/api/v1/admin/dashboard/stats" \
        "" \
        "Get Dashboard Stats"
    
    # Test event analytics dashboard
    test_api "GET" "http://localhost:8082/api/v1/event-analytics/dashboard" \
        "" \
        "Get Event Analytics Dashboard"
    
    # Test user analytics
    test_api "GET" "http://localhost:8081/api/v1/admin/analytics/users" \
        "" \
        "Get User Analytics"
    
    # Test event participation analytics
    test_api "GET" "http://localhost:8082/api/v1/event-analytics/participation" \
        "" \
        "Get Event Participation Analytics"
    
    echo
}

# Test Error Handling
test_error_handling() {
    echo "⚠️ Testing Error Handling"
    echo "========================"
    
    # Test invalid user registration
    test_api "POST" "http://localhost:8081/api/v1/user/auth/signup" \
        '{"username":"","password":"","email":"invalid"}' \
        "Invalid User Registration (should fail)"
    
    # Test invalid event creation
    test_api "POST" "http://localhost:8082/api/v1/events/create" \
        '{"title":"","description":"","startDate":"invalid"}' \
        "Invalid Event Creation (should fail)"
    
    # Test non-existent resource
    test_api "GET" "http://localhost:8081/api/v1/user/profile/999999" \
        "" \
        "Non-existent User Profile (should fail)"
    
    # Test non-existent event
    test_api "GET" "http://localhost:8082/api/v1/events/999999" \
        "" \
        "Non-existent Event (should fail)"
    
    echo
}

# Test Service Health and Monitoring
test_health_endpoints() {
    echo "🏥 Testing Health and Monitoring Endpoints"
    echo "========================================="
    
    # Test user service health
    test_api "GET" "http://localhost:8081/actuator/health" \
        "" \
        "User Service Health"
    
    # Test event service health
    test_api "GET" "http://localhost:8082/actuator/health" \
        "" \
        "Event Service Health"
    
    # Test user service info
    test_api "GET" "http://localhost:8081/actuator/info" \
        "" \
        "User Service Info"
    
    # Test event service info
    test_api "GET" "http://localhost:8082/actuator/info" \
        "" \
        "Event Service Info"
    
    # Test user service metrics
    test_api "GET" "http://localhost:8081/actuator/metrics" \
        "" \
        "User Service Metrics"
    
    # Test event service metrics
    test_api "GET" "http://localhost:8082/actuator/metrics" \
        "" \
        "Event Service Metrics"
    
    echo
}

# Main execution
main() {
    echo "🧪 IJAA Comprehensive API Testing"
    echo "================================="
    echo
    
    # Check services
    check_services
    
    # Test all API categories
    test_user_service
    test_event_service
    test_advanced_search
    test_dashboard_apis
    test_error_handling
    test_health_endpoints
    
    echo "🎉 API Testing Complete!"
    echo "======================="
    echo
    print_success "All API tests have been executed successfully!"
    print_status "Check the output above for any failed tests."
    echo
    print_status "Next Steps:"
    print_status "1. Review any failed tests and fix issues"
    print_status "2. Test with authentication tokens for protected endpoints"
    print_status "3. Run performance tests for high-load scenarios"
    print_status "4. Test cross-service communication"
    echo
}

# Run main function
main "$@"
