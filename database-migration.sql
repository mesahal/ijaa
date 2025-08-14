-- 🔄 IJAA Database Migration Script
-- This script migrates data from the old unified database to the new separated microservices databases

-- =====================================================
-- 1. BACKUP EXISTING DATA (IMPORTANT!)
-- =====================================================

-- Before running this migration, create a backup:
-- pg_dump -h localhost -U root -d ijaa > ijaa_backup_$(date +%Y%m%d_%H%M%S).sql

-- =====================================================
-- 2. MIGRATE USER SERVICE DATA
-- =====================================================

-- Connect to the old database to read data
\c ijaa;

-- Migrate users table
INSERT INTO ijaa_users.users (id, user_id, username, password, active, created_at, updated_at)
SELECT id, user_id, username, password, active, created_at, updated_at
FROM users
ON CONFLICT (id) DO NOTHING;

-- Migrate admins table
INSERT INTO ijaa_users.admins (id, name, email, password_hash, role, active, created_at, updated_at)
SELECT id, name, email, password_hash, role, active, created_at, updated_at
FROM admins
ON CONFLICT (id) DO NOTHING;

-- Migrate profiles table
INSERT INTO ijaa_users.profiles (id, username, user_id, name, profession, location, bio, phone, linkedin, website, batch, email, facebook, show_phone, show_linkedin, show_website, show_email, show_facebook, connections, created_at, updated_at)
SELECT id, username, user_id, name, profession, location, bio, phone, linkedin, website, batch, email, facebook, show_phone, show_linkedin, show_website, show_email, show_facebook, connections, created_at, updated_at
FROM profiles
ON CONFLICT (id) DO NOTHING;

-- Migrate connections table
INSERT INTO ijaa_users.connections (id, requester_username, receiver_username, status, created_at)
SELECT id, requester_username, receiver_username, status, created_at
FROM connections
ON CONFLICT (id) DO NOTHING;

-- Migrate interests table
INSERT INTO ijaa_users.interests (id, username, user_id, interest, created_at, updated_at)
SELECT id, username, user_id, interest, created_at, updated_at
FROM interests
ON CONFLICT (id) DO NOTHING;

-- Migrate experiences table
INSERT INTO ijaa_users.experiences (id, username, user_id, title, company, period, description, created_at, updated_at)
SELECT id, username, user_id, title, company, period, description, created_at, updated_at
FROM experiences
ON CONFLICT (id) DO NOTHING;

-- Migrate announcements table
INSERT INTO ijaa_users.announcements (id, title, content, category, active, is_urgent, author_name, author_email, image_url, view_count, created_at, updated_at)
SELECT id, title, content, category, active, is_urgent, author_name, author_email, image_url, view_count, created_at, updated_at
FROM announcements
ON CONFLICT (id) DO NOTHING;

-- Migrate reports table
INSERT INTO ijaa_users.reports (id, title, description, category, status, priority, reporter_name, reporter_email, assigned_to, admin_notes, attachment_url, created_at, updated_at)
SELECT id, title, description, category, status, priority, reporter_name, reporter_email, assigned_to, admin_notes, attachment_url, created_at, updated_at
FROM reports
ON CONFLICT (id) DO NOTHING;

-- Migrate feature_flags table
INSERT INTO ijaa_users.feature_flags (id, feature_name, enabled, description, created_at, updated_at)
SELECT id, feature_name, enabled, description, created_at, updated_at
FROM feature_flags
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- 3. MIGRATE EVENT SERVICE DATA
-- =====================================================

-- Migrate events table
INSERT INTO ijaa_events.events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, created_at, updated_at)
SELECT id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, created_at, updated_at
FROM events
ON CONFLICT (id) DO NOTHING;

-- Migrate event_participations table
INSERT INTO ijaa_events.event_participations (id, event_id, participant_username, status, message, created_at, updated_at)
SELECT id, event_id, participant_username, status, message, created_at, updated_at
FROM event_participations
ON CONFLICT (id) DO NOTHING;

-- Migrate event_invitations table
INSERT INTO ijaa_events.event_invitations (id, event_id, invited_username, invited_by_username, personal_message, is_read, is_responded, created_at, updated_at)
SELECT id, event_id, invited_username, invited_by_username, personal_message, is_read, is_responded, created_at, updated_at
FROM event_invitations
ON CONFLICT (id) DO NOTHING;

-- Migrate event_comments table
INSERT INTO ijaa_events.event_comments (id, event_id, username, content, is_edited, is_deleted, parent_comment_id, likes, replies, created_at, updated_at)
SELECT id, event_id, username, content, is_edited, is_deleted, parent_comment_id, likes, replies, created_at, updated_at
FROM event_comments
ON CONFLICT (id) DO NOTHING;

