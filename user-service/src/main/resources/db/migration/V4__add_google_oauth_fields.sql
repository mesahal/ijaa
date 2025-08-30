-- Add Google OAuth fields to users table
-- This migration adds fields to support Google OAuth authentication

-- Add new columns to users table
ALTER TABLE users 
ADD COLUMN email VARCHAR(100) UNIQUE,
ADD COLUMN first_name VARCHAR(100),
ADD COLUMN last_name VARCHAR(100),
ADD COLUMN profile_picture_url VARCHAR(255),
ADD COLUMN google_id VARCHAR(100) UNIQUE,
ADD COLUMN auth_provider VARCHAR(20) DEFAULT 'LOCAL',
ADD COLUMN locale VARCHAR(100),
ADD COLUMN email_verified VARCHAR(10);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_google_id ON users(google_id);
CREATE INDEX idx_users_auth_provider ON users(auth_provider);

-- Insert Google Sign-In feature flag
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('user.google-signin', 'Google Sign-In', 'Enable Google OAuth2 sign-in functionality', true);
