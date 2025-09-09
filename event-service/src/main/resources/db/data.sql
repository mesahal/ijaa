-- Event Service Initial Data - Updated for Current IJAA System
-- This file inserts comprehensive initial data for the IJAA event service

-- Insert comprehensive sample events for 2024-2025
INSERT INTO events (title, description, event_date, end_date, location, organizer_id, organizer_name, max_participants, event_type, is_active, is_public) VALUES
-- Major Networking Events
('IJAA Annual Alumni Meetup 2024', 'Join us for the biggest alumni gathering of the year! Network with fellow alumni, share experiences, and celebrate our community. Features keynote speakers, networking sessions, and cultural performances.', '2024-12-15 18:00:00', '2024-12-15 22:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 200, 'NETWORKING', true, true),

('Tech Talk: Spring Boot Best Practices', 'Learn about Spring Boot best practices from industry experts. This workshop covers advanced topics including microservices, security, and performance optimization. Hands-on coding sessions included.', '2024-12-20 19:00:00', '2024-12-20 21:00:00', 'Online', 1, 'System Administrator', 100, 'WORKSHOP', true, true),

('Alumni Career Fair 2024', 'Connect with potential employers and explore career opportunities. Representatives from top tech companies, startups, and multinational corporations will be present. Bring your resume and portfolio!', '2024-12-25 10:00:00', '2024-12-25 16:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 300, 'CAREER', true, true),

('Code Review & Pair Programming Session', 'Collaborative code review session for alumni developers. Share your code, get feedback, and learn from peers. Perfect for improving coding skills and learning new techniques.', '2024-12-28 20:00:00', '2024-12-28 22:00:00', 'Online', 1, 'System Administrator', 50, 'TECHNICAL', true, true),

-- Technical Workshops
('Machine Learning Fundamentals Workshop', 'Introduction to machine learning concepts and practical applications. Learn about algorithms, data preprocessing, and model evaluation. No prior ML experience required.', '2025-01-10 14:00:00', '2025-01-10 17:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 80, 'WORKSHOP', true, true),

('Web Development Bootcamp', 'Intensive 3-day web development bootcamp covering HTML, CSS, JavaScript, and modern frameworks. Build a complete project from scratch.', '2025-01-15 09:00:00', '2025-01-17 17:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 60, 'BOOTCAMP', true, true),

('IoT Development Workshop', 'Learn about Internet of Things development, from sensors to cloud integration. Hands-on projects with Arduino and Raspberry Pi.', '2025-01-20 15:00:00', '2025-01-20 18:00:00', 'Chittagong, Bangladesh', 1, 'System Administrator', 40, 'WORKSHOP', true, true),

('Cybersecurity Awareness Session', 'Learn about cybersecurity best practices, common threats, and how to protect yourself and your organization. Interactive demonstrations included.', '2025-01-25 19:00:00', '2025-01-25 21:00:00', 'Online', 1, 'System Administrator', 120, 'AWARENESS', true, true),

-- Regional Meetups
('Sylhet Alumni Meetup', 'Regional meetup for alumni in Sylhet area. Network with local professionals and discuss regional opportunities.', '2025-02-01 18:00:00', '2025-02-01 20:00:00', 'Sylhet, Bangladesh', 1, 'System Administrator', 50, 'NETWORKING', true, true),

('Chittagong Tech Meetup', 'Technology-focused meetup in Chittagong. Share knowledge, discuss trends, and build connections.', '2025-02-05 19:00:00', '2025-02-05 21:00:00', 'Chittagong, Bangladesh', 1, 'System Administrator', 60, 'TECHNICAL', true, true),

('Rajshahi Alumni Gathering', 'Casual gathering for alumni in Rajshahi. Share experiences and build professional relationships.', '2025-02-10 17:00:00', '2025-02-10 19:00:00', 'Rajshahi, Bangladesh', 1, 'System Administrator', 40, 'NETWORKING', true, true),

-- Specialized Events
('Women in Tech Leadership Panel', 'Panel discussion featuring successful women leaders in technology. Learn about career progression, challenges, and opportunities in the tech industry.', '2025-02-15 16:00:00', '2025-02-15 18:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 100, 'PANEL', true, true),

('Startup Pitch Competition', 'Annual startup pitch competition for alumni entrepreneurs. Present your ideas to investors and industry experts. Prizes and mentorship opportunities available.', '2025-02-20 10:00:00', '2025-02-20 16:00:00', 'Dhaka, Bangladesh', 1, 'System Administrator', 150, 'COMPETITION', true, true),

('Open Source Contribution Workshop', 'Learn how to contribute to open source projects. Find projects, understand contribution guidelines, and make your first contribution.', '2025-02-25 14:00:00', '2025-02-25 17:00:00', 'Online', 1, 'System Administrator', 70, 'WORKSHOP', true, true)
ON CONFLICT DO NOTHING;

-- Insert comprehensive event comments with nested replies
INSERT INTO event_comments (event_id, user_id, username, author_name, content, parent_comment_id, likes_count) VALUES
-- Comments for Annual Meetup (Event ID 1)
(1, 1, 'admin', 'System Administrator', 'Looking forward to meeting everyone at the annual meetup! This will be our biggest gathering yet.', NULL, 15),
(1, 2, 'testuser', 'Test User', 'This sounds amazing! Count me in. Can''t wait to reconnect with old friends.', NULL, 8),
(1, 3, 'ahmed.khan', 'Ahmed Khan', 'Great initiative! I''ll definitely be there. Should we organize some breakout sessions?', NULL, 12),
(1, 4, 'fatima.rahman', 'Fatima Rahman', 'Excited about this! Will there be any specific themes for networking?', NULL, 6),
(1, 5, 'omar.hassan', 'Omar Hassan', 'Perfect timing! I''ll be in Dhaka during that period.', NULL, 4),

-- Replies to admin's comment
(1, 2, 'testuser', 'Test User', 'Yes, breakout sessions would be great! Maybe by department or industry?', 1, 3),
(1, 3, 'ahmed.khan', 'Ahmed Khan', 'I can help organize the tech industry breakout session if needed.', 1, 5),

-- Comments for Tech Talk (Event ID 2)
(2, 1, 'admin', 'System Administrator', 'This workshop will cover advanced Spring Boot topics including microservices architecture.', NULL, 20),
(2, 2, 'testuser', 'Test User', 'Perfect! I''ve been working with Spring Boot and would love to learn more advanced concepts.', NULL, 7),
(2, 6, 'sara.ahmed', 'Sara Ahmed', 'Will there be hands-on coding exercises? I learn better with practical examples.', NULL, 9),
(2, 7, 'nadia.islam', 'Nadia Islam', 'Great topic! Spring Boot is essential for modern Java development.', NULL, 6),

-- Comments for Career Fair (Event ID 3)
(3, 1, 'admin', 'System Administrator', 'We have confirmed participation from 25+ companies including Google, Microsoft, and local startups.', NULL, 25),
(3, 2, 'testuser', 'Test User', 'This is exactly what I need right now! Will there be interview preparation sessions?', NULL, 12),
(3, 8, 'karim.ali', 'Karim Ali', 'Great opportunity for recent graduates and experienced professionals alike.', NULL, 8),
(3, 9, 'leila.hossain', 'Leila Hossain', 'I''ll be there representing my company. Looking forward to meeting potential candidates.', NULL, 10),

-- Comments for ML Workshop (Event ID 5)
(5, 1, 'admin', 'System Administrator', 'This workshop is perfect for beginners. We''ll start from the basics and work our way up.', NULL, 18),
(5, 7, 'nadia.islam', 'Nadia Islam', 'I can help as a mentor if needed. Machine learning is my specialty.', NULL, 15),
(5, 2, 'testuser', 'Test User', 'Excited to learn ML! Will we get to work with real datasets?', NULL, 11),

-- Comments for Web Development Bootcamp (Event ID 6)
(6, 1, 'admin', 'System Administrator', 'This intensive bootcamp will take you from beginner to building full-stack applications.', NULL, 22),
(6, 6, 'sara.ahmed', 'Sara Ahmed', 'I''m interested! Will we learn modern frameworks like React or Vue?', NULL, 9),
(6, 3, 'ahmed.khan', 'Ahmed Khan', 'I can help with the JavaScript and React parts if needed.', NULL, 8)
ON CONFLICT DO NOTHING;

-- Insert comprehensive event participations
INSERT INTO event_participations (event_id, user_id, username, status) VALUES
-- Annual Meetup participations
(1, 1, 'admin', 'CONFIRMED'),
(1, 2, 'testuser', 'CONFIRMED'),
(1, 3, 'ahmed.khan', 'CONFIRMED'),
(1, 4, 'fatima.rahman', 'CONFIRMED'),
(1, 5, 'omar.hassan', 'CONFIRMED'),
(1, 6, 'sara.ahmed', 'CONFIRMED'),
(1, 7, 'nadia.islam', 'CONFIRMED'),
(1, 8, 'karim.ali', 'PENDING'),
(1, 9, 'leila.hossain', 'CONFIRMED'),

-- Tech Talk participations
(2, 1, 'admin', 'CONFIRMED'),
(2, 2, 'testuser', 'CONFIRMED'),
(2, 6, 'sara.ahmed', 'CONFIRMED'),
(2, 7, 'nadia.islam', 'CONFIRMED'),
(2, 3, 'ahmed.khan', 'PENDING'),

-- Career Fair participations
(3, 1, 'admin', 'CONFIRMED'),
(3, 2, 'testuser', 'CONFIRMED'),
(3, 8, 'karim.ali', 'CONFIRMED'),
(3, 9, 'leila.hossain', 'CONFIRMED'),
(3, 4, 'fatima.rahman', 'PENDING'),
(3, 5, 'omar.hassan', 'PENDING'),

-- Code Review participations
(4, 1, 'admin', 'CONFIRMED'),
(4, 2, 'testuser', 'CONFIRMED'),
(4, 3, 'ahmed.khan', 'CONFIRMED'),
(4, 6, 'sara.ahmed', 'CONFIRMED'),

-- ML Workshop participations
(5, 1, 'admin', 'CONFIRMED'),
(5, 7, 'nadia.islam', 'CONFIRMED'),
(5, 2, 'testuser', 'CONFIRMED'),
(5, 8, 'karim.ali', 'PENDING'),

-- Web Development Bootcamp participations
(6, 1, 'admin', 'CONFIRMED'),
(6, 6, 'sara.ahmed', 'CONFIRMED'),
(6, 2, 'testuser', 'CONFIRMED'),
(6, 3, 'ahmed.khan', 'CONFIRMED'),

-- IoT Workshop participations
(7, 1, 'admin', 'CONFIRMED'),
(7, 8, 'karim.ali', 'CONFIRMED'),
(7, 4, 'fatima.rahman', 'PENDING'),

-- Cybersecurity Session participations
(8, 1, 'admin', 'CONFIRMED'),
(8, 2, 'testuser', 'CONFIRMED'),
(8, 3, 'ahmed.khan', 'CONFIRMED'),
(8, 6, 'sara.ahmed', 'CONFIRMED'),
(8, 7, 'nadia.islam', 'CONFIRMED'),

-- Regional meetup participations
(9, 5, 'omar.hassan', 'CONFIRMED'),
(9, 9, 'leila.hossain', 'CONFIRMED'),
(10, 4, 'fatima.rahman', 'CONFIRMED'),
(10, 8, 'karim.ali', 'CONFIRMED'),
(11, 6, 'sara.ahmed', 'PENDING'),

-- Specialized event participations
(12, 1, 'admin', 'CONFIRMED'),
(12, 6, 'sara.ahmed', 'CONFIRMED'),
(12, 7, 'nadia.islam', 'CONFIRMED'),
(13, 1, 'admin', 'CONFIRMED'),
(13, 3, 'ahmed.khan', 'CONFIRMED'),
(13, 8, 'karim.ali', 'PENDING'),
(14, 1, 'admin', 'CONFIRMED'),
(14, 2, 'testuser', 'CONFIRMED'),
(14, 3, 'ahmed.khan', 'CONFIRMED')
ON CONFLICT DO NOTHING;

-- Insert comprehensive event invitations
INSERT INTO event_invitations (event_id, inviter_id, invitee_id, message, status) VALUES
-- Invitations for Annual Meetup
(1, 1, 2, 'You are invited to our annual alumni meetup! This is our biggest event of the year.', 'ACCEPTED'),
(1, 1, 3, 'Join us for the annual meetup. Great networking opportunity!', 'ACCEPTED'),
(1, 1, 4, 'You are invited to the annual meetup. Looking forward to seeing you there!', 'ACCEPTED'),
(1, 1, 5, 'Annual meetup invitation. Don''t miss this great opportunity!', 'PENDING'),

-- Invitations for Tech Talk
(2, 1, 2, 'Join us for this technical workshop on Spring Boot best practices.', 'ACCEPTED'),
(2, 1, 6, 'This workshop will be perfect for your web development skills.', 'ACCEPTED'),
(2, 1, 7, 'Machine learning expert like you would benefit from this workshop.', 'PENDING'),

-- Invitations for Career Fair
(3, 1, 2, 'Great opportunity for career growth. Join us at the career fair!', 'ACCEPTED'),
(3, 1, 8, 'Your IoT expertise would be valuable at the career fair.', 'ACCEPTED'),
(3, 1, 9, 'Represent your company at the career fair. Great networking opportunity!', 'PENDING'),

-- Invitations for Code Review
(4, 1, 2, 'Join our collaborative code review session. Share your knowledge!', 'ACCEPTED'),
(4, 1, 3, 'Your experience would be valuable in the code review session.', 'ACCEPTED'),
(4, 1, 6, 'Perfect opportunity to improve your coding skills.', 'PENDING'),

-- Invitations for ML Workshop
(5, 1, 2, 'Learn machine learning fundamentals in this hands-on workshop.', 'ACCEPTED'),
(5, 1, 8, 'IoT and ML go hand in hand. Join this workshop!', 'PENDING'),

-- Invitations for Web Development Bootcamp
(6, 1, 2, 'Intensive web development bootcamp. Perfect for skill enhancement!', 'ACCEPTED'),
(6, 1, 3, 'Your full-stack experience would be valuable in this bootcamp.', 'ACCEPTED'),

-- Invitations for IoT Workshop
(7, 1, 4, 'Learn about IoT development. Perfect for electrical engineers!', 'PENDING'),
(7, 1, 8, 'Your IoT expertise would be valuable in this workshop.', 'ACCEPTED'),

-- Invitations for Cybersecurity Session
(8, 1, 2, 'Learn about cybersecurity best practices. Essential knowledge!', 'ACCEPTED'),
(8, 1, 3, 'Security is crucial for developers. Join this session!', 'ACCEPTED'),
(8, 1, 6, 'Web developers need security knowledge. Join this session!', 'PENDING'),

-- Invitations for Regional Meetups
(9, 1, 5, 'Regional meetup in Sylhet. Great for local networking!', 'ACCEPTED'),
(9, 1, 9, 'Join the Sylhet alumni meetup. Local connections matter!', 'ACCEPTED'),
(10, 1, 4, 'Tech meetup in Chittagong. Perfect for your field!', 'ACCEPTED'),
(10, 1, 8, 'IoT and tech meetup in Chittagong. Join us!', 'ACCEPTED'),

-- Invitations for Specialized Events
(12, 1, 6, 'Women in tech leadership panel. Your perspective matters!', 'ACCEPTED'),
(12, 1, 7, 'Join the women in tech discussion. Great networking opportunity!', 'PENDING'),
(13, 1, 3, 'Startup pitch competition. Perfect for entrepreneurs!', 'ACCEPTED'),
(13, 1, 8, 'Showcase your IoT innovations at the startup competition!', 'PENDING'),
(14, 1, 2, 'Learn open source contribution. Great for developers!', 'ACCEPTED'),
(14, 1, 3, 'Your coding skills would be valuable in open source projects!', 'ACCEPTED')
ON CONFLICT DO NOTHING;
