# Database Migration: Simplify Countries and Cities Tables

This migration simplifies the `countries` and `cities` tables by removing unnecessary columns and keeping only the essential ones.

## Overview

### Before Migration
- **Countries table**: Contains 25+ columns including ISO codes, currency info, coordinates, timezones, etc.
- **Cities table**: Contains 12+ columns including state info, coordinates, timezones, etc.

### After Migration
- **Countries table**: Only `id` and `name` columns
- **Cities table**: Only `id`, `name`, and `country_id` columns

## Benefits

1. **Reduced Storage**: Significantly smaller database size
2. **Faster Queries**: Fewer columns mean faster SELECT operations
3. **Easier Maintenance**: Simpler schema is easier to manage
4. **Better Performance**: Smaller indexes and faster joins
5. **Cleaner Code**: Simpler entity classes and DTOs

## Files Included

### Migration Scripts
- `migration-simplify-tables.sql` - Basic migration script
- `migration-simplify-tables-safe.sql` - Safe migration with validation and rollback support
- `execute-migration.sh` - Executable script to run the migration

### Simplified Table Definitions
- `countries-simplified.sql` - Clean countries table definition
- `cities-simplified.sql` - Clean cities table definition

## How to Run the Migration

### Option 1: Using the Executable Script (Recommended)

```bash
cd user-service/src/main/resources/db
./execute-migration.sh
```

The script will:
- Test database connection
- Show current table structures
- Ask for confirmation
- Execute the migration
- Verify results

### Option 2: Manual Execution

```bash
# Connect to your database
psql -h localhost -p 5432 -U postgres -d ijaa_user

# Run the migration script
\i migration-simplify-tables-safe.sql

# Review the results and commit if satisfied
COMMIT;
```

### Option 3: Using Environment Variables

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=ijaa_user
export DB_USER=postgres
export DB_PASSWORD=your_password

./execute-migration.sh
```

## What Gets Removed

### Countries Table - Removed Columns:
- `iso3`, `iso2`, `numeric_code` - ISO codes
- `phonecode` - Country calling codes
- `capital` - Capital city names
- `currency`, `currency_name`, `currency_symbol` - Currency information
- `tld` - Top-level domain codes
- `native` - Native language names
- `region`, `region_id`, `subregion`, `subregion_id` - Geographic regions
- `nationality` - Nationality terms
- `timezones` - Timezone information
- `translations` - Multi-language translations
- `latitude`, `longitude` - Geographic coordinates
- `emoji`, `emojiU` - Flag emojis
- `created_at`, `updated_at` - Timestamps
- `flag` - Status flags
- `wikiDataId` - WikiData identifiers

### Cities Table - Removed Columns:
- `state_id`, `state_code` - State/province references
- `country_code` - Country code (redundant with country_id)
- `latitude`, `longitude` - Geographic coordinates
- `timezone` - Timezone information
- `created_at`, `updated_at` - Timestamps
- `flag` - Status flags
- `wikiDataId` - WikiData identifiers

## What Remains

### Countries Table:
- `id` - Primary key (auto-incrementing)
- `name` - Country name in English

### Cities Table:
- `id` - Primary key (auto-incrementing)
- `name` - City name in English
- `country_id` - Foreign key to countries table

## Safety Features

The safe migration script includes:

1. **Data Validation**: Checks for orphaned cities before migration
2. **Backup Creation**: Creates backup tables with essential data
3. **Transaction Support**: Runs in a transaction for easy rollback
4. **Column Existence Checks**: Only drops columns that exist
5. **Integrity Verification**: Verifies data integrity after migration
6. **Performance Indexes**: Creates indexes for better query performance

## Rollback Instructions

If you need to rollback the migration:

```sql
-- Restore from backup tables
TRUNCATE countries, cities;
INSERT INTO countries SELECT * FROM countries_backup;
INSERT INTO cities SELECT * FROM cities_backup;

-- Or restore from your database backup
```

## Verification Queries

After migration, you can verify the results:

```sql
-- Check table structures
\d countries;
\d cities;

-- Verify data integrity
SELECT COUNT(*) as total_cities FROM cities;
SELECT COUNT(*) as cities_with_valid_countries 
FROM cities c 
JOIN countries co ON c.country_id = co.id;

-- Sample data
SELECT * FROM countries LIMIT 5;
SELECT c.id, c.name, c.country_id, co.name as country_name 
FROM cities c 
JOIN countries co ON c.country_id = co.id 
LIMIT 10;
```

## Java Entity Compatibility

The existing Java entities are already compatible with the simplified schema:

- `Country.java` - Only has `id` and `name` fields
- `City.java` - Only has `id`, `name`, and `countryId` fields

No changes to Java code are required.

## API Compatibility

All existing APIs will continue to work:

- `GET /api/v1/user/location/countries` - Returns countries with id and name
- `GET /api/v1/user/location/countries/{countryId}/cities` - Returns cities with id, name, and countryId

## Performance Impact

Expected performance improvements:

- **Storage**: ~80% reduction in table size
- **Query Speed**: Faster SELECT operations due to fewer columns
- **Index Performance**: Smaller indexes, faster lookups
- **Memory Usage**: Less memory required for table scans

## Troubleshooting

### Common Issues

1. **Foreign Key Constraint Errors**
   - The migration handles this by dropping and recreating constraints
   - If issues persist, check for orphaned cities

2. **Column Not Found Errors**
   - The safe migration script checks for column existence before dropping
   - Use the safe migration script to avoid these errors

3. **Data Loss Concerns**
   - Backup tables are created automatically
   - Review the backup before committing the transaction

### Getting Help

If you encounter issues:

1. Check the migration logs for specific error messages
2. Verify your database connection and permissions
3. Ensure you have sufficient disk space for backups
4. Review the rollback instructions if needed

## Post-Migration Tasks

After successful migration:

1. **Update Documentation**: Update any documentation that references the old schema
2. **Test Applications**: Verify all applications work with the simplified schema
3. **Monitor Performance**: Check query performance improvements
4. **Clean Up**: Remove backup tables after confirming everything works (optional)

## Summary

This migration simplifies the database schema while maintaining all essential functionality. The simplified tables are more efficient, easier to maintain, and fully compatible with the existing Java codebase and APIs.




