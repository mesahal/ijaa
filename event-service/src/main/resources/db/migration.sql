-- Migration script to update event_comments table structure
-- This script handles the transition from event_id to post_id and adds author_name column

-- Check if the table exists and has the old structure
DO $$
BEGIN
    -- Check if event_comments table exists and has event_id column
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'event_comments') THEN
        -- Check if it has the old event_id column
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_comments' AND column_name = 'event_id') THEN
            -- Drop the old table and recreate with new structure
            DROP TABLE IF EXISTS event_comments CASCADE;
        END IF;
    END IF;
END $$;

-- Create the new event_comments table with post_id, author_name, and user_id
CREATE TABLE IF NOT EXISTS event_comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    is_edited BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    parent_comment_id BIGINT,
    likes INTEGER DEFAULT 0,
    replies INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- If the table already exists with post_id but without author_name, add the column
DO $$
BEGIN
    -- Check if event_comments table exists and has post_id but no author_name
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'event_comments') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_comments' AND column_name = 'post_id') THEN
            IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_comments' AND column_name = 'author_name') THEN
                -- Add the author_name column
                ALTER TABLE event_comments ADD COLUMN author_name VARCHAR(100);
                -- Update existing records to use username as author_name (temporary fallback)
                UPDATE event_comments SET author_name = username WHERE author_name IS NULL;
                -- Make the column NOT NULL after updating existing records
                ALTER TABLE event_comments ALTER COLUMN author_name SET NOT NULL;
            END IF;
        END IF;
    END IF;
END $$;

-- If the table already exists but without user_id, add the column
DO $$
BEGIN
    -- Check if event_comments table exists and has post_id but no user_id
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'event_comments') THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_comments' AND column_name = 'post_id') THEN
            IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_comments' AND column_name = 'user_id') THEN
                -- Add the user_id column
                ALTER TABLE event_comments ADD COLUMN user_id VARCHAR(50);
                -- Update existing records to use username as user_id (temporary fallback)
                UPDATE event_comments SET user_id = username WHERE user_id IS NULL;
                -- Make the column NOT NULL after updating existing records
                ALTER TABLE event_comments ALTER COLUMN user_id SET NOT NULL;
            END IF;
        END IF;
    END IF;
END $$;

-- Add user_id column to event_posts table if it doesn't exist
DO $$
BEGIN
    -- Check if event_posts table exists and doesn't have user_id column
    IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'event_posts') THEN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'event_posts' AND column_name = 'user_id') THEN
            -- Add the user_id column
            ALTER TABLE event_posts ADD COLUMN user_id VARCHAR(50);
            -- Note: We don't set NOT NULL constraint as existing posts won't have user_id
        END IF;
    END IF;
END $$;

-- Create indexes for the new table
CREATE INDEX IF NOT EXISTS idx_event_comments_post_id ON event_comments(post_id);
CREATE INDEX IF NOT EXISTS idx_event_comments_username ON event_comments(username);
CREATE INDEX IF NOT EXISTS idx_event_comments_parent ON event_comments(parent_comment_id);
