#!/bin/bash

# 🗄️ IJAA Database Setup Script
# This script automates the creation and setup of separated databases for microservices

set -e  # Exit on any error

echo "🚀 Starting IJAA Database Setup..."

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

# Check if PostgreSQL is running
check_postgres() {
    print_status "Checking PostgreSQL connection..."
    if ! pg_isready -h localhost -U root; then
        print_error "PostgreSQL is not running or not accessible"
        print_status "Please start PostgreSQL and ensure the 'root' user exists"
        exit 1
    fi
    print_success "PostgreSQL is running"
}

# Create databases
create_databases() {
    print_status "Creating databases..."
    
    # Create ijaa_users database
    if psql -h localhost -U root -d postgres -c "SELECT 1 FROM pg_database WHERE datname='ijaa_users'" | grep -q 1; then
        print_warning "Database 'ijaa_users' already exists"
    else
        psql -h localhost -U root -d postgres -c "CREATE DATABASE ijaa_users;"
        print_success "Created database 'ijaa_users'"
    fi
    
    # Create ijaa_events database
    if psql -h localhost -U root -d postgres -c "SELECT 1 FROM pg_database WHERE datname='ijaa_events'" | grep -q 1; then
        print_warning "Database 'ijaa_events' already exists"
    else
        psql -h localhost -U root -d postgres -c "CREATE DATABASE ijaa_events;"
        print_success "Created database 'ijaa_events'"
    fi
}

# Grant permissions
grant_permissions() {
    print_status "Granting database permissions..."
    
    psql -h localhost -U root -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE ijaa_users TO root;"
    psql -h localhost -U root -d postgres -c "GRANT ALL PRIVILEGES ON DATABASE ijaa_events TO root;"
    
    print_success "Database permissions granted"
}

# Run database setup script
run_setup_script() {
    print_status "Running database setup script..."
    
    if [ -f "database-setup.sql" ]; then
        psql -h localhost -U root -d postgres -f database-setup.sql
        print_success "Database setup script completed"
    else
        print_error "database-setup.sql not found"
        print_status "Please ensure the database setup script exists in the current directory"
        exit 1
    fi
}

# Verify setup
verify_setup() {
    print_status "Verifying database setup..."
    
    # Check User Service Database
    print_status "Checking User Service Database (ijaa_users)..."
    USER_TABLES=$(psql -h localhost -U root -d ijaa_users -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" | xargs)
    print_success "User Service Database has $USER_TABLES tables"
    
    # Check Event Service Database
    print_status "Checking Event Service Database (ijaa_events)..."
    EVENT_TABLES=$(psql -h localhost -U root -d ijaa_events -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" | xargs)
    print_success "Event Service Database has $EVENT_TABLES tables"
    
    # List tables in each database
    print_status "User Service Database Tables:"
    psql -h localhost -U root -d ijaa_users -c "\dt" | grep -v "List of relations" | grep -v "Schema" | grep -v "Name" | grep -v "Type" | grep -v "Owner" | grep -v "----" | grep -v "^$"
    
    print_status "Event Service Database Tables:"
    psql -h localhost -U root -d ijaa_events -c "\dt" | grep -v "List of relations" | grep -v "Schema" | grep -v "Name" | grep -v "Type" | grep -v "Owner" | grep -v "----" | grep -v "^$"
}

# Check if migration is needed
check_migration() {
    print_status "Checking if data migration is needed..."
    
    # Check if old database exists
    if psql -h localhost -U root -d postgres -c "SELECT 1 FROM pg_database WHERE datname='ijaa'" | grep -q 1; then
        print_warning "Old database 'ijaa' found"
        read -p "Do you want to migrate data from the old database? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            migrate_data
        else
            print_status "Skipping data migration"
        fi
    else
        print_success "No old database found, migration not needed"
    fi
}

# Migrate data
migrate_data() {
    print_status "Starting data migration..."
    
    # Create backup
    BACKUP_FILE="ijaa_backup_$(date +%Y%m%d_%H%M%S).sql"
    print_status "Creating backup: $BACKUP_FILE"
    pg_dump -h localhost -U root -d ijaa > "$BACKUP_FILE"
    print_success "Backup created: $BACKUP_FILE"
    
    # Run migration script
    if [ -f "database-migration.sql" ]; then
        print_status "Running migration script..."
        psql -h localhost -U root -d postgres -f database-migration.sql
        print_success "Data migration completed"
    else
        print_error "database-migration.sql not found"
        print_status "Please ensure the migration script exists in the current directory"
        exit 1
    fi
}

# Main execution
main() {
    echo "=========================================="
    echo "🗄️  IJAA Database Setup Script"
    echo "=========================================="
    echo
    
    # Check PostgreSQL
    check_postgres
    
    # Create databases
    create_databases
    
    # Grant permissions
    grant_permissions
    
    # Run setup script
    run_setup_script
    
    # Check for migration
    check_migration
    
    # Verify setup
    verify_setup
    
    echo
    echo "=========================================="
    print_success "Database setup completed successfully!"
    echo "=========================================="
    echo
    echo "📊 Database Summary:"
    echo "   • User Service Database: ijaa_users"
    echo "   • Event Service Database: ijaa_events"
    echo
    echo "🚀 Next Steps:"
    echo "   1. Start your microservices"
    echo "   2. Test the API endpoints"
    echo "   3. Verify data integrity"
    echo
    echo "📚 For more information, see: DATABASE_SEPARATION_GUIDE.md"
    echo
}

# Run main function
main "$@"
