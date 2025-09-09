--
-- SAFE Migration Script: Simplify Countries and Cities Tables
-- This script safely drops unnecessary columns from countries and cities tables
-- keeping only essential columns: id, name for countries and id, name, country_id for cities
--
-- IMPORTANT: Run this script in a transaction and verify results before committing
--

BEGIN;

-- ==============================================
-- STEP 1: VERIFY CURRENT TABLE STRUCTURES
-- ==============================================

-- Check current countries table structure
SELECT 'Current Countries Table Structure:' as info;
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'countries' 
ORDER BY ordinal_position;

-- Check current cities table structure
SELECT 'Current Cities Table Structure:' as info;
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'cities' 
ORDER BY ordinal_position;

-- ==============================================
-- STEP 2: DATA VALIDATION AND CLEANUP
-- ==============================================

-- Check for orphaned cities (cities with invalid country_id)
SELECT 'Checking for orphaned cities...' as info;
SELECT COUNT(*) as orphaned_cities_count
FROM cities c 
LEFT JOIN countries co ON c.country_id = co.id 
WHERE co.id IS NULL;

-- If there are orphaned cities, you may want to clean them up first
-- Uncomment the following line if you want to delete orphaned cities
-- DELETE FROM cities WHERE country_id NOT IN (SELECT id FROM countries);

-- ==============================================
-- STEP 3: BACKUP CRITICAL DATA (Optional)
-- ==============================================

-- Create backup tables with only the essential data
CREATE TABLE IF NOT EXISTS countries_backup AS 
SELECT id, name FROM countries;

CREATE TABLE IF NOT EXISTS cities_backup AS 
SELECT id, name, country_id FROM cities;

-- ==============================================
-- STEP 4: SIMPLIFY COUNTRIES TABLE
-- ==============================================

-- Drop foreign key constraints that reference countries table first
ALTER TABLE cities DROP CONSTRAINT IF EXISTS cities_country_id_fkey;

-- Drop unnecessary columns from countries table (using IF EXISTS to avoid errors)
DO $$ 
BEGIN
    -- Drop columns that might exist
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'iso3') THEN
        ALTER TABLE countries DROP COLUMN iso3;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'numeric_code') THEN
        ALTER TABLE countries DROP COLUMN numeric_code;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'iso2') THEN
        ALTER TABLE countries DROP COLUMN iso2;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'phonecode') THEN
        ALTER TABLE countries DROP COLUMN phonecode;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'capital') THEN
        ALTER TABLE countries DROP COLUMN capital;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'currency') THEN
        ALTER TABLE countries DROP COLUMN currency;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'currency_name') THEN
        ALTER TABLE countries DROP COLUMN currency_name;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'currency_symbol') THEN
        ALTER TABLE countries DROP COLUMN currency_symbol;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'tld') THEN
        ALTER TABLE countries DROP COLUMN tld;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'native') THEN
        ALTER TABLE countries DROP COLUMN native;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'region') THEN
        ALTER TABLE countries DROP COLUMN region;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'region_id') THEN
        ALTER TABLE countries DROP COLUMN region_id;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'subregion') THEN
        ALTER TABLE countries DROP COLUMN subregion;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'subregion_id') THEN
        ALTER TABLE countries DROP COLUMN subregion_id;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'nationality') THEN
        ALTER TABLE countries DROP COLUMN nationality;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'timezones') THEN
        ALTER TABLE countries DROP COLUMN timezones;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'translations') THEN
        ALTER TABLE countries DROP COLUMN translations;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'latitude') THEN
        ALTER TABLE countries DROP COLUMN latitude;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'longitude') THEN
        ALTER TABLE countries DROP COLUMN longitude;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'emoji') THEN
        ALTER TABLE countries DROP COLUMN emoji;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'emojiU') THEN
        ALTER TABLE countries DROP COLUMN "emojiU";
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'created_at') THEN
        ALTER TABLE countries DROP COLUMN created_at;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'updated_at') THEN
        ALTER TABLE countries DROP COLUMN updated_at;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'flag') THEN
        ALTER TABLE countries DROP COLUMN flag;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'countries' AND column_name = 'wikiDataId') THEN
        ALTER TABLE countries DROP COLUMN "wikiDataId";
    END IF;
