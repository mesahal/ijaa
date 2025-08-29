-- Clean up dummy feature flags and replace with real implemented features
-- This migration removes the dummy data and inserts only the actually implemented features
-- This runs in the ijaa_users database (User Service)

-- First, delete all existing feature flags (including dummy ones)
DELETE FROM feature_flags;

-- Reset auto-increment counter
ALTER TABLE feature_flags AUTO_INCREMENT = 1;

-- Insert authentication & user management feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('user.registration', 'User Registration', 'Enable user registration functionality', true),
('user.login', 'User Login', 'Enable user login functionality', true),
('user.password-change', 'User Password Change', 'Enable user password change functionality', true),
('user.profile', 'User Profile Features', 'Enable user profile management features', true),
('user.experiences', 'User Experiences', 'Enable user experience management features', true),
('user.interests', 'User Interests', 'Enable user interest management features', true);

-- Insert admin feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('admin.features', 'Admin Features', 'Enable admin functionality', true),
('admin.user-management', 'Admin User Management', 'Enable admin user management features', true),
('admin.announcements', 'Admin Announcements', 'Enable admin announcement management', true),
('admin.reports', 'Admin Reports', 'Enable admin report management', true),
('admin.auth', 'Admin Authentication', 'Enable admin authentication features', true);

-- Insert search feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('alumni.search', 'Alumni Search', 'Enable alumni search functionality', true);

-- Insert file management feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('file-upload.profile-photo', 'Profile Photo Upload', 'Enable profile photo upload', true),
('file-upload.cover-photo', 'Cover Photo Upload', 'Enable cover photo upload', true),
('file-download', 'File Download', 'Enable file download functionality', true),
('file-delete', 'File Delete', 'Enable file deletion functionality', true);

-- Insert event feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('events', 'Event Management', 'Enable event management functionality', true),
('events.creation', 'Event Creation', 'Enable event creation functionality', true),
('events.update', 'Event Update', 'Enable event update functionality', true),
('events.delete', 'Event Delete', 'Enable event deletion functionality', true),
('events.participation', 'Event Participation', 'Enable event participation and RSVP functionality', true),
('events.invitations', 'Event Invitations', 'Enable event invitation functionality', true),
('events.comments', 'Event Comments', 'Enable event commenting system', true),
('events.media', 'Event Media', 'Enable media attachments for events', true),
('events.templates', 'Event Templates', 'Enable reusable event templates', true),
('events.recurring', 'Recurring Events', 'Enable recurring event patterns', true),
('events.analytics', 'Event Analytics', 'Enable event analytics and reporting', true),
('events.reminders', 'Event Reminders', 'Enable event reminder notifications', true),
('calendar.integration', 'Calendar Integration', 'Enable external calendar synchronization', true);

-- Insert search feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('search', 'Search Feature', 'Enable search functionality', true),
('search.advanced-filters', 'Advanced Search Filters', 'Enable advanced search filters', true);

-- Insert business feature flags (IMPLEMENTED)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('announcements', 'Announcement System', 'Enable system-wide announcements', true),
('reports', 'Report System', 'Enable user reporting system', true);
