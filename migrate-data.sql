-- 🔄 IJAA Data Migration Script (Corrected)
-- This script migrates data from the old unified database to the new separated databases

-- =====================================================
-- 1. MIGRATE USER SERVICE DATA
-- =====================================================

-- Connect to user service database
\c ijaa_users;

-- Migrate users table
INSERT INTO users (id, user_id, username, password, active, created_at, updated_at)
SELECT id, user_id, username, password, active, created_at, updated_at
FROM ijaa.users
ON CONFLICT (id) DO NOTHING;

-- Migrate admins table
INSERT INTO admins (id, name, email, password_hash, role, active, created_at, updated_at)
SELECT id, name, email, password_hash, role, active, created_at, updated_at
FROM ijaa.admins
ON CONFLICT (id) DO NOTHING;

-- Migrate profiles table
INSERT INTO profiles (id, username, user_id, name, profession, location, bio, phone, linkedin, website, batch, email, facebook, show_phone, show_linkedin, show_website, show_email, show_facebook, connections, created_at, updated_at)
SELECT id, username, user_id, name, profession, location, bio, phone, linkedin, website, batch, email, facebook, show_phone, show_linkedin, show_website, show_email, show_facebook, connections, created_at, updated_at
FROM ijaa.profiles
ON CONFLICT (id) DO NOTHING;

-- Migrate connections table
INSERT INTO connections (id, requester_username, receiver_username, status, created_at)
SELECT id, requester_username, receiver_username, status, created_at
FROM ijaa.connections
ON CONFLICT (id) DO NOTHING;

-- Migrate interests table
INSERT INTO interests (id, username, user_id, interest, created_at, updated_at)
SELECT id, username, user_id, interest, created_at, updated_at
FROM ijaa.interests
ON CONFLICT (id) DO NOTHING;

-- Migrate experiences table
INSERT INTO experiences (id, username, user_id, title, company, period, description, created_at, updated_at)
SELECT id, username, user_id, title, company, period, description, created_at, updated_at
FROM ijaa.experiences
ON CONFLICT (id) DO NOTHING;

-- Migrate announcements table
INSERT INTO announcements (id, title, content, category, active, is_urgent, author_name, author_email, image_url, view_count, created_at, updated_at)
SELECT id, title, content, category, active, is_urgent, author_name, author_email, image_url, view_count, created_at, updated_at
FROM ijaa.announcements
ON CONFLICT (id) DO NOTHING;

-- Migrate reports table
INSERT INTO reports (id, title, description, category, status, priority, reporter_name, reporter_email, assigned_to, admin_notes, attachment_url, created_at, updated_at)
SELECT id, title, description, category, status, priority, reporter_name, reporter_email, assigned_to, admin_notes, attachment_url, created_at, updated_at
FROM ijaa.reports
ON CONFLICT (id) DO NOTHING;

-- Note: feature_flags table already has default data from setup script

-- =====================================================
-- 2. MIGRATE EVENT SERVICE DATA
-- =====================================================

-- Connect to event service database
\c ijaa_events;

-- Migrate events table
INSERT INTO events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, created_at, updated_at)
SELECT id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, created_at, updated_at
FROM ijaa.events
ON CONFLICT (id) DO NOTHING;

-- Migrate event_participations table
INSERT INTO event_participations (id, event_id, participant_username, status, message, created_at, updated_at)
SELECT id, event_id, participant_username, status, message, created_at, updated_at
FROM ijaa.event_participations
ON CONFLICT (id) DO NOTHING;

-- Migrate event_invitations table
INSERT INTO event_invitations (id, event_id, invited_username, invited_by_username, personal_message, is_read, is_responded, created_at, updated_at)
SELECT id, event_id, invited_username, invited_by_username, personal_message, is_read, is_responded, created_at, updated_at
FROM ijaa.event_invitations
ON CONFLICT (id) DO NOTHING;

-- Migrate event_comments table
INSERT INTO event_comments (id, event_id, username, content, is_edited, is_deleted, parent_comment_id, likes, replies, created_at, updated_at)
SELECT id, event_id, username, content, is_edited, is_deleted, parent_comment_id, likes, replies, created_at, updated_at
FROM ijaa.event_comments
ON CONFLICT (id) DO NOTHING;

-- Migrate event_media table
INSERT INTO event_media (id, event_id, uploaded_by_username, file_name, file_url, file_type, file_size, caption, media_type, is_approved, likes, created_at, updated_at)
SELECT id, event_id, uploaded_by_username, file_name, file_url, file_type, file_size, caption, media_type, is_approved, likes, created_at, updated_at
FROM ijaa.event_media
ON CONFLICT (id) DO NOTHING;

-- Migrate event_reminders table
INSERT INTO event_reminders (id, event_id, username, reminder_time, reminder_type, is_sent, is_active, custom_message, channel, created_at, updated_at)
SELECT id, event_id, username, reminder_time, reminder_type, is_sent, is_active, custom_message, channel, created_at, updated_at
FROM ijaa.event_reminders
ON CONFLICT (id) DO NOTHING;

-- Migrate recurring_events table
INSERT INTO recurring_events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, recurrence_type, recurrence_interval, recurrence_end_date, recurrence_days, max_occurrences, generate_instances, created_at, updated_at)
SELECT id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, recurrence_type, recurrence_interval, recurrence_end_date, recurrence_days, max_occurrences, generate_instances, created_at, updated_at
FROM ijaa.recurring_events
ON CONFLICT (id) DO NOTHING;

