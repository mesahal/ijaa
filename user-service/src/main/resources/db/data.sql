-- User Service Initial Data
-- This file inserts essential initial data for the IJAA user service

-- Insert default admin user (password: admin123)
INSERT INTO admins (username, email, password, first_name, last_name, role) 
VALUES ('admin', 'admin@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'System', 'Administrator', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Insert default feature flags
INSERT INTO feature_flags (name, description, is_enabled) VALUES
('USER_REGISTRATION', 'Allow new user registration', true),
('USER_LOGIN', 'Allow user login', true),
('PROFILE_MANAGEMENT', 'Allow users to manage their profiles', true),
('ALUMNI_SEARCH', 'Allow alumni search functionality', true),
('CONNECTION_SYSTEM', 'Allow users to connect with other alumni', true),
('EVENT_CREATION', 'Allow users to create events', true),
('EVENT_PARTICIPATION', 'Allow users to participate in events', true),
('EVENT_COMMENTS', 'Allow users to comment on events', true),
('FILE_UPLOAD', 'Allow file uploads', true),
('ADMIN_DASHBOARD', 'Enable admin dashboard functionality', true)
ON CONFLICT (name) DO NOTHING;

-- Insert sample user for testing (password: user123)
INSERT INTO users (username, email, password, first_name, last_name, graduation_year, department, role) 
VALUES ('testuser', 'test@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Test', 'User', 2020, 'Computer Science', 'USER')
ON CONFLICT (username) DO NOTHING;

-- Insert profile for test user
INSERT INTO profiles (user_id, bio, current_position, company, location) 
SELECT id, 'Software Developer passionate about technology', 'Software Developer', 'Tech Corp', 'Dhaka, Bangladesh'
FROM users WHERE username = 'testuser'
ON CONFLICT DO NOTHING;

-- Insert user settings for test user
INSERT INTO user_settings (user_id, theme_preference, email_notifications, push_notifications) 
SELECT id, 'DEVICE', true, true
FROM users WHERE username = 'testuser'
ON CONFLICT DO NOTHING;

-- Insert sample interests for test user
INSERT INTO interests (user_id, interest_name, category, skill_level) 
SELECT id, 'Java Development', 'Programming', 'Advanced'
FROM users WHERE username = 'testuser'
ON CONFLICT DO NOTHING;

INSERT INTO interests (user_id, interest_name, category, skill_level) 
SELECT id, 'Spring Boot', 'Framework', 'Intermediate'
FROM users WHERE username = 'testuser'
ON CONFLICT DO NOTHING;

-- Insert sample experience for test user
INSERT INTO experiences (user_id, title, company, location, start_date, is_current, description) 
SELECT id, 'Software Developer', 'Tech Corp', 'Dhaka, Bangladesh', '2020-06-01', true, 'Full-stack development using Java and Spring Boot'
FROM users WHERE username = 'testuser'
ON CONFLICT DO NOTHING;
