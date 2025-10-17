-- File Service Database Schema
-- This file creates all necessary tables for the IJAA file service

-- Users table (for file service - stores file paths)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    profile_photo_path VARCHAR(255),
    cover_photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event banners table
CREATE TABLE IF NOT EXISTS event_banners (
    id BIGSERIAL PRIMARY KEY,
    event_id VARCHAR(255) UNIQUE NOT NULL,
    file_name VARCHAR(200) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event post media table
CREATE TABLE IF NOT EXISTS event_post_media (
    id BIGSERIAL PRIMARY KEY,
    post_id VARCHAR(255) NOT NULL,
    file_name VARCHAR(200) NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    media_type VARCHAR(20) NOT NULL, -- IMAGE, VIDEO
    file_order INTEGER DEFAULT 0, -- For ordering multiple files in a post
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_user_id ON users(user_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_event_banners_event_id ON event_banners(event_id);
CREATE INDEX IF NOT EXISTS idx_event_post_media_post_id ON event_post_media(post_id);
CREATE INDEX IF NOT EXISTS idx_event_post_media_type ON event_post_media(media_type);
CREATE INDEX IF NOT EXISTS idx_event_post_media_order ON event_post_media(file_order);