-- Migrate event_media table
INSERT INTO ijaa_events.event_media (id, event_id, uploaded_by_username, file_name, file_url, file_type, file_size, caption, media_type, is_approved, likes, created_at, updated_at)
SELECT id, event_id, uploaded_by_username, file_name, file_url, file_type, file_size, caption, media_type, is_approved, likes, created_at, updated_at
FROM event_media
ON CONFLICT (id) DO NOTHING;

-- Migrate event_reminders table
INSERT INTO ijaa_events.event_reminders (id, event_id, username, reminder_time, reminder_type, is_sent, is_active, custom_message, channel, created_at, updated_at)
SELECT id, event_id, username, reminder_time, reminder_type, is_sent, is_active, custom_message, channel, created_at, updated_at
FROM event_reminders
ON CONFLICT (id) DO NOTHING;

-- Migrate recurring_events table
INSERT INTO ijaa_events.recurring_events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, recurrence_type, recurrence_interval, recurrence_end_date, recurrence_days, max_occurrences, generate_instances, created_at, updated_at)
SELECT id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, recurrence_type, recurrence_interval, recurrence_end_date, recurrence_days, max_occurrences, generate_instances, created_at, updated_at
FROM recurring_events
ON CONFLICT (id) DO NOTHING;

-- Migrate event_templates table
INSERT INTO ijaa_events.event_templates (id, template_name, name, created_by_username, category, is_public, is_active, title, description, location, event_type, is_online, meeting_link, max_participants, organizer_name, organizer_email, invite_message, privacy, default_duration_minutes, default_start_time, default_end_time, supports_recurrence, default_recurrence_type, default_recurrence_interval, default_recurrence_days, usage_count, average_rating, total_ratings, tags, created_at, updated_at)
SELECT id, template_name, name, created_by_username, category, is_public, is_active, title, description, location, event_type, is_online, meeting_link, max_participants, organizer_name, organizer_email, invite_message, privacy, default_duration_minutes, default_start_time, default_end_time, supports_recurrence, default_recurrence_type, default_recurrence_interval, default_recurrence_days, usage_count, average_rating, total_ratings, tags, created_at, updated_at
FROM event_templates
ON CONFLICT (id) DO NOTHING;

-- Migrate event_analytics table
INSERT INTO ijaa_events.event_analytics (id, event_id, event_title, organizer_username, total_invitations, confirmed_attendees, actual_attendees, no_shows, maybe_attendees, declined_attendees, pending_responses, total_comments, total_media_uploads, total_reminders, first_rsvp_time, last_rsvp_time, average_response_time_hours, attendance_rate, response_rate, engagement_rate, is_completed, event_start_date, event_end_date, created_at, updated_at, last_updated)
SELECT id, event_id, event_title, organizer_username, total_invitations, confirmed_attendees, actual_attendees, no_shows, maybe_attendees, declined_attendees, pending_responses, total_comments, total_media_uploads, total_reminders, first_rsvp_time, last_rsvp_time, average_response_time_hours, attendance_rate, response_rate, engagement_rate, is_completed, event_start_date, event_end_date, created_at, updated_at, last_updated
FROM event_analytics
ON CONFLICT (id) DO NOTHING;

-- Migrate calendar_integrations table
INSERT INTO ijaa_events.calendar_integrations (id, username, calendar_type, calendar_name, calendar_url, access_token, refresh_token, token_expiry, calendar_id, is_active, sync_to_external, sync_from_external, sync_recurring_events, sync_reminders, last_sync_error, last_sync_time, sync_frequency_hours, created_at, updated_at)
SELECT id, username, calendar_type, calendar_name, calendar_url, access_token, refresh_token, token_expiry, calendar_id, is_active, sync_to_external, sync_from_external, sync_recurring_events, sync_reminders, last_sync_error, last_sync_time, sync_frequency_hours, created_at, updated_at
FROM calendar_integrations
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- 4. VERIFY MIGRATION
-- =====================================================

-- Verify User Service Data
\c ijaa_users;
SELECT 'User Service Migration Verification:' as info;
SELECT 'Users:' as table_name, COUNT(*) as count FROM users;
SELECT 'Profiles:' as table_name, COUNT(*) as count FROM profiles;
SELECT 'Admins:' as table_name, COUNT(*) as count FROM admins;
SELECT 'Connections:' as table_name, COUNT(*) as count FROM connections;
SELECT 'Interests:' as table_name, COUNT(*) as count FROM interests;
SELECT 'Experiences:' as table_name, COUNT(*) as count FROM experiences;
SELECT 'Announcements:' as table_name, COUNT(*) as count FROM announcements;
SELECT 'Reports:' as table_name, COUNT(*) as count FROM reports;
SELECT 'Feature Flags:' as table_name, COUNT(*) as count FROM feature_flags;

