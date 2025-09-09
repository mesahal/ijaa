-- User Service Initial Data - Updated for Current IJAA System
-- This file inserts comprehensive initial data for the IJAA user service

-- Insert default admin users
INSERT INTO admins (username, email, password, first_name, last_name, role) VALUES
('admin', 'admin@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'System', 'Administrator', 'ADMIN'),
('superadmin', 'superadmin@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Super', 'Admin', 'ADMIN')
ON CONFLICT (username) DO NOTHING;

-- Insert comprehensive feature flags for the current system
INSERT INTO feature_flags (name, description, is_enabled) VALUES
-- Core User Features
('USER_REGISTRATION', 'Allow new user registration', true),
('USER_LOGIN', 'Allow user login', true),
('PROFILE_MANAGEMENT', 'Allow users to manage their profiles', true),
('ALUMNI_SEARCH', 'Allow alumni search functionality', true),
('CONNECTION_SYSTEM', 'Allow users to connect with other alumni', true),

-- Event System Features
('EVENT_CREATION', 'Allow users to create events', true),
('EVENT_PARTICIPATION', 'Allow users to participate in events', true),
('EVENT_COMMENTS', 'Allow users to comment on events', true),
('EVENT_INVITATIONS', 'Allow users to send event invitations', true),
('EVENT_BANNERS', 'Allow event banner uploads', true),
('ADVANCED_EVENT_SEARCH', 'Enable advanced event search functionality', true),

-- File Management Features
('FILE_UPLOAD', 'Allow file uploads', true),
('PROFILE_PHOTOS', 'Allow profile photo uploads', true),
('COVER_PHOTOS', 'Allow cover photo uploads', true),

-- Admin Features
('ADMIN_DASHBOARD', 'Enable admin dashboard functionality', true),
('USER_MANAGEMENT', 'Allow admins to manage users', true),
('FEATURE_FLAG_MANAGEMENT', 'Allow admins to manage feature flags', true),
('ANNOUNCEMENT_SYSTEM', 'Enable system announcements', true),
('REPORTING_SYSTEM', 'Enable user reporting system', true),

-- Advanced Features
('USER_SETTINGS', 'Enable user settings and preferences', true),
('THEME_CUSTOMIZATION', 'Allow users to customize themes', true),
('NOTIFICATION_SYSTEM', 'Enable notification system', true)
ON CONFLICT (name) DO NOTHING;

-- Insert sample users with realistic data
INSERT INTO users (username, email, password, first_name, last_name, phone, date_of_birth, graduation_year, department, is_active, role) VALUES
-- Test user (password: user123)
('testuser', 'test@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Test', 'User', '+8801712345678', '1998-05-15', 2020, 'Computer Science', true, 'USER'),

-- Sample alumni users
('ahmed.khan', 'ahmed.khan@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Ahmed', 'Khan', '+8801812345678', '1995-03-20', 2018, 'Computer Science', true, 'USER'),
('fatima.rahman', 'fatima.rahman@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Fatima', 'Rahman', '+8801912345678', '1996-07-12', 2019, 'Electrical Engineering', true, 'USER'),
('omar.hassan', 'omar.hassan@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Omar', 'Hassan', '+8801612345678', '1997-11-08', 2020, 'Mechanical Engineering', true, 'USER'),
('sara.ahmed', 'sara.ahmed@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Sara', 'Ahmed', '+8801512345678', '1996-09-25', 2019, 'Computer Science', true, 'USER'),
('ali.mahmud', 'ali.mahmud@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Ali', 'Mahmud', '+8801412345678', '1995-12-03', 2018, 'Civil Engineering', true, 'USER'),
('nadia.islam', 'nadia.islam@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Nadia', 'Islam', '+8801312345678', '1997-04-18', 2020, 'Computer Science', true, 'USER'),
('karim.ali', 'karim.ali@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Karim', 'Ali', '+8801212345678', '1996-08-30', 2019, 'Electrical Engineering', true, 'USER'),
('leila.hossain', 'leila.hossain@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Leila', 'Hossain', '+8801112345678', '1995-06-14', 2018, 'Mechanical Engineering', true, 'USER')
ON CONFLICT (username) DO NOTHING;

-- Insert comprehensive profiles for all users
INSERT INTO profiles (user_id, bio, current_position, company, location, website, linkedin_url, github_url) VALUES
-- Test user profile
((SELECT id FROM users WHERE username = 'testuser'), 
 'Software Developer passionate about technology and innovation', 
 'Software Developer', 'Tech Solutions Ltd', 'Dhaka, Bangladesh', 
 'https://testuser.dev', 'https://linkedin.com/in/testuser', 'https://github.com/testuser'),

-- Sample alumni profiles
((SELECT id FROM users WHERE username = 'ahmed.khan'), 
 'Senior Software Engineer with 5+ years of experience in full-stack development', 
 'Senior Software Engineer', 'Digital Innovations Corp', 'Dhaka, Bangladesh', 
 'https://ahmedkhan.dev', 'https://linkedin.com/in/ahmedkhan', 'https://github.com/ahmedkhan'),

((SELECT id FROM users WHERE username = 'fatima.rahman'), 
 'Electrical Engineer specializing in power systems and renewable energy', 
 'Power Systems Engineer', 'Energy Solutions Ltd', 'Chittagong, Bangladesh', 
 'https://fatimarahman.dev', 'https://linkedin.com/in/fatimarahman', 'https://github.com/fatimarahman'),

((SELECT id FROM users WHERE username = 'omar.hassan'), 
 'Mechanical Engineer with expertise in automotive design and manufacturing', 
 'Mechanical Design Engineer', 'AutoTech Industries', 'Sylhet, Bangladesh', 
 'https://omarhassan.dev', 'https://linkedin.com/in/omarhassan', 'https://github.com/omarhassan'),

((SELECT id FROM users WHERE username = 'sara.ahmed'), 
 'Full-stack developer passionate about creating user-friendly applications', 
 'Full-Stack Developer', 'WebTech Solutions', 'Dhaka, Bangladesh', 
 'https://saraahmed.dev', 'https://linkedin.com/in/saraahmed', 'https://github.com/saraahmed'),

((SELECT id FROM users WHERE username = 'ali.mahmud'), 
 'Civil Engineer with experience in infrastructure and construction projects', 
 'Project Engineer', 'Construction Plus Ltd', 'Rajshahi, Bangladesh', 
 'https://alimahmud.dev', 'https://linkedin.com/in/alimahmud', 'https://github.com/alimahmud'),

((SELECT id FROM users WHERE username = 'nadia.islam'), 
 'Software Engineer specializing in machine learning and AI applications', 
 'ML Engineer', 'AI Innovations Lab', 'Dhaka, Bangladesh', 
 'https://nadiaislam.dev', 'https://linkedin.com/in/nadiaislam', 'https://github.com/nadiaislam'),

((SELECT id FROM users WHERE username = 'karim.ali'), 
 'Electrical Engineer with focus on smart grid and IoT solutions', 
 'IoT Solutions Engineer', 'Smart Grid Technologies', 'Chittagong, Bangladesh', 
 'https://karimali.dev', 'https://linkedin.com/in/karimali', 'https://github.com/karimali'),

((SELECT id FROM users WHERE username = 'leila.hossain'), 
 'Mechanical Engineer specializing in robotics and automation', 
 'Robotics Engineer', 'Automation Solutions Inc', 'Sylhet, Bangladesh', 
 'https://leilahossain.dev', 'https://linkedin.com/in/leilahossain', 'https://github.com/leilahossain')
ON CONFLICT DO NOTHING;

-- Insert user settings for all users
INSERT INTO user_settings (user_id, theme_preference, email_notifications, push_notifications) VALUES
((SELECT id FROM users WHERE username = 'testuser'), 'DEVICE', true, true),
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'DARK', true, true),
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'LIGHT', true, false),
((SELECT id FROM users WHERE username = 'omar.hassan'), 'DEVICE', true, true),
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'DARK', false, true),
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'LIGHT', true, true),
((SELECT id FROM users WHERE username = 'nadia.islam'), 'DEVICE', true, true),
((SELECT id FROM users WHERE username = 'karim.ali'), 'DARK', true, false),
((SELECT id FROM users WHERE username = 'leila.hossain'), 'LIGHT', true, true)
ON CONFLICT DO NOTHING;

