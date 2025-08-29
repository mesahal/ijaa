-- Add missing feature flags that are defined in constants but not in database
-- This migration adds feature flags for UI features, chat, and other missing functionality

-- Insert UI feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('NEW_UI', 'New User Interface', 'Enable new modern user interface design', false),
('DARK_MODE', 'Dark Mode', 'Enable dark mode theme option', false),
('NOTIFICATIONS', 'Notifications', 'Enable system notifications', true);

-- Insert authentication feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('SOCIAL_LOGIN', 'Social Login', 'Enable social media login options (Google, Facebook, etc.)', false);

-- Insert chat feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('chat', 'Chat System', 'Enable chat functionality between alumni', false),
('chat.file-sharing', 'Chat File Sharing', 'Enable file sharing in chat', false),
('chat.voice-calls', 'Chat Voice Calls', 'Enable voice calls in chat', false);

-- Insert search feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('ALUMNI_DIRECTORY', 'Alumni Directory', 'Enable comprehensive alumni directory', false);

-- Insert admin feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('admin.event-management', 'Admin Event Management', 'Enable admin event management features', false);

-- Insert business feature flags (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('PAYMENT_INTEGRATION', 'Payment Integration', 'Enable payment processing and integration', false),
('MENTORSHIP_PROGRAM', 'Mentorship Program', 'Enable mentorship program features', false);

-- Insert file upload feature flag (defined in constants but missing from DB)
INSERT INTO feature_flags (name, display_name, description, enabled) VALUES
('file-upload', 'File Upload', 'Enable general file upload functionality', true);
