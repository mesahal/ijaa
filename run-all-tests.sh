#!/bin/bash

# 🧪 IJAA Comprehensive Test Suite Execution Script
# This script runs all tests across the IJAA microservices and generates coverage reports

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Function to check if Maven is available
check_maven() {
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        print_status "Please install Maven and try again"
        exit 1
    fi
    print_success "Maven found: $(mvn --version | head -n 1)"
}

# Function to check if Java is available
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        print_status "Please install Java 17+ and try again"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 17+ is required, found Java $JAVA_VERSION"
        exit 1
    fi
    print_success "Java found: $(java -version 2>&1 | head -n 1)"
}

# Function to run tests for a service
run_service_tests() {
    local service_name=$1
    local service_dir=$2
    
    print_status "Running tests for $service_name..."
    
    if [ ! -d "$service_dir" ]; then
        print_error "Service directory $service_dir not found"
        return 1
    fi
    
    cd "$service_dir"
    
    # Clean and compile
    print_status "Cleaning and compiling $service_name..."
    mvn clean compile -q
    
    # Run tests
    print_status "Running tests for $service_name..."
    if mvn test -q; then
        print_success "All tests passed for $service_name"
    else
        print_error "Some tests failed for $service_name"
        return 1
    fi
    
    # Generate coverage report
    print_status "Generating coverage report for $service_name..."
    mvn jacoco:report -q
    
    if [ -f "target/site/jacoco/index.html" ]; then
        print_success "Coverage report generated for $service_name"
        print_status "Coverage report location: $service_dir/target/site/jacoco/index.html"
    else
        print_warning "Coverage report not generated for $service_name"
    fi
    
    cd ..
}

# Function to run specific test categories
run_test_category() {
    local service_name=$1
    local service_dir=$2
    local test_category=$3
    
    print_status "Running $test_category tests for $service_name..."
    
    cd "$service_dir"
    
    case $test_category in
        "unit")
            mvn test -Dtest="*ServiceTest" -q
            ;;
        "integration")
            mvn test -Dtest="*IntegrationTest" -q
            ;;
        "repository")
            mvn test -Dtest="*RepositoryTest" -q
            ;;
        "all")
            mvn test -q
            ;;
        *)
            print_error "Unknown test category: $test_category"
            return 1
            ;;
    esac
    
    if [ $? -eq 0 ]; then
        print_success "$test_category tests passed for $service_name"
    else
        print_error "$test_category tests failed for $service_name"
        return 1
    fi
    
    cd ..
}

# Function to generate combined coverage report
generate_combined_report() {
    print_status "Generating combined coverage report..."
    
    # Create reports directory
    mkdir -p reports
    
    # Copy coverage reports
    if [ -d "user-service/target/site/jacoco" ]; then
        cp -r user-service/target/site/jacoco reports/user-service-coverage
        print_success "User service coverage report copied"
    fi
    
    if [ -d "event-service/target/site/jacoco" ]; then
        cp -r event-service/target/site/jacoco reports/event-service-coverage
        print_success "Event service coverage report copied"
    fi
    
    # Generate summary report
    cat > reports/test-summary.md << EOF
# 🧪 IJAA Test Suite Execution Summary

## Test Execution Date
$(date)

## Services Tested
- User Service: $(if [ -d "user-service/target/site/jacoco" ]; then echo "✅ PASSED"; else echo "❌ FAILED"; fi)
- Event Service: $(if [ -d "event-service/target/site/jacoco" ]; then echo "✅ PASSED"; else echo "❌ FAILED"; fi)

## Coverage Reports
- User Service: reports/user-service-coverage/index.html
- Event Service: reports/event-service-coverage/index.html

## Test Categories
- Unit Tests: Service layer tests with mocked dependencies
- Integration Tests: Controller layer tests with MockMvc
- Repository Tests: Database layer tests with H2
- End-to-End Tests: Complete workflow tests

## Next Steps
1. Review coverage reports for areas needing improvement
2. Fix any failing tests
3. Add additional test cases for uncovered scenarios
4. Integrate with CI/CD pipeline

EOF
    
    print_success "Combined test summary generated: reports/test-summary.md"
}

