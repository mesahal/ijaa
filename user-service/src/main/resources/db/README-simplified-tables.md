# Simplified Countries and Cities Tables

This directory contains simplified versions of the countries and cities tables with only essential columns.

## What Was Removed

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
- `state_id` - State/province references
- `latitude`, `longitude` - Geographic coordinates
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

## Benefits of Simplified Tables

1. **Reduced Storage**: Significantly smaller database size
2. **Faster Queries**: Fewer columns mean faster SELECT operations
3. **Easier Maintenance**: Simpler schema is easier to manage
4. **Better Performance**: Smaller indexes and faster joins
5. **Cleaner Code**: Simpler entity classes and DTOs

## How to Use

### 1. Replace Existing Tables:
```sql
-- Run the simplified table scripts
\i countries-simplified.sql
\i cities-simplified.sql
```

### 2. Basic Queries:
```sql
-- Get all countries
SELECT * FROM countries;

-- Get all cities in a specific country
SELECT c.name as city_name, co.name as country_name
FROM cities c
JOIN countries co ON c.country_id = co.id
WHERE co.name = 'United States';

-- Get cities by country ID
SELECT name FROM cities WHERE country_id = 231; -- US cities
```

### 3. Add More Cities:
```sql
-- Add a new city
INSERT INTO cities (name, country_id) VALUES ('New City', 231);

-- Add multiple cities
INSERT INTO cities (name, country_id) VALUES 
('City 1', 231),
('City 2', 231),
('City 3', 231);
```

## Sample Data Included

### Countries:
- 245 countries with basic information
- Covers all major countries and territories

### Cities:
- 381 major cities worldwide
- Representative cities from each country
- Major metropolitan areas included

## Adding More Data

### To Add More Countries:
```sql
INSERT INTO countries (name) VALUES ('New Country');
```

### To Add More Cities:
```sql
INSERT INTO cities (name, country_id) VALUES ('New City', country_id);
```

## Migration from Full Tables

If you're migrating from the full tables:

1. **Backup existing data**:
```sql
CREATE TABLE countries_backup AS SELECT * FROM countries;
CREATE TABLE cities_backup AS SELECT * FROM cities;
```

2. **Run simplified scripts**:
```sql
\i countries-simplified.sql
\i cities-simplified.sql
```

3. **Verify data**:
```sql
SELECT COUNT(*) FROM countries;  -- Should be 245
SELECT COUNT(*) FROM cities;     -- Should be 381
```

## Performance Considerations

- **Indexes**: The cities table has an index on `country_id` for fast lookups
- **Foreign Keys**: Proper referential integrity maintained
- **Sequences**: Auto-incrementing IDs for easy data insertion

## File Structure

```
db/
├── countries-simplified.sql      # Simplified countries table
├── cities-simplified.sql         # Simplified cities table
├── README-simplified-tables.md   # This file
├── countries.sql                 # Original full countries table (keep as backup)
└── cities.sql                    # Original full cities table (keep as backup)
```

## Notes

- Keep the original `.sql` files as backups
- The simplified tables maintain referential integrity
- All major countries and cities are included
- Easy to extend with additional data as needed
- Suitable for production use with minimal geographic data requirements