-- Migrate event_templates table
INSERT INTO event_templates (id, template_name, name, created_by_username, category, is_public, is_active, title, description, location, event_type, is_online, meeting_link, max_participants, organizer_name, organizer_email, invite_message, privacy, default_duration_minutes, default_start_time, default_end_time, supports_recurrence, default_recurrence_type, default_recurrence_interval, default_recurrence_days, usage_count, average_rating, total_ratings, tags, created_at, updated_at)
SELECT id, template_name, name, created_by_username, category, is_public, is_active, title, description, location, event_type, is_online, meeting_link, max_participants, organizer_name, organizer_email, invite_message, privacy, default_duration_minutes, default_start_time, default_end_time, supports_recurrence, default_recurrence_type, default_recurrence_interval, default_recurrence_days, usage_count, average_rating, total_ratings, tags, created_at, updated_at
FROM ijaa.event_templates
ON CONFLICT (id) DO NOTHING;

-- Migrate event_analytics table
INSERT INTO event_analytics (id, event_id, event_title, organizer_username, total_invitations, confirmed_attendees, actual_attendees, no_shows, maybe_attendees, declined_attendees, pending_responses, total_comments, total_media_uploads, total_reminders, first_rsvp_time, last_rsvp_time, average_response_time_hours, attendance_rate, response_rate, engagement_rate, is_completed, event_start_date, event_end_date, created_at, updated_at, last_updated)
SELECT id, event_id, event_title, organizer_username, total_invitations, confirmed_attendees, actual_attendees, no_shows, maybe_attendees, declined_attendees, pending_responses, total_comments, total_media_uploads, total_reminders, first_rsvp_time, last_rsvp_time, average_response_time_hours, attendance_rate, response_rate, engagement_rate, is_completed, event_start_date, event_end_date, created_at, updated_at, last_updated
FROM ijaa.event_analytics
ON CONFLICT (id) DO NOTHING;

-- Migrate calendar_integrations table
INSERT INTO calendar_integrations (id, username, calendar_type, calendar_name, calendar_url, access_token, refresh_token, token_expiry, calendar_id, is_active, sync_to_external, sync_from_external, sync_recurring_events, sync_reminders, last_sync_error, last_sync_time, sync_frequency_hours, created_at, updated_at)
SELECT id, username, calendar_type, calendar_name, calendar_url, access_token, refresh_token, token_expiry, calendar_id, is_active, sync_to_external, sync_from_external, sync_recurring_events, sync_reminders, last_sync_error, last_sync_time, sync_frequency_hours, created_at, updated_at
FROM ijaa.calendar_integrations
ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- 3. VERIFY MIGRATION
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
-- 4. UPDATE SEQUENCES
-- =====================================================

-- Update User Service Sequences
\c ijaa_users;
SELECT setval('users_id_seq', COALESCE((SELECT MAX(id) FROM users), 1));
SELECT setval('admins_id_seq', COALESCE((SELECT MAX(id) FROM admins), 1));
SELECT setval('profiles_id_seq', COALESCE((SELECT MAX(id) FROM profiles), 1));
SELECT setval('connections_id_seq', COALESCE((SELECT MAX(id) FROM connections), 1));
SELECT setval('interests_id_seq', COALESCE((SELECT MAX(id) FROM interests), 1));
SELECT setval('experiences_id_seq', COALESCE((SELECT MAX(id) FROM experiences), 1));
SELECT setval('announcements_id_seq', COALESCE((SELECT MAX(id) FROM announcements), 1));
SELECT setval('reports_id_seq', COALESCE((SELECT MAX(id) FROM reports), 1));
SELECT setval('feature_flags_id_seq', COALESCE((SELECT MAX(id) FROM feature_flags), 1));

-- Update Event Service Sequences
\c ijaa_events;
SELECT setval('events_id_seq', COALESCE((SELECT MAX(id) FROM events), 1));
SELECT setval('event_participations_id_seq', COALESCE((SELECT MAX(id) FROM event_participations), 1));
SELECT setval('event_invitations_id_seq', COALESCE((SELECT MAX(id) FROM event_invitations), 1));
SELECT setval('event_comments_id_seq', COALESCE((SELECT MAX(id) FROM event_comments), 1));
SELECT setval('event_media_id_seq', COALESCE((SELECT MAX(id) FROM event_media), 1));
SELECT setval('event_reminders_id_seq', COALESCE((SELECT MAX(id) FROM event_reminders), 1));
SELECT setval('recurring_events_id_seq', COALESCE((SELECT MAX(id) FROM recurring_events), 1));
SELECT setval('event_templates_id_seq', COALESCE((SELECT MAX(id) FROM event_templates), 1));
SELECT setval('event_analytics_id_seq', COALESCE((SELECT MAX(id) FROM event_analytics), 1));
SELECT setval('calendar_integrations_id_seq', COALESCE((SELECT MAX(id) FROM calendar_integrations), 1));

-- =====================================================
-- COMPLETION MESSAGE
-- =====================================================

SELECT '✅ Data Migration Complete!' as status;
SELECT '📊 User Service Database: ijaa_users' as user_db;
SELECT '📊 Event Service Database: ijaa_events' as event_db;
SELECT '🔍 Data has been successfully migrated to separated databases' as info;
SELECT '🚀 Ready to start microservices with separated databases!' as next_step;
