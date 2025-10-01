-- Event Service Database Schema
-- This file creates all necessary tables for the IJAA event service

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    location VARCHAR(100),
    event_type VARCHAR(50),
    active BOOLEAN DEFAULT TRUE,
    privacy VARCHAR(20) DEFAULT 'PUBLIC',
    invite_message VARCHAR(500),
    is_online BOOLEAN DEFAULT FALSE,
    meeting_link VARCHAR(500),
    max_participants INTEGER NOT NULL,
    current_participants INTEGER DEFAULT 0,
    organizer_name VARCHAR(100),
    organizer_email VARCHAR(100),
    created_by_username VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event comments table
CREATE TABLE IF NOT EXISTS event_comments (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    is_edited BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    parent_comment_id BIGINT,
    likes INTEGER DEFAULT 0,
    replies INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event participations table
CREATE TABLE IF NOT EXISTS event_participations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    participant_username VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_id, participant_username)
);

-- Event invitations table
CREATE TABLE IF NOT EXISTS event_invitations (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL,
    invited_username VARCHAR(50) NOT NULL,
    invited_by_username VARCHAR(50) NOT NULL,
    personal_message VARCHAR(500),
    is_read BOOLEAN DEFAULT FALSE,
    is_responded BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_id, invited_username)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_events_start_date ON events(start_date);
CREATE INDEX IF NOT EXISTS idx_events_location ON events(location);
CREATE INDEX IF NOT EXISTS idx_events_active ON events(active);
CREATE INDEX IF NOT EXISTS idx_events_created_by ON events(created_by_username);
CREATE INDEX IF NOT EXISTS idx_event_comments_event_id ON event_comments(event_id);
CREATE INDEX IF NOT EXISTS idx_event_comments_username ON event_comments(username);
CREATE INDEX IF NOT EXISTS idx_event_comments_parent ON event_comments(parent_comment_id);
CREATE INDEX IF NOT EXISTS idx_event_participations_event_id ON event_participations(event_id);
CREATE INDEX IF NOT EXISTS idx_event_participations_username ON event_participations(participant_username);
CREATE INDEX IF NOT EXISTS idx_event_invitations_event_id ON event_invitations(event_id);
CREATE INDEX IF NOT EXISTS idx_event_invitations_invited ON event_invitations(invited_username);
