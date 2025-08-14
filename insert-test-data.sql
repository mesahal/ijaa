-- 🧪 IJAA Test Data Insertion Script
-- This script inserts realistic test data for all tables

-- =====================================================
-- 1. USER SERVICE DATA (ijaa_users database)
-- =====================================================

\c ijaa_users;

-- Insert Users
INSERT INTO users (id, user_id, username, password, active, created_at, updated_at) VALUES
(1, 'USER_ABC12345', 'john.doe', '$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnX8K', true, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'USER_DEF67890', 'jane.smith', '$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnX8K', true, '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(3, 'USER_GHI11111', 'mike.wilson', '$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnX8K', true, '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(4, 'USER_JKL22222', 'sarah.johnson', '$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnX8K', true, '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(5, 'USER_MNO33333', 'david.brown', '$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnX8K', true, '2024-01-19 14:00:00', '2024-01-19 14:00:00');

-- Insert Profiles
INSERT INTO profiles (id, username, user_id, name, profession, location, bio, phone, linkedin, website, batch, email, facebook, show_phone, show_linkedin, show_website, show_email, show_facebook, connections, created_at, updated_at) VALUES
(1, 'john.doe', 'USER_ABC12345', 'John Doe', 'Software Engineer', 'Mumbai, India', 'Passionate software engineer with 5+ years of experience in Java and Spring Boot.', '+91-9876543210', 'linkedin.com/in/johndoe', 'johndoe.dev', '2019', 'john.doe@example.com', 'facebook.com/johndoe', false, true, true, true, false, 15, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'jane.smith', 'USER_DEF67890', 'Jane Smith', 'Data Scientist', 'Bangalore, India', 'Data scientist specializing in machine learning and AI.', '+91-9876543211', 'linkedin.com/in/janesmith', 'janesmith.ai', '2020', 'jane.smith@example.com', 'facebook.com/janesmith', true, true, false, true, false, 23, '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(3, 'mike.wilson', 'USER_GHI11111', 'Mike Wilson', 'Product Manager', 'Delhi, India', 'Product manager with expertise in fintech and mobile apps.', '+91-9876543212', 'linkedin.com/in/mikewilson', 'mikewilson.pm', '2018', 'mike.wilson@example.com', 'facebook.com/mikewilson', false, true, true, false, true, 8, '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(4, 'sarah.johnson', 'USER_JKL22222', 'Sarah Johnson', 'UX Designer', 'Pune, India', 'Creative UX designer focused on user-centered design.', '+91-9876543213', 'linkedin.com/in/sarahjohnson', 'sarahjohnson.design', '2021', 'sarah.johnson@example.com', 'facebook.com/sarahjohnson', true, false, true, true, false, 12, '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(5, 'david.brown', 'USER_MNO33333', 'David Brown', 'DevOps Engineer', 'Hyderabad, India', 'DevOps engineer passionate about automation and cloud technologies.', '+91-9876543214', 'linkedin.com/in/davidbrown', 'davidbrown.dev', '2017', 'david.brown@example.com', 'facebook.com/davidbrown', false, true, true, true, true, 19, '2024-01-19 14:00:00', '2024-01-19 14:00:00');

-- Insert Connections
INSERT INTO connections (id, requester_username, receiver_username, status, created_at) VALUES
(1, 'john.doe', 'jane.smith', 'ACCEPTED', '2024-01-20 10:00:00'),
(2, 'john.doe', 'mike.wilson', 'ACCEPTED', '2024-01-21 11:00:00'),
(3, 'jane.smith', 'sarah.johnson', 'PENDING', '2024-01-22 12:00:00'),
(4, 'mike.wilson', 'david.brown', 'ACCEPTED', '2024-01-23 13:00:00'),
(5, 'sarah.johnson', 'john.doe', 'ACCEPTED', '2024-01-24 14:00:00');

-- Insert Interests
INSERT INTO interests (id, username, user_id, interest, created_at, updated_at) VALUES
(1, 'john.doe', 'USER_ABC12345', 'Java Development', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'john.doe', 'USER_ABC12345', 'Spring Boot', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(3, 'john.doe', 'USER_ABC12345', 'Microservices', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(4, 'jane.smith', 'USER_DEF67890', 'Machine Learning', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(5, 'jane.smith', 'USER_DEF67890', 'Python', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(6, 'jane.smith', 'USER_DEF67890', 'Data Analysis', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(7, 'mike.wilson', 'USER_GHI11111', 'Product Management', '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(8, 'mike.wilson', 'USER_GHI11111', 'Agile Methodologies', '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(9, 'sarah.johnson', 'USER_JKL22222', 'UI/UX Design', '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(10, 'sarah.johnson', 'USER_JKL22222', 'Figma', '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(11, 'david.brown', 'USER_MNO33333', 'Docker', '2024-01-19 14:00:00', '2024-01-19 14:00:00'),
(12, 'david.brown', 'USER_MNO33333', 'Kubernetes', '2024-01-19 14:00:00', '2024-01-19 14:00:00');

-- Insert Experiences
INSERT INTO experiences (id, username, user_id, title, company, period, description, created_at, updated_at) VALUES
(1, 'john.doe', 'USER_ABC12345', 'Senior Software Engineer', 'TechCorp India', '2022-Present', 'Leading development of microservices architecture using Spring Boot and Java.', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'john.doe', 'USER_ABC12345', 'Software Engineer', 'StartupXYZ', '2020-2022', 'Developed RESTful APIs and implemented CI/CD pipelines.', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(3, 'jane.smith', 'USER_DEF67890', 'Data Scientist', 'DataTech Solutions', '2021-Present', 'Building machine learning models for predictive analytics.', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(4, 'mike.wilson', 'USER_GHI11111', 'Product Manager', 'FinTech Innovations', '2019-Present', 'Managing product roadmap and leading cross-functional teams.', '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(5, 'sarah.johnson', 'USER_JKL22222', 'UX Designer', 'Design Studio Pro', '2021-Present', 'Creating user-centered design solutions for web and mobile applications.', '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(6, 'david.brown', 'USER_MNO33333', 'DevOps Engineer', 'CloudTech Systems', '2018-Present', 'Implementing infrastructure as code and managing cloud deployments.', '2024-01-19 14:00:00', '2024-01-19 14:00:00');

-- Insert Announcements
INSERT INTO announcements (id, title, content, category, active, is_urgent, author_name, author_email, image_url, view_count, created_at, updated_at) VALUES
(1, 'Welcome to IJAA Alumni Network!', 'Welcome to our new alumni networking platform. Connect with fellow alumni and stay updated with the latest events.', 'GENERAL', true, false, 'Admin', 'admin@ijaa.com', 'https://example.com/welcome.jpg', 45, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'Annual Alumni Meet 2024', 'Join us for the annual alumni meet on March 15th, 2024. Network with fellow alumni and share your experiences.', 'EVENT', true, true, 'Admin', 'admin@ijaa.com', 'https://example.com/alumni-meet.jpg', 78, '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(3, 'Job Opportunities Available', 'Several companies are hiring through our alumni network. Check the job board for latest opportunities.', 'CAREER', true, false, 'Admin', 'admin@ijaa.com', 'https://example.com/jobs.jpg', 32, '2024-01-17 12:00:00', '2024-01-17 12:00:00');

-- Insert Reports
INSERT INTO reports (id, title, description, category, status, priority, reporter_name, reporter_email, assigned_to, admin_notes, attachment_url, created_at, updated_at) VALUES
(1, 'Inappropriate Content', 'Found inappropriate content in a user profile.', 'CONTENT_MODERATION', 'PENDING', 'HIGH', 'john.doe', 'john.doe@example.com', 'admin', 'Under review', 'https://example.com/report1.pdf', '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 'Technical Issue', 'Unable to upload profile picture.', 'TECHNICAL', 'IN_PROGRESS', 'MEDIUM', 'jane.smith', 'jane.smith@example.com', 'tech_support', 'Investigating the issue', 'https://example.com/report2.pdf', '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 'Feature Request', 'Request for dark mode feature.', 'FEATURE_REQUEST', 'RESOLVED', 'LOW', 'mike.wilson', 'mike.wilson@example.com', 'product_team', 'Feature implemented', 'https://example.com/report3.pdf', '2024-01-22 12:00:00', '2024-01-22 12:00:00');

-- =====================================================
-- 2. EVENT SERVICE DATA (ijaa_events database)
-- =====================================================

\c ijaa_events;

-- Insert Events
INSERT INTO events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, created_at, updated_at) VALUES
(1, 'Annual Alumni Meet 2024', 'Join us for the annual alumni meet where we will network, share experiences, and celebrate our achievements.', '2024-03-15 18:00:00', '2024-03-15 22:00:00', 'IIT Jodhpur Campus', 'NETWORKING', true, 'PUBLIC', 'Looking forward to seeing all alumni at this special event!', false, NULL, 200, 45, 'Alumni Association', 'alumni@iitj.ac.in', 'john.doe', '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
(2, 'Tech Talk: AI in 2024', 'An insightful session on the latest developments in Artificial Intelligence and its impact on various industries.', '2024-02-20 19:00:00', '2024-02-20 21:00:00', 'Online', 'TECHNICAL', true, 'PUBLIC', 'Join us for an exciting discussion on AI trends!', true, 'https://meet.google.com/abc-defg-hij', 100, 23, 'Tech Community', 'tech@iitj.ac.in', 'jane.smith', '2024-01-16 11:00:00', '2024-01-16 11:00:00'),
(3, 'Career Fair 2024', 'Connect with top companies and explore career opportunities in various domains.', '2024-04-10 10:00:00', '2024-04-10 17:00:00', 'IIT Jodhpur Auditorium', 'CAREER', true, 'PUBLIC', 'Great opportunity to explore career options!', false, NULL, 300, 67, 'Placement Cell', 'placement@iitj.ac.in', 'mike.wilson', '2024-01-17 12:00:00', '2024-01-17 12:00:00'),
(4, 'Workshop: Web Development', 'Hands-on workshop on modern web development technologies including React, Node.js, and MongoDB.', '2024-02-25 14:00:00', '2024-02-25 18:00:00', 'Computer Science Lab', 'WORKSHOP', true, 'PUBLIC', 'Learn practical web development skills!', false, NULL, 50, 28, 'CS Department', 'cs@iitj.ac.in', 'sarah.johnson', '2024-01-18 13:00:00', '2024-01-18 13:00:00'),
(5, 'Sports Meet 2024', 'Annual sports meet featuring cricket, football, and other sports competitions.', '2024-03-30 08:00:00', '2024-03-30 18:00:00', 'Sports Complex', 'SPORTS', true, 'PUBLIC', 'Show your sportsmanship and team spirit!', false, NULL, 150, 34, 'Sports Committee', 'sports@iitj.ac.in', 'david.brown', '2024-01-19 14:00:00', '2024-01-19 14:00:00');

-- Insert Event Participations
INSERT INTO event_participations (id, event_id, participant_username, status, message, created_at, updated_at) VALUES
(1, 1, 'john.doe', 'CONFIRMED', 'Looking forward to the alumni meet!', '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 1, 'jane.smith', 'CONFIRMED', 'Excited to attend!', '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 1, 'mike.wilson', 'MAYBE', 'Will try to make it', '2024-01-22 12:00:00', '2024-01-22 12:00:00'),
(4, 2, 'john.doe', 'CONFIRMED', 'Interested in AI discussion', '2024-01-23 13:00:00', '2024-01-23 13:00:00'),
(5, 2, 'jane.smith', 'CONFIRMED', 'AI expert here!', '2024-01-24 14:00:00', '2024-01-24 14:00:00'),
(6, 3, 'mike.wilson', 'CONFIRMED', 'Looking for new opportunities', '2024-01-25 15:00:00', '2024-01-25 15:00:00'),
(7, 4, 'sarah.johnson', 'CONFIRMED', 'Web development enthusiast', '2024-01-26 16:00:00', '2024-01-26 16:00:00'),
(8, 5, 'david.brown', 'CONFIRMED', 'Sports lover!', '2024-01-27 17:00:00', '2024-01-27 17:00:00');

-- Insert Event Invitations
INSERT INTO event_invitations (id, event_id, invited_username, invited_by_username, personal_message, is_read, is_responded, created_at, updated_at) VALUES
(1, 1, 'john.doe', 'admin', 'Hope you can join us for this special event!', true, true, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 1, 'jane.smith', 'admin', 'Your presence would make this event special!', true, true, '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 2, 'john.doe', 'jane.smith', 'Thought you might be interested in this AI talk!', true, true, '2024-01-22 12:00:00', '2024-01-22 12:00:00'),
(4, 3, 'mike.wilson', 'admin', 'Great opportunity for career growth!', false, false, '2024-01-23 13:00:00', '2024-01-23 13:00:00'),
(5, 4, 'sarah.johnson', 'admin', 'Perfect for your design background!', true, true, '2024-01-24 14:00:00', '2024-01-24 14:00:00');

-- Insert Event Comments
INSERT INTO event_comments (id, event_id, username, content, is_edited, is_deleted, parent_comment_id, likes, replies, created_at, updated_at) VALUES
(1, 1, 'john.doe', 'Looking forward to meeting everyone!', false, false, NULL, 5, 2, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 1, 'jane.smith', 'Same here! It will be great to catch up.', false, false, 1, 3, 0, '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 1, 'mike.wilson', 'Count me in!', false, false, 1, 2, 0, '2024-01-22 12:00:00', '2024-01-22 12:00:00'),
(4, 2, 'jane.smith', 'This AI talk sounds fascinating!', false, false, NULL, 8, 1, '2024-01-23 13:00:00', '2024-01-23 13:00:00'),
(5, 2, 'john.doe', 'Agreed! AI is the future.', false, false, 4, 4, 0, '2024-01-24 14:00:00', '2024-01-24 14:00:00');

-- Insert Event Media
INSERT INTO event_media (id, event_id, uploaded_by_username, file_name, file_url, file_type, file_size, caption, media_type, is_approved, likes, created_at, updated_at) VALUES
(1, 1, 'admin', 'alumni_meet_2023.jpg', 'https://example.com/media/alumni_meet_2023.jpg', 'image/jpeg', 2048576, 'Last year alumni meet highlights', 'IMAGE', true, 12, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 2, 'jane.smith', 'ai_presentation.pdf', 'https://example.com/media/ai_presentation.pdf', 'application/pdf', 5120000, 'AI presentation slides', 'DOCUMENT', true, 8, '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 3, 'admin', 'career_fair_banner.jpg', 'https://example.com/media/career_fair_banner.jpg', 'image/jpeg', 1536000, 'Career fair promotional banner', 'IMAGE', true, 15, '2024-01-22 12:00:00', '2024-01-22 12:00:00');

-- Insert Event Reminders
INSERT INTO event_reminders (id, event_id, username, reminder_time, reminder_type, is_sent, is_active, custom_message, channel, created_at, updated_at) VALUES
(1, 1, 'john.doe', '2024-03-15 17:00:00', 'EMAIL', false, true, 'Reminder: Alumni Meet starts in 1 hour!', 'EMAIL', '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 2, 'jane.smith', '2024-02-20 18:30:00', 'SMS', false, true, 'Tech Talk starts in 30 minutes!', 'SMS', '2024-01-21 11:00:00', '2024-01-21 11:00:00'),
(3, 3, 'mike.wilson', '2024-04-10 09:00:00', 'EMAIL', false, true, 'Career Fair starts in 1 hour!', 'EMAIL', '2024-01-22 12:00:00', '2024-01-22 12:00:00');

-- Insert Recurring Events
INSERT INTO recurring_events (id, title, description, start_date, end_date, location, event_type, active, privacy, invite_message, is_online, meeting_link, max_participants, current_participants, organizer_name, organizer_email, created_by_username, recurrence_type, recurrence_interval, recurrence_end_date, recurrence_days, max_occurrences, generate_instances, created_at, updated_at) VALUES
(1, 'Weekly Study Group', 'Weekly study group for competitive exam preparation.', '2024-02-01 19:00:00', '2024-02-01 21:00:00', 'Library', 'STUDY', true, 'PUBLIC', 'Join our weekly study sessions!', false, NULL, 20, 8, 'Study Group', 'study@iitj.ac.in', 'john.doe', 'WEEKLY', 1, '2024-12-31 23:59:59', 'MONDAY', 52, true, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 'Monthly Tech Meetup', 'Monthly technology meetup to discuss latest trends.', '2024-02-15 18:00:00', '2024-02-15 20:00:00', 'Online', 'TECHNICAL', true, 'PUBLIC', 'Monthly tech discussions!', true, 'https://meet.google.com/xyz-uvwx-yz', 50, 15, 'Tech Community', 'tech@iitj.ac.in', 'jane.smith', 'MONTHLY', 1, '2024-12-31 23:59:59', 'THIRD_THURSDAY', 12, true, '2024-01-21 11:00:00', '2024-01-21 11:00:00');

-- Insert Event Templates
INSERT INTO event_templates (id, template_name, name, created_by_username, category, is_public, is_active, title, description, location, event_type, is_online, meeting_link, max_participants, organizer_name, organizer_email, invite_message, privacy, default_duration_minutes, default_start_time, default_end_time, supports_recurrence, default_recurrence_type, default_recurrence_interval, default_recurrence_days, usage_count, average_rating, total_ratings, tags, created_at, updated_at) VALUES
(1, 'tech_talk_template', 'Tech Talk Template', 'jane.smith', 'TECHNICAL', true, true, 'Tech Talk: {Topic}', 'An informative session on {Topic} and its applications.', 'Online', 'TECHNICAL', true, 'https://meet.google.com/meeting-link', 100, 'Tech Community', 'tech@iitj.ac.in', 'Join us for an exciting tech discussion!', 'PUBLIC', 120, '19:00:00', '21:00:00', true, 'MONTHLY', 1, 'THIRD_THURSDAY', 5, 4.5, 10, 'tech,talk,online', '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 'workshop_template', 'Workshop Template', 'sarah.johnson', 'WORKSHOP', true, true, 'Workshop: {Skill}', 'Hands-on workshop on {Skill} for beginners and intermediates.', 'Computer Lab', 'WORKSHOP', false, NULL, 30, 'CS Department', 'cs@iitj.ac.in', 'Learn practical skills in this workshop!', 'PUBLIC', 240, '14:00:00', '18:00:00', false, NULL, NULL, NULL, 3, 4.8, 8, 'workshop,hands-on,practical', '2024-01-21 11:00:00', '2024-01-21 11:00:00');

-- Insert Event Analytics
INSERT INTO event_analytics (id, event_id, event_title, organizer_username, total_invitations, confirmed_attendees, actual_attendees, no_shows, maybe_attendees, declined_attendees, pending_responses, total_comments, total_media_uploads, total_reminders, first_rsvp_time, last_rsvp_time, average_response_time_hours, attendance_rate, response_rate, engagement_rate, is_completed, event_start_date, event_end_date, created_at, updated_at, last_updated) VALUES
(1, 1, 'Annual Alumni Meet 2024', 'john.doe', 150, 45, 42, 3, 15, 5, 85, 3, 1, 45, '2024-01-20 10:00:00', '2024-01-25 15:00:00', 24, 93.33, 43.33, 85.00, false, '2024-03-15 18:00:00', '2024-03-15 22:00:00', '2024-01-20 10:00:00', '2024-01-25 15:00:00', '2024-01-25 15:00:00'),
(2, 2, 'Tech Talk: AI in 2024', 'jane.smith', 80, 23, 20, 3, 8, 2, 47, 2, 1, 23, '2024-01-23 13:00:00', '2024-01-26 16:00:00', 18, 86.96, 41.25, 75.00, false, '2024-02-20 19:00:00', '2024-02-20 21:00:00', '2024-01-23 13:00:00', '2024-01-26 16:00:00', '2024-01-26 16:00:00');

-- Insert Calendar Integrations
INSERT INTO calendar_integrations (id, username, calendar_type, calendar_name, calendar_url, access_token, refresh_token, token_expiry, calendar_id, is_active, sync_to_external, sync_from_external, sync_recurring_events, sync_reminders, last_sync_error, last_sync_time, sync_frequency_hours, created_at, updated_at) VALUES
(1, 'john.doe', 'GOOGLE', 'John Doe Calendar', 'https://calendar.google.com/calendar/embed?src=john.doe@gmail.com', 'ya29.a0AfH6SMC...', '1//04dX...', '2024-02-20 10:00:00', 'john.doe@gmail.com', true, true, true, true, true, NULL, '2024-01-20 10:00:00', 24, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(2, 'jane.smith', 'OUTLOOK', 'Jane Smith Calendar', 'https://outlook.office365.com/calendar/...', 'EwBwA8l6BAAU...', 'M.R3_BAY...', '2024-02-21 11:00:00', 'jane.smith@outlook.com', true, true, false, true, true, NULL, '2024-01-21 11:00:00', 24, '2024-01-21 11:00:00', '2024-01-21 11:00:00');

-- =====================================================
-- 3. UPDATE SEQUENCES
-- =====================================================

-- Update User Service Sequences
\c ijaa_users;
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('profiles_id_seq', (SELECT MAX(id) FROM profiles));
SELECT setval('connections_id_seq', (SELECT MAX(id) FROM connections));
SELECT setval('interests_id_seq', (SELECT MAX(id) FROM interests));
SELECT setval('experiences_id_seq', (SELECT MAX(id) FROM experiences));
SELECT setval('announcements_id_seq', (SELECT MAX(id) FROM announcements));
SELECT setval('reports_id_seq', (SELECT MAX(id) FROM reports));

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
-- COMPLETION MESSAGE
-- =====================================================

SELECT '✅ Test Data Insertion Complete!' as status;
SELECT '📊 User Service: 5 users, 5 profiles, 5 connections, 12 interests, 6 experiences, 3 announcements, 3 reports' as user_data;
SELECT '📊 Event Service: 5 events, 8 participations, 5 invitations, 5 comments, 3 media, 3 reminders, 2 recurring events, 2 templates, 2 analytics, 2 calendar integrations' as event_data;
SELECT '🚀 Ready for comprehensive API testing!' as next_step;