-- Insert comprehensive interests for users
INSERT INTO interests (user_id, interest_name, category, skill_level) VALUES
-- Test user interests
((SELECT id FROM users WHERE username = 'testuser'), 'Java Development', 'Programming', 'Advanced'),
((SELECT id FROM users WHERE username = 'testuser'), 'Spring Boot', 'Framework', 'Intermediate'),
((SELECT id FROM users WHERE username = 'testuser'), 'Database Design', 'Database', 'Intermediate'),

-- Ahmed Khan interests
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'Full-Stack Development', 'Programming', 'Advanced'),
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'React.js', 'Frontend', 'Advanced'),
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'Node.js', 'Backend', 'Advanced'),
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'Microservices', 'Architecture', 'Intermediate'),

-- Fatima Rahman interests
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'Power Systems', 'Electrical', 'Advanced'),
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'Renewable Energy', 'Energy', 'Advanced'),
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'Smart Grid', 'Technology', 'Intermediate'),

-- Omar Hassan interests
((SELECT id FROM users WHERE username = 'omar.hassan'), 'Automotive Design', 'Mechanical', 'Advanced'),
((SELECT id FROM users WHERE username = 'omar.hassan'), 'CAD/CAM', 'Software', 'Advanced'),
((SELECT id FROM users WHERE username = 'omar.hassan'), 'Manufacturing', 'Process', 'Intermediate'),

