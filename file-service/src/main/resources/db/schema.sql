-- File Service Database Schema
-- This file creates all necessary tables for the IJAA file service

-- File metadata table
CREATE TABLE IF NOT EXISTS file_metadata (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(100),
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_category VARCHAR(50) NOT NULL, -- 'PROFILE_PHOTO', 'COVER_PHOTO', 'EVENT_BANNER'
    is_active BOOLEAN DEFAULT true
);

-- Event banners table
CREATE TABLE IF NOT EXISTS event_banners (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(100),
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_file_metadata_user_id ON file_metadata(user_id);
CREATE INDEX IF NOT EXISTS idx_file_metadata_file_category ON file_metadata(file_category);
CREATE INDEX IF NOT EXISTS idx_event_banners_event_id ON event_banners(event_id);
CREATE INDEX IF NOT EXISTS idx_file_metadata_is_active ON file_metadata(is_active);
CREATE INDEX IF NOT EXISTS idx_event_banners_is_active ON event_banners(is_active);
