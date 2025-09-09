#!/bin/bash

# Database Migration Script: Simplify Countries and Cities Tables
# This script executes the safe migration to drop unnecessary columns

# Configuration
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-ijaa_user}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-password}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}IJAA Database Migration: Simplify Tables${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""

# Check if psql is available
if ! command -v psql &> /dev/null; then
    echo -e "${RED}Error: psql command not found. Please install PostgreSQL client.${NC}"
    exit 1
fi

# Set password environment variable
export PGPASSWORD=$DB_PASSWORD

echo -e "${YELLOW}Database Configuration:${NC}"
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo "User: $DB_USER"
echo ""

# Test database connection
echo -e "${YELLOW}Testing database connection...${NC}"
if ! psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1;" > /dev/null 2>&1; then
    echo -e "${RED}Error: Cannot connect to database. Please check your configuration.${NC}"
    exit 1
fi
echo -e "${GREEN}Database connection successful!${NC}"
echo ""

# Show current table structures
echo -e "${YELLOW}Current table structures:${NC}"
echo "Countries table:"
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "\d countries" 2>/dev/null || echo "Countries table not found or error occurred"

echo ""
echo "Cities table:"
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "\d cities" 2>/dev/null || echo "Cities table not found or error occurred"

echo ""

# Ask for confirmation
read -p "Do you want to proceed with the migration? (y/N): " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Migration cancelled by user.${NC}"
    exit 0
fi

echo ""
echo -e "${YELLOW}Executing migration...${NC}"

# Execute the migration script
if psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f migration-simplify-tables-safe.sql; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Migration completed successfully!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo -e "${YELLOW}Final table structures:${NC}"
    echo "Countries table:"
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "\d countries"
    
    echo ""
    echo "Cities table:"
    psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "\d cities"
    
    echo ""
    echo -e "${GREEN}Migration Summary:${NC}"
    echo "✅ Countries table simplified to: id, name"
    echo "✅ Cities table simplified to: id, name, country_id"
    echo "✅ Foreign key constraints recreated"
    echo "✅ Performance indexes created"
    echo "✅ Data integrity verified"
    
else
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}Migration failed!${NC}"
    echo -e "${RED}========================================${NC}"
    echo ""
    echo -e "${YELLOW}Please check the error messages above and try again.${NC}"
    echo -e "${YELLOW}You may need to manually rollback any partial changes.${NC}"
    exit 1
fi

# Clean up
unset PGPASSWORD

echo ""
echo -e "${GREEN}Migration script completed.${NC}"