-- Sara Ahmed interests
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'Web Development', 'Programming', 'Advanced'),
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'UI/UX Design', 'Design', 'Intermediate'),
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'Agile Development', 'Methodology', 'Intermediate'),

-- Ali Mahmud interests
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'Structural Analysis', 'Civil', 'Advanced'),
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'Project Management', 'Management', 'Intermediate'),
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'AutoCAD', 'Software', 'Advanced'),

-- Nadia Islam interests
((SELECT id FROM users WHERE username = 'nadia.islam'), 'Machine Learning', 'AI', 'Advanced'),
((SELECT id FROM users WHERE username = 'nadia.islam'), 'Python', 'Programming', 'Advanced'),
((SELECT id FROM users WHERE username = 'nadia.islam'), 'Data Science', 'Analytics', 'Intermediate'),

-- Karim Ali interests
((SELECT id FROM users WHERE username = 'karim.ali'), 'IoT Development', 'Technology', 'Advanced'),
((SELECT id FROM users WHERE username = 'karim.ali'), 'Smart Grid', 'Electrical', 'Advanced'),
((SELECT id FROM users WHERE username = 'karim.ali'), 'Embedded Systems', 'Hardware', 'Intermediate'),

-- Leila Hossain interests
((SELECT id FROM users WHERE username = 'leila.hossain'), 'Robotics', 'Mechanical', 'Advanced'),
((SELECT id FROM users WHERE username = 'leila.hossain'), 'Automation', 'Technology', 'Advanced'),
((SELECT id FROM users WHERE username = 'leila.hossain'), 'PLC Programming', 'Programming', 'Intermediate')
ON CONFLICT DO NOTHING;

-- Insert comprehensive work experiences
INSERT INTO experiences (user_id, title, company, location, start_date, end_date, is_current, description) VALUES
-- Test user experience
((SELECT id FROM users WHERE username = 'testuser'), 'Software Developer', 'Tech Solutions Ltd', 'Dhaka, Bangladesh', '2020-06-01', NULL, true, 'Full-stack development using Java, Spring Boot, and React.js'),

-- Ahmed Khan experiences
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'Senior Software Engineer', 'Digital Innovations Corp', 'Dhaka, Bangladesh', '2021-03-01', NULL, true, 'Leading development team, architecting microservices solutions'),
((SELECT id FROM users WHERE username = 'ahmed.khan'), 'Software Developer', 'TechStart Inc', 'Dhaka, Bangladesh', '2018-07-01', '2021-02-28', false, 'Full-stack development with React and Node.js'),

-- Fatima Rahman experiences
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'Power Systems Engineer', 'Energy Solutions Ltd', 'Chittagong, Bangladesh', '2019-01-01', NULL, true, 'Designing and implementing power distribution systems'),
((SELECT id FROM users WHERE username = 'fatima.rahman'), 'Junior Engineer', 'Power Grid Corp', 'Dhaka, Bangladesh', '2018-08-01', '2018-12-31', false, 'Assisting in power system analysis and maintenance'),

-- Omar Hassan experiences
((SELECT id FROM users WHERE username = 'omar.hassan'), 'Mechanical Design Engineer', 'AutoTech Industries', 'Sylhet, Bangladesh', '2020-01-01', NULL, true, 'Designing automotive components and systems'),
((SELECT id FROM users WHERE username = 'omar.hassan'), 'Design Intern', 'Mech Solutions', 'Dhaka, Bangladesh', '2019-06-01', '2019-12-31', false, 'Assisting in mechanical design and CAD modeling'),

-- Sara Ahmed experiences
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'Full-Stack Developer', 'WebTech Solutions', 'Dhaka, Bangladesh', '2019-09-01', NULL, true, 'Developing web applications using modern technologies'),
((SELECT id FROM users WHERE username = 'sara.ahmed'), 'Frontend Developer', 'WebStart Ltd', 'Dhaka, Bangladesh', '2019-03-01', '2019-08-31', false, 'Creating responsive user interfaces with React'),