# Function to show help
show_help() {
    echo "🧪 IJAA Comprehensive Test Suite"
    echo ""
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -h, --help              Show this help message"
    echo "  -s, --service SERVICE   Run tests for specific service (user|event|all)"
    echo "  -c, --category CATEGORY Run specific test category (unit|integration|repository|all)"
    echo "  -r, --report            Generate coverage reports"
    echo "  -v, --verbose           Verbose output"
    echo ""
    echo "Examples:"
    echo "  $0                      # Run all tests for all services"
    echo "  $0 -s user              # Run all tests for user service only"
    echo "  $0 -s event -c unit     # Run unit tests for event service only"
    echo "  $0 -c integration       # Run integration tests for all services"
    echo ""
}

# Main execution
main() {
    print_status "🧪 Starting IJAA Comprehensive Test Suite Execution"
    print_status "=================================================="
    
    # Parse command line arguments
    SERVICE="all"
    CATEGORY="all"
    GENERATE_REPORT=false
    VERBOSE=false
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                show_help
                exit 0
                ;;
            -s|--service)
                SERVICE="$2"
                shift 2
                ;;
            -c|--category)
                CATEGORY="$2"
                shift 2
                ;;
            -r|--report)
                GENERATE_REPORT=true
                shift
                ;;
            -v|--verbose)
                VERBOSE=true
                shift
                ;;
            *)
                print_error "Unknown option: $1"
                show_help
                exit 1
                ;;
        esac
    done
    
    # Validate options
    if [[ "$SERVICE" != "all" && "$SERVICE" != "user" && "$SERVICE" != "event" ]]; then
        print_error "Invalid service: $SERVICE. Use 'user', 'event', or 'all'"
        exit 1
    fi
    
    if [[ "$CATEGORY" != "all" && "$CATEGORY" != "unit" && "$CATEGORY" != "integration" && "$CATEGORY" != "repository" ]]; then
        print_error "Invalid category: $CATEGORY. Use 'unit', 'integration', 'repository', or 'all'"
        exit 1
    fi
    
    # Check prerequisites
    print_status "Checking prerequisites..."
    check_java
    check_maven
    
    # Set Maven options
    if [ "$VERBOSE" = true ]; then
        export MAVEN_OPTS="-X"
    else
        export MAVEN_OPTS="-q"
    fi
    
    # Run tests based on options
    if [ "$SERVICE" = "all" ]; then
        if [ "$CATEGORY" = "all" ]; then
            # Run all tests for all services
            run_service_tests "User Service" "user-service"
            run_service_tests "Event Service" "event-service"
        else
            # Run specific category for all services
            run_test_category "User Service" "user-service" "$CATEGORY"
            run_test_category "Event Service" "event-service" "$CATEGORY"
        fi
    else
        if [ "$CATEGORY" = "all" ]; then
            # Run all tests for specific service
            if [ "$SERVICE" = "user" ]; then
                run_service_tests "User Service" "user-service"
            else
                run_service_tests "Event Service" "event-service"
            fi
        else
            # Run specific category for specific service
            if [ "$SERVICE" = "user" ]; then
                run_test_category "User Service" "user-service" "$CATEGORY"
            else
                run_test_category "Event Service" "event-service" "$CATEGORY"
            fi
        fi
    fi
    
    # Generate combined report if requested
    if [ "$GENERATE_REPORT" = true ]; then
        generate_combined_report
    fi
    
    print_status "=================================================="
    print_success "🎉 Test suite execution completed successfully!"
    
    if [ "$GENERATE_REPORT" = true ]; then
        print_status "📊 Coverage reports available in:"
        print_status "   - User Service: user-service/target/site/jacoco/index.html"
        print_status "   - Event Service: event-service/target/site/jacoco/index.html"
        print_status "   - Combined Summary: reports/test-summary.md"
    fi
}

# Run main function with all arguments
main "$@"
