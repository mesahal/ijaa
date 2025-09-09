--
-- Migration Script: Simplify Countries and Cities Tables
-- This script drops unnecessary columns from countries and cities tables
-- keeping only essential columns: id, name for countries and id, name, country_id for cities
--

-- ==============================================
-- STEP 1: BACKUP EXISTING DATA (Optional)
-- ==============================================
-- Uncomment the following lines if you want to backup existing data
-- CREATE TABLE countries_backup AS SELECT * FROM countries;
-- CREATE TABLE cities_backup AS SELECT * FROM cities;

-- ==============================================
-- STEP 2: SIMPLIFY COUNTRIES TABLE
-- ==============================================

-- Drop foreign key constraints that reference countries table first
ALTER TABLE cities DROP CONSTRAINT IF EXISTS cities_country_id_fkey;

-- Drop unnecessary columns from countries table
ALTER TABLE countries DROP COLUMN IF EXISTS iso3;
ALTER TABLE countries DROP COLUMN IF EXISTS numeric_code;
ALTER TABLE countries DROP COLUMN IF EXISTS iso2;
ALTER TABLE countries DROP COLUMN IF EXISTS phonecode;
ALTER TABLE countries DROP COLUMN IF EXISTS capital;
ALTER TABLE countries DROP COLUMN IF EXISTS currency;
ALTER TABLE countries DROP COLUMN IF EXISTS currency_name;
ALTER TABLE countries DROP COLUMN IF EXISTS currency_symbol;
ALTER TABLE countries DROP COLUMN IF EXISTS tld;
ALTER TABLE countries DROP COLUMN IF EXISTS native;
ALTER TABLE countries DROP COLUMN IF EXISTS region;
ALTER TABLE countries DROP COLUMN IF EXISTS region_id;
ALTER TABLE countries DROP COLUMN IF EXISTS subregion;
ALTER TABLE countries DROP COLUMN IF EXISTS subregion_id;
ALTER TABLE countries DROP COLUMN IF EXISTS nationality;
ALTER TABLE countries DROP COLUMN IF EXISTS timezones;
ALTER TABLE countries DROP COLUMN IF EXISTS translations;
ALTER TABLE countries DROP COLUMN IF EXISTS latitude;
ALTER TABLE countries DROP COLUMN IF EXISTS longitude;
ALTER TABLE countries DROP COLUMN IF EXISTS emoji;
ALTER TABLE countries DROP COLUMN IF EXISTS "emojiU";
ALTER TABLE countries DROP COLUMN IF EXISTS created_at;
ALTER TABLE countries DROP COLUMN IF EXISTS updated_at;
ALTER TABLE countries DROP COLUMN IF EXISTS flag;
ALTER TABLE countries DROP COLUMN IF EXISTS "wikiDataId";

-- ==============================================
-- STEP 3: SIMPLIFY CITIES TABLE
-- ==============================================

-- Drop unnecessary columns from cities table
ALTER TABLE cities DROP COLUMN IF EXISTS state_id;
ALTER TABLE cities DROP COLUMN IF EXISTS state_code;
ALTER TABLE cities DROP COLUMN IF EXISTS country_code;
ALTER TABLE cities DROP COLUMN IF EXISTS latitude;
ALTER TABLE cities DROP COLUMN IF EXISTS longitude;
ALTER TABLE cities DROP COLUMN IF EXISTS timezone;
ALTER TABLE cities DROP COLUMN IF EXISTS created_at;
ALTER TABLE cities DROP COLUMN IF EXISTS updated_at;
ALTER TABLE cities DROP COLUMN IF EXISTS flag;
ALTER TABLE cities DROP COLUMN IF EXISTS "wikiDataId";

-- ==============================================
-- STEP 4: RECREATE FOREIGN KEY CONSTRAINTS
-- ==============================================

-- Recreate foreign key constraint for cities table
ALTER TABLE cities ADD CONSTRAINT cities_country_id_fkey 
    FOREIGN KEY (country_id) REFERENCES countries(id);

-- ==============================================
-- STEP 5: CREATE INDEXES FOR PERFORMANCE
-- ==============================================

-- Create index on cities.country_id for better performance
CREATE INDEX IF NOT EXISTS cities_country_id_idx ON cities(country_id);

-- ==============================================
-- STEP 6: VERIFY TABLE STRUCTURES
-- ==============================================

-- Display final table structures
\d countries;
\d cities;

-- ==============================================
-- STEP 7: VERIFY DATA INTEGRITY
-- ==============================================

-- Check that all cities still have valid country references
SELECT COUNT(*) as total_cities FROM cities;
SELECT COUNT(*) as cities_with_valid_countries 
FROM cities c 
JOIN countries co ON c.country_id = co.id;

-- If the counts don't match, there are orphaned cities
-- You may need to clean up orphaned records or update country_id values

-- ==============================================
-- MIGRATION COMPLETE
-- ==============================================

-- Final verification queries
SELECT 'Countries table simplified successfully' as status;
SELECT 'Cities table simplified successfully' as status;
SELECT 'Foreign key constraints recreated' as status;
SELECT 'Indexes created for performance' as status;




