-- Create feature_flags table with hierarchical structure
CREATE TABLE feature_flags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,   -- unique key like "chat", "chat.file-sharing"
    display_name VARCHAR(255),           -- human-readable label
    parent_id BIGINT NULL,               -- reference to parent flag (NULL for top-level)
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES feature_flags(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_feature_flags_name ON feature_flags(name);
CREATE INDEX idx_feature_flags_parent_id ON feature_flags(parent_id);
CREATE INDEX idx_feature_flags_enabled ON feature_flags(enabled);

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