-- Verify Event Service Data
\c ijaa_events;
SELECT 'Event Service Migration Verification:' as info;
SELECT 'Events:' as table_name, COUNT(*) as count FROM events;
SELECT 'Event Participations:' as table_name, COUNT(*) as count FROM event_participations;
SELECT 'Event Invitations:' as table_name, COUNT(*) as count FROM event_invitations;
SELECT 'Event Comments:' as table_name, COUNT(*) as count FROM event_comments;
SELECT 'Event Media:' as table_name, COUNT(*) as count FROM event_media;
SELECT 'Event Reminders:' as table_name, COUNT(*) as count FROM event_reminders;
SELECT 'Recurring Events:' as table_name, COUNT(*) as count FROM recurring_events;
SELECT 'Event Templates:' as table_name, COUNT(*) as count FROM event_templates;
SELECT 'Event Analytics:' as table_name, COUNT(*) as count FROM event_analytics;
SELECT 'Calendar Integrations:' as table_name, COUNT(*) as count FROM calendar_integrations;

-- =====================================================
-- 5. UPDATE SEQUENCES
-- =====================================================

-- Update User Service Sequences
\c ijaa_users;
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('admins_id_seq', (SELECT MAX(id) FROM admins));
SELECT setval('profiles_id_seq', (SELECT MAX(id) FROM profiles));
SELECT setval('connections_id_seq', (SELECT MAX(id) FROM connections));
SELECT setval('interests_id_seq', (SELECT MAX(id) FROM interests));
SELECT setval('experiences_id_seq', (SELECT MAX(id) FROM experiences));
SELECT setval('announcements_id_seq', (SELECT MAX(id) FROM announcements));
SELECT setval('reports_id_seq', (SELECT MAX(id) FROM reports));
SELECT setval('feature_flags_id_seq', (SELECT MAX(id) FROM feature_flags));

-- Update Event Service Sequences
\c ijaa_events;
SELECT setval('events_id_seq', (SELECT MAX(id) FROM events));
SELECT setval('event_participations_id_seq', (SELECT MAX(id) FROM event_participations));
SELECT setval('event_invitations_id_seq', (SELECT MAX(id) FROM event_invitations));
SELECT setval('event_comments_id_seq', (SELECT MAX(id) FROM event_comments));
SELECT setval('event_media_id_seq', (SELECT MAX(id) FROM event_media));
SELECT setval('event_reminders_id_seq', (SELECT MAX(id) FROM event_reminders));
SELECT setval('recurring_events_id_seq', (SELECT MAX(id) FROM recurring_events));
SELECT setval('event_templates_id_seq', (SELECT MAX(id) FROM event_templates));
SELECT setval('event_analytics_id_seq', (SELECT MAX(id) FROM event_analytics));
SELECT setval('calendar_integrations_id_seq', (SELECT MAX(id) FROM calendar_integrations));

-- =====================================================
-- 6. CLEANUP (OPTIONAL - ONLY AFTER VERIFICATION)
-- =====================================================

-- WARNING: Only run this section after verifying that all data has been migrated correctly!

-- Connect to old database
\c ijaa;

-- Drop old tables (only after successful migration verification)
-- DROP TABLE IF EXISTS calendar_integrations CASCADE;
-- DROP TABLE IF EXISTS event_analytics CASCADE;
-- DROP TABLE IF EXISTS event_templates CASCADE;
-- DROP TABLE IF EXISTS recurring_events CASCADE;
-- DROP TABLE IF EXISTS event_reminders CASCADE;
-- DROP TABLE IF EXISTS event_media CASCADE;
-- DROP TABLE IF EXISTS event_comments CASCADE;
-- DROP TABLE IF EXISTS event_invitations CASCADE;
-- DROP TABLE IF EXISTS event_participations CASCADE;
-- DROP TABLE IF EXISTS events CASCADE;
-- DROP TABLE IF EXISTS feature_flags CASCADE;
-- DROP TABLE IF EXISTS reports CASCADE;
-- DROP TABLE IF EXISTS announcements CASCADE;
-- DROP TABLE IF EXISTS experiences CASCADE;
-- DROP TABLE IF EXISTS interests CASCADE;
-- DROP TABLE IF EXISTS connections CASCADE;
-- DROP TABLE IF EXISTS profiles CASCADE;
-- DROP TABLE IF EXISTS admins CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

SELECT '✅ Database Migration Complete!' as status;
SELECT '📊 User Service Database: ijaa_users' as user_db;
SELECT '📊 Event Service Database: ijaa_events' as event_db;
SELECT '🔍 Please verify the data migration before dropping old tables' as warning;
SELECT '🚀 Ready to start microservices with separated databases!' as next_step;