-- Ali Mahmud experiences
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'Project Engineer', 'Construction Plus Ltd', 'Rajshahi, Bangladesh', '2018-11-01', NULL, true, 'Managing construction projects and site supervision'),
((SELECT id FROM users WHERE username = 'ali.mahmud'), 'Site Engineer', 'BuildRight Corp', 'Dhaka, Bangladesh', '2018-06-01', '2018-10-31', false, 'Site supervision and quality control'),

-- Nadia Islam experiences
((SELECT id FROM users WHERE username = 'nadia.islam'), 'ML Engineer', 'AI Innovations Lab', 'Dhaka, Bangladesh', '2020-03-01', NULL, true, 'Developing machine learning models and AI solutions'),
((SELECT id FROM users WHERE username = 'nadia.islam'), 'Data Analyst', 'DataTech Inc', 'Dhaka, Bangladesh', '2020-01-01', '2020-02-28', false, 'Analyzing data and creating reports'),

-- Karim Ali experiences
((SELECT id FROM users WHERE username = 'karim.ali'), 'IoT Solutions Engineer', 'Smart Grid Technologies', 'Chittagong, Bangladesh', '2019-08-01', NULL, true, 'Developing IoT solutions for smart grid applications'),
((SELECT id FROM users WHERE username = 'karim.ali'), 'Electrical Engineer', 'Power Solutions Ltd', 'Dhaka, Bangladesh', '2019-03-01', '2019-07-31', false, 'Electrical system design and maintenance'),

-- Leila Hossain experiences
((SELECT id FROM users WHERE username = 'leila.hossain'), 'Robotics Engineer', 'Automation Solutions Inc', 'Sylhet, Bangladesh', '2018-12-01', NULL, true, 'Designing and programming industrial robots'),
((SELECT id FROM users WHERE username = 'leila.hossain'), 'Automation Engineer', 'RoboTech Ltd', 'Dhaka, Bangladesh', '2018-07-01', '2018-11-30', false, 'Implementing automation solutions')
ON CONFLICT DO NOTHING;

-- Insert sample connections between users
INSERT INTO connections (requester_id, recipient_id, status) VALUES
-- Test user connections
((SELECT id FROM users WHERE username = 'testuser'), (SELECT id FROM users WHERE username = 'ahmed.khan'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'testuser'), (SELECT id FROM users WHERE username = 'sara.ahmed'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'testuser'), (SELECT id FROM users WHERE username = 'nadia.islam'), 'PENDING'),

-- Other user connections
((SELECT id FROM users WHERE username = 'ahmed.khan'), (SELECT id FROM users WHERE username = 'fatima.rahman'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'ahmed.khan'), (SELECT id FROM users WHERE username = 'omar.hassan'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'fatima.rahman'), (SELECT id FROM users WHERE username = 'karim.ali'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'sara.ahmed'), (SELECT id FROM users WHERE username = 'leila.hossain'), 'PENDING'),
((SELECT id FROM users WHERE username = 'nadia.islam'), (SELECT id FROM users WHERE username = 'karim.ali'), 'ACCEPTED'),
((SELECT id FROM users WHERE username = 'omar.hassan'), (SELECT id FROM users WHERE username = 'ali.mahmud'), 'ACCEPTED')
ON CONFLICT DO NOTHING;

-- Insert sample announcements
INSERT INTO announcements (title, content, admin_id, is_active) VALUES
('Welcome to IJAA Alumni Network!', 'Welcome to the IJAA Alumni Network! Connect with fellow alumni, share experiences, and grow your professional network.', (SELECT id FROM admins WHERE username = 'admin'), true),
('New Feature: Advanced Event Search', 'We have launched advanced event search functionality. Find events based on location, date, type, and more!', (SELECT id FROM admins WHERE username = 'admin'), true),
('System Maintenance Notice', 'Scheduled maintenance will occur on Sunday, 2:00 AM - 4:00 AM UTC. Services may be temporarily unavailable.', (SELECT id FROM admins WHERE username = 'admin'), true),
('Alumni Meetup 2024', 'Join us for the annual alumni meetup on December 15th, 2024. Network with fellow alumni and industry professionals.', (SELECT id FROM admins WHERE username = 'admin'), true)
ON CONFLICT DO NOTHING;

-- Insert sample reports
INSERT INTO reports (reporter_id, reported_user_id, reason, status) VALUES
((SELECT id FROM users WHERE username = 'testuser'), (SELECT id FROM users WHERE username = 'ahmed.khan'), 'Inappropriate content in profile', 'PENDING'),
((SELECT id FROM users WHERE username = 'fatima.rahman'), (SELECT id FROM users WHERE username = 'omar.hassan'), 'Spam messages', 'RESOLVED')
ON CONFLICT DO NOTHING;
