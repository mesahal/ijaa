--
-- Validation Script: Test Migration Logic
-- This script validates the migration without making changes
--

-- ==============================================
-- STEP 1: CHECK CURRENT TABLE STRUCTURES
-- ==============================================

SELECT '=== CURRENT COUNTRIES TABLE STRUCTURE ===' as info;
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name = 'countries' 
ORDER BY ordinal_position;

SELECT '=== CURRENT CITIES TABLE STRUCTURE ===' as info;
SELECT column_name, data_type, is_nullable, column_default
FROM information_schema.columns 
WHERE table_name = 'cities' 
ORDER BY ordinal_position;

-- ==============================================
-- STEP 2: CHECK DATA COUNTS
-- ==============================================

SELECT '=== DATA COUNTS ===' as info;
SELECT 
    (SELECT COUNT(*) FROM countries) as countries_count,
    (SELECT COUNT(*) FROM cities) as cities_count,
    (SELECT COUNT(DISTINCT country_id) FROM cities) as unique_countries_in_cities;

-- ==============================================
-- STEP 3: CHECK FOR ORPHANED CITIES
-- ==============================================

SELECT '=== ORPHANED CITIES CHECK ===' as info;
SELECT COUNT(*) as orphaned_cities_count
FROM cities c 
LEFT JOIN countries co ON c.country_id = co.id 
WHERE co.id IS NULL;

-- Show sample orphaned cities if any
SELECT 'Sample orphaned cities (if any):' as info;
SELECT c.id, c.name, c.country_id
FROM cities c 
LEFT JOIN countries co ON c.country_id = co.id 
WHERE co.id IS NULL
LIMIT 5;

-- ==============================================
-- STEP 4: CHECK COLUMNS TO BE DROPPED
-- ==============================================

SELECT '=== COUNTRIES COLUMNS TO BE DROPPED ===' as info;
SELECT column_name
FROM information_schema.columns 
WHERE table_name = 'countries' 
AND column_name NOT IN ('id', 'name')
ORDER BY column_name;

SELECT '=== CITIES COLUMNS TO BE DROPPED ===' as info;
SELECT column_name
FROM information_schema.columns 
WHERE table_name = 'cities' 
AND column_name NOT IN ('id', 'name', 'country_id')
ORDER BY column_name;

-- ==============================================
-- STEP 5: SAMPLE DATA PREVIEW
-- ==============================================

SELECT '=== SAMPLE COUNTRIES DATA ===' as info;
SELECT id, name FROM countries LIMIT 5;

SELECT '=== SAMPLE CITIES DATA ===' as info;
SELECT c.id, c.name, c.country_id, co.name as country_name
FROM cities c 
JOIN countries co ON c.country_id = co.id 
LIMIT 10;

-- ==============================================
-- STEP 6: FOREIGN KEY CONSTRAINTS
-- ==============================================

SELECT '=== FOREIGN KEY CONSTRAINTS ===' as info;
SELECT 
    tc.constraint_name,
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
    AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
    AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' 
AND (tc.table_name = 'countries' OR tc.table_name = 'cities');

-- ==============================================
-- STEP 7: INDEXES
-- ==============================================

SELECT '=== EXISTING INDEXES ===' as info;
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes 
WHERE tablename IN ('countries', 'cities')
ORDER BY tablename, indexname;

-- ==============================================
-- VALIDATION COMPLETE
-- ==============================================

SELECT '=== VALIDATION COMPLETE ===' as info;
SELECT 'Review the results above before running the migration.' as recommendation;






