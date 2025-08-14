-- 🗄️ IJAA Microservices Database Setup Script
-- This script sets up the proper database separation for the microservices architecture

-- =====================================================
-- 1. CREATE DATABASES FOR MICROSERVICES
-- =====================================================

-- Create User Service Database
CREATE DATABASE ijaa_users;

-- Create Event Service Database  
CREATE DATABASE ijaa_events;

-- =====================================================
-- 2. USER SERVICE DATABASE SCHEMA (ijaa_users)
-- =====================================================

\c ijaa_users;

-- Core User Management Tables
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admins (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS profiles (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    profession VARCHAR(255),
    location VARCHAR(255),
    bio TEXT,
    phone VARCHAR(20),
    linkedin VARCHAR(255),
    website VARCHAR(255),
    batch VARCHAR(50),
    email VARCHAR(255),
    facebook VARCHAR(255),
    show_phone BOOLEAN DEFAULT false,
    show_linkedin BOOLEAN DEFAULT true,
    show_website BOOLEAN DEFAULT true,
    show_email BOOLEAN DEFAULT true,
    show_facebook BOOLEAN DEFAULT false,
    connections INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Networking Tables
CREATE TABLE IF NOT EXISTS connections (
    id BIGSERIAL PRIMARY KEY,
    requester_username VARCHAR(255) NOT NULL,
    receiver_username VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(requester_username, receiver_username)
);

CREATE TABLE IF NOT EXISTS interests (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    interest VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS experiences (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    period VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Content Management Tables
CREATE TABLE IF NOT EXISTS announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(100),
    active BOOLEAN DEFAULT true,
    is_urgent BOOLEAN DEFAULT false,
    author_name VARCHAR(255),
    author_email VARCHAR(255),
    image_url VARCHAR(500),
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100),
    status VARCHAR(50) DEFAULT 'PENDING',
    priority VARCHAR(50) DEFAULT 'MEDIUM',
    reporter_name VARCHAR(255),
    reporter_email VARCHAR(255),
    assigned_to VARCHAR(255),
    admin_notes TEXT,
    attachment_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Feature Flag Table
CREATE TABLE IF NOT EXISTS feature_flags (
    id BIGSERIAL PRIMARY KEY,
    feature_name VARCHAR(255) UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT false,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 3. EVENT SERVICE DATABASE SCHEMA (ijaa_events)
-- =====================================================

\c ijaa_events;

-- Core Event Tables
CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    location VARCHAR(255),
    event_type VARCHAR(100),
    active BOOLEAN DEFAULT true,
    privacy VARCHAR(50) DEFAULT 'PUBLIC',
    invite_message TEXT,
    is_online BOOLEAN DEFAULT false,
    meeting_link VARCHAR(500),
    max_participants INTEGER,
    current_participants INTEGER DEFAULT 0,
    organizer_name VARCHAR(255),
    organizer_email VARCHAR(255),
    created_by_username VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS event_participations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    participant_username VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_invitations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    invited_username VARCHAR(255) NOT NULL,
    invited_by_username VARCHAR(255) NOT NULL,
    personal_message TEXT,
    is_read BOOLEAN DEFAULT false,
    is_responded BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_comments (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_edited BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false,
    parent_comment_id BIGINT,
    likes INTEGER DEFAULT 0,
    replies INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES event_comments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_media (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    uploaded_by_username VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    caption TEXT,
    media_type VARCHAR(50),
    is_approved BOOLEAN DEFAULT true,
    likes INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_reminders (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    reminder_time TIMESTAMP NOT NULL,
    reminder_type VARCHAR(50) DEFAULT 'EMAIL',
    is_sent BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    custom_message TEXT,
    channel VARCHAR(50) DEFAULT 'EMAIL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- Advanced Event Features
CREATE TABLE IF NOT EXISTS recurring_events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    location VARCHAR(255),
    event_type VARCHAR(100),
    active BOOLEAN DEFAULT true,
    privacy VARCHAR(50) DEFAULT 'PUBLIC',
    invite_message TEXT,
    is_online BOOLEAN DEFAULT false,
    meeting_link VARCHAR(500),
    max_participants INTEGER,
    current_participants INTEGER DEFAULT 0,
    organizer_name VARCHAR(255),
    organizer_email VARCHAR(255),
    created_by_username VARCHAR(255) NOT NULL,
    recurrence_type VARCHAR(50),
    recurrence_interval INTEGER,
    recurrence_end_date TIMESTAMP,
    recurrence_days VARCHAR(100),
    max_occurrences INTEGER,
    generate_instances BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS event_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_by_username VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    is_public BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    event_type VARCHAR(100),
    is_online BOOLEAN DEFAULT false,
    meeting_link VARCHAR(500),
    max_participants INTEGER,
    organizer_name VARCHAR(255),
    organizer_email VARCHAR(255),
    invite_message TEXT,
    privacy VARCHAR(50) DEFAULT 'PUBLIC',
    default_duration_minutes INTEGER,
    default_start_time TIME,
    default_end_time TIME,
    supports_recurrence BOOLEAN DEFAULT false,
    default_recurrence_type VARCHAR(50),
    default_recurrence_interval INTEGER,
    default_recurrence_days VARCHAR(100),
    usage_count INTEGER DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.0,
    total_ratings INTEGER DEFAULT 0,
    tags VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Analytics Table
CREATE TABLE IF NOT EXISTS event_analytics (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    event_title VARCHAR(255),
    organizer_username VARCHAR(255),
    total_invitations INTEGER DEFAULT 0,
    confirmed_attendees INTEGER DEFAULT 0,
    actual_attendees INTEGER DEFAULT 0,
    no_shows INTEGER DEFAULT 0,
    maybe_attendees INTEGER DEFAULT 0,
    declined_attendees INTEGER DEFAULT 0,
    pending_responses INTEGER DEFAULT 0,
    total_comments INTEGER DEFAULT 0,
    total_media_uploads INTEGER DEFAULT 0,
    total_reminders INTEGER DEFAULT 0,
    first_rsvp_time TIMESTAMP,
    last_rsvp_time TIMESTAMP,
    average_response_time_hours INTEGER,
    attendance_rate DECIMAL(5,2),
    response_rate DECIMAL(5,2),
    engagement_rate DECIMAL(5,2),
    is_completed BOOLEAN DEFAULT false,
    event_start_date TIMESTAMP,
    event_end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

-- Calendar Integration
CREATE TABLE IF NOT EXISTS calendar_integrations (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    calendar_type VARCHAR(50) NOT NULL,
    calendar_name VARCHAR(255),
    calendar_url VARCHAR(500),
    access_token TEXT,
    refresh_token TEXT,
    token_expiry TIMESTAMP,
    calendar_id VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    sync_to_external BOOLEAN DEFAULT true,
    sync_from_external BOOLEAN DEFAULT true,
    sync_recurring_events BOOLEAN DEFAULT true,
    sync_reminders BOOLEAN DEFAULT true,
    last_sync_error TEXT,
    last_sync_time TIMESTAMP,
    sync_frequency_hours INTEGER DEFAULT 24,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 4. CREATE INDEXES FOR PERFORMANCE
-- =====================================================

-- User Service Indexes
\c ijaa_users;

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_user_id ON users(user_id);
CREATE INDEX idx_profiles_username ON profiles(username);
CREATE INDEX idx_profiles_user_id ON profiles(user_id);
CREATE INDEX idx_connections_requester ON connections(requester_username);
CREATE INDEX idx_connections_receiver ON connections(receiver_username);
CREATE INDEX idx_interests_username ON interests(username);
CREATE INDEX idx_experiences_username ON experiences(username);
CREATE INDEX idx_feature_flags_name ON feature_flags(feature_name);

-- Event Service Indexes
\c ijaa_events;

CREATE INDEX idx_events_created_by ON events(created_by_username);
CREATE INDEX idx_events_start_date ON events(start_date);
CREATE INDEX idx_events_event_type ON events(event_type);
CREATE INDEX idx_events_privacy ON events(privacy);
CREATE INDEX idx_event_participations_event_id ON event_participations(event_id);
CREATE INDEX idx_event_participations_participant ON event_participations(participant_username);
CREATE INDEX idx_event_invitations_event_id ON event_invitations(event_id);
CREATE INDEX idx_event_invitations_invited ON event_invitations(invited_username);
CREATE INDEX idx_event_comments_event_id ON event_comments(event_id);
CREATE INDEX idx_event_media_event_id ON event_media(event_id);
CREATE INDEX idx_event_reminders_event_id ON event_reminders(event_id);
CREATE INDEX idx_event_reminders_username ON event_reminders(username);
CREATE INDEX idx_recurring_events_created_by ON recurring_events(created_by_username);
CREATE INDEX idx_event_templates_created_by ON event_templates(created_by_username);
CREATE INDEX idx_event_analytics_event_id ON event_analytics(event_id);
CREATE INDEX idx_calendar_integrations_username ON calendar_integrations(username);

-- =====================================================
-- 5. INSERT DEFAULT DATA
-- =====================================================

-- Insert default feature flags
\c ijaa_users;

INSERT INTO feature_flags (feature_name, enabled, description) VALUES
('NEW_UI', true, 'Modern user interface'),
('CHAT_FEATURE', false, 'Real-time chat functionality'),
('EVENT_REGISTRATION', true, 'Event registration system'),
('PAYMENT_INTEGRATION', false, 'Payment processing'),
('SOCIAL_LOGIN', false, 'Social media login options'),
('DARK_MODE', true, 'Dark mode theme'),
('NOTIFICATIONS', true, 'Push notifications'),
('ADVANCED_SEARCH', true, 'Advanced search with filters'),
('ALUMNI_DIRECTORY', true, 'Public alumni directory'),
('MENTORSHIP_PROGRAM', false, 'Mentorship program matching')
ON CONFLICT (feature_name) DO NOTHING;

-- =====================================================
-- 6. VERIFICATION QUERIES
-- =====================================================

-- Verify User Service Database
\c ijaa_users;
SELECT 'User Service Database Tables:' as info;
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

-- Verify Event Service Database
\c ijaa_events;
SELECT 'Event Service Database Tables:' as info;
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;

-- =====================================================
-- 7. DATABASE PERMISSIONS
-- =====================================================

-- Grant permissions to the application user
GRANT ALL PRIVILEGES ON DATABASE ijaa_users TO root;
GRANT ALL PRIVILEGES ON DATABASE ijaa_events TO root;

\c ijaa_users;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO root;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO root;

\c ijaa_events;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO root;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO root;

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

SELECT '✅ IJAA Microservices Database Setup Complete!' as status;
SELECT '📊 User Service Database: ijaa_users' as user_db;
SELECT '📊 Event Service Database: ijaa_events' as event_db;
SELECT '🚀 Ready to start microservices!' as next_step;