END $$;

-- ==============================================
-- STEP 5: SIMPLIFY CITIES TABLE
-- ==============================================

-- Drop unnecessary columns from cities table
DO $$ 
BEGIN
    -- Drop columns that might exist
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'state_id') THEN
        ALTER TABLE cities DROP COLUMN state_id;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'state_code') THEN
        ALTER TABLE cities DROP COLUMN state_code;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'country_code') THEN
        ALTER TABLE cities DROP COLUMN country_code;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'latitude') THEN
        ALTER TABLE cities DROP COLUMN latitude;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'longitude') THEN
        ALTER TABLE cities DROP COLUMN longitude;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'timezone') THEN
        ALTER TABLE cities DROP COLUMN timezone;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'created_at') THEN
        ALTER TABLE cities DROP COLUMN created_at;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'updated_at') THEN
        ALTER TABLE cities DROP COLUMN updated_at;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'flag') THEN
        ALTER TABLE cities DROP COLUMN flag;
    END IF;
    
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'cities' AND column_name = 'wikiDataId') THEN
        ALTER TABLE cities DROP COLUMN "wikiDataId";
    END IF;
END $$;

-- ==============================================
-- STEP 6: RECREATE FOREIGN KEY CONSTRAINTS
-- ==============================================

-- Recreate foreign key constraint for cities table
ALTER TABLE cities ADD CONSTRAINT cities_country_id_fkey 
    FOREIGN KEY (country_id) REFERENCES countries(id);

-- ==============================================
-- STEP 7: CREATE INDEXES FOR PERFORMANCE
-- ==============================================

-- Create index on cities.country_id for better performance
CREATE INDEX IF NOT EXISTS cities_country_id_idx ON cities(country_id);

-- ==============================================
-- STEP 8: VERIFY FINAL TABLE STRUCTURES
-- ==============================================

-- Check final countries table structure
SELECT 'Final Countries Table Structure:' as info;
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'countries' 
ORDER BY ordinal_position;

-- Check final cities table structure
SELECT 'Final Cities Table Structure:' as info;
SELECT column_name, data_type, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'cities' 
ORDER BY ordinal_position;

-- ==============================================
-- STEP 9: VERIFY DATA INTEGRITY
-- ==============================================

-- Verify that all cities still have valid country references
SELECT 'Data Integrity Check:' as info;
SELECT 
    (SELECT COUNT(*) FROM cities) as total_cities,
    (SELECT COUNT(*) FROM cities c JOIN countries co ON c.country_id = co.id) as cities_with_valid_countries,
    (SELECT COUNT(*) FROM countries) as total_countries;

-- ==============================================
-- STEP 10: SAMPLE DATA VERIFICATION
-- ==============================================

-- Show sample data from simplified tables
SELECT 'Sample Countries Data:' as info;
SELECT * FROM countries LIMIT 5;

SELECT 'Sample Cities Data:' as info;
SELECT c.id, c.name, c.country_id, co.name as country_name 
FROM cities c 
JOIN countries co ON c.country_id = co.id 
LIMIT 10;

-- ==============================================
-- MIGRATION COMPLETE
-- ==============================================

SELECT 'Migration completed successfully!' as status;
SELECT 'Countries table now has only: id, name' as countries_result;
SELECT 'Cities table now has only: id, name, country_id' as cities_result;
SELECT 'Foreign key constraints recreated' as constraints_result;
SELECT 'Performance indexes created' as indexes_result;

-- Uncomment the following line to commit the transaction
-- COMMIT;

-- If you want to rollback instead, uncomment the following line
-- ROLLBACK;




