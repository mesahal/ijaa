-- Event Service Initial Data
-- This file inserts sample event data for testing

-- Insert sample events
INSERT INTO events (title, description, event_date, end_date, location, organizer_id, organizer_name, max_participants, event_type) VALUES
('IJAA Annual Meetup 2024', 'Annual gathering of IJAA alumni to network and share experiences', '2024-12-15 18:00:00', '2024-12-15 22:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 100, 'NETWORKING'),
('Tech Talk: Spring Boot Best Practices', 'Learn about Spring Boot best practices from industry experts', '2024-12-20 19:00:00', '2024-12-20 21:00:00', 'Online', 1, 'System Administrator', 50, 'WORKSHOP'),
('Alumni Career Fair', 'Connect with potential employers and explore career opportunities', '2024-12-25 10:00:00', '2024-12-25 16:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 200, 'CAREER'),
('Code Review Session', 'Collaborative code review session for alumni developers', '2024-12-28 20:00:00', '2024-12-28 22:00:00', 'Online', 1, 'System Administrator', 30, 'TECHNICAL')
ON CONFLICT DO NOTHING;

-- Insert sample event comments
INSERT INTO event_comments (event_id, user_id, username, author_name, content) VALUES
(1, 1, 'admin', 'System Administrator', 'Looking forward to meeting everyone at the annual meetup!'),
(1, 2, 'testuser', 'Test User', 'This sounds great! Count me in.'),
(2, 1, 'admin', 'System Administrator', 'This workshop will cover advanced Spring Boot topics.'),
(3, 2, 'testuser', 'Test User', 'Perfect timing for career development!')
ON CONFLICT DO NOTHING;

-- Insert sample event participations
INSERT INTO event_participations (event_id, user_id, username, status) VALUES
(1, 1, 'admin', 'CONFIRMED'),
(1, 2, 'testuser', 'CONFIRMED'),
(2, 1, 'admin', 'CONFIRMED'),
(2, 2, 'testuser', 'PENDING'),
(3, 1, 'admin', 'CONFIRMED'),
(4, 2, 'testuser', 'CONFIRMED')
ON CONFLICT DO NOTHING;

-- Insert sample event invitations
INSERT INTO event_invitations (event_id, inviter_id, invitee_id, message) VALUES
(1, 1, 2, 'You are invited to our annual meetup!'),
(2, 1, 2, 'Join us for this technical workshop.'),
(3, 1, 2, 'Great opportunity for career growth!')
ON CONFLICT DO NOTHING;
