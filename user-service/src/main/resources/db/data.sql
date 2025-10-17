-- User Service Initial Data - Essential Data Only
-- This file inserts essential initial data for the IJAA user service
-- Includes countries, cities, feature flags, and 1 admin account

-- Insert default admin user
INSERT INTO admins (name, email, password_hash, role, active) VALUES
('System Administrator', 'admin@ijaa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true)
ON CONFLICT (email) DO NOTHING;

-- Insert comprehensive feature flags for the current system
INSERT INTO feature_flags (id, feature_name, enabled, description, created_at, updated_at, display_name, parent_id) VALUES
-- Core User Features
(1, 'user.registration', true, 'Enable user registration functionality', '2025-08-27 02:36:53.431459', '2025-08-29 15:35:06.135007', 'User Registration', 4),
(2, 'user.login', true, 'Enable user login functionality', '2025-08-27 02:36:53.431459', '2025-08-29 15:09:25.722963', 'User Login', 4),
(45, 'user.logout', true, 'Enable user logout functionality', '2025-10-01 12:00:00.000000', '2025-10-01 12:00:00.000000', 'User Logout', 4),
(3, 'user.password-change', true, 'Enable user password change functionality', '2025-08-27 02:36:53.431459', '2025-08-29 15:35:03.610380', 'User Password Change', 4),
(4, 'user.profile', true, 'Enable user profile management features', '2025-08-27 02:36:53.431459', '2025-08-29 15:35:11.752641', 'User Profile Features', NULL),
(5, 'user.experiences', true, 'Enable user experience management features', '2025-08-27 02:36:53.431459', '2025-08-30 13:50:06.756997', 'User Experiences', 4),
(6, 'user.interests', true, 'Enable user interest management features', '2025-08-27 02:36:53.431459', '2025-08-29 15:55:53.352431', 'User Interests', 4),
(30, 'user.settings', true, 'Enable user settings and preferences functionality', '2025-08-27 02:36:53.431459', '2025-08-29 15:35:11.752641', 'User Settings', 4),
(39, 'user.location', true, 'Enable user location functionality', '2025-09-06 16:44:25.281104', '2025-09-06 16:44:25.281104', 'User Location', 4),

-- Admin Features
(7, 'admin.features', true, 'Enable admin functionality', '2025-08-27 02:36:53.433767', '2025-08-27 02:36:53.433767', 'Admin Features', NULL),
(8, 'admin.user-management', true, 'Enable admin user management features', '2025-08-27 02:36:53.433767', '2025-08-29 15:20:53.056025', 'Admin User Management', 7),
(9, 'admin.announcements', true, 'Enable admin announcement management', '2025-08-27 02:36:53.433767', '2025-08-27 02:36:53.433767', 'Admin Announcements', 7),
(10, 'admin.reports', true, 'Enable admin report management', '2025-08-27 02:36:53.433767', '2025-08-27 02:36:53.433767', 'Admin Reports', 7),
(11, 'admin.auth', true, 'Enable admin authentication features', '2025-08-27 02:36:53.433767', '2025-08-27 02:36:53.433767', 'Admin Authentication', 7),
(46, 'admin.logout', true, 'Enable admin logout functionality', '2025-10-01 12:00:00.000000', '2025-10-01 12:00:00.000000', 'Admin Logout', 7),

-- Alumni Search
(12, 'alumni.search', true, 'Enable alumni search functionality', '2025-08-27 02:36:53.435055', '2025-08-30 13:50:02.621134', 'Alumni Search', NULL),

-- File Upload Features
(34, 'file-upload', true, 'Enable file upload functionality', '2025-08-27 02:40:42.174515', '2025-08-29 15:26:46.465337', 'File Upload', NULL),
(13, 'file-upload.profile-photo', true, 'Enable profile photo upload', '2025-08-27 02:36:53.436219', '2025-08-29 15:30:47.072664', 'Profile Photo Upload', 34),
(14, 'file-upload.cover-photo', true, 'Enable cover photo upload', '2025-08-27 02:36:53.436219', '2025-08-29 15:30:41.271865', 'Cover Photo Upload', 34),

-- File Management Features
(35, 'file', true, 'Enable file management functionality', '2025-08-27 02:40:42.177800', '2025-08-27 02:40:42.177800', 'File Management', NULL),
(15, 'file-download', true, 'Enable file download functionality', '2025-08-27 02:36:53.436219', '2025-08-29 15:19:17.213189', 'File Download', 35),
(16, 'file-delete', true, 'Enable file deletion functionality', '2025-08-27 02:36:53.436219', '2025-08-27 02:36:53.436219', 'File Delete', 35),

-- Event Management Features
(17, 'events', true, 'Enable event management functionality', '2025-08-27 02:36:53.437514', '2025-08-29 15:20:37.161630', 'Event Management', NULL),
(18, 'events.creation', true, 'Enable event creation functionality', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Creation', 17),
(19, 'events.update', true, 'Enable event update functionality', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Update', 17),
(20, 'events.delete', true, 'Enable event deletion functionality', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Delete', 17),
(21, 'events.participation', true, 'Enable event participation and RSVP functionality', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Participation', 17),
(22, 'events.invitations', true, 'Enable event invitation functionality', '2025-08-27 02:36:53.437514', '2025-08-29 15:35:16.990246', 'Event Invitations', 17),
(23, 'events.comments', true, 'Enable event commenting system', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Comments', 17),
(24, 'events.media', true, 'Enable media attachments for events', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Media', 17),
(25, 'events.templates', true, 'Enable reusable event templates', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Templates', 17),
(26, 'events.recurring', true, 'Enable recurring event patterns', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Recurring Events', 17),
(27, 'events.analytics', true, 'Enable event analytics and reporting', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Analytics', 17),
(28, 'events.reminders', true, 'Enable event reminder notifications', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Reminders', 17),
(29, 'calendar.integration', true, 'Enable external calendar synchronization', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Calendar Integration', 17),

-- System Features
(32, 'announcements', true, 'Enable system-wide announcements', '2025-08-27 02:36:53.440857', '2025-08-27 02:36:53.440857', 'Announcement System', NULL),
(33, 'reports', true, 'Enable user reporting system', '2025-08-27 02:36:53.440857', '2025-08-29 15:30:59.274431', 'Report System', NULL),

-- Missing Event Features
(40, 'events.banner', true, 'Enable event banner upload and management', '2025-08-27 02:36:53.437514', '2025-08-27 02:36:53.437514', 'Event Banner', 17),

-- Event Posts Features
(47, 'events.posts', true, 'Enable event posts and discussion functionality', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Event Posts', 17),
(48, 'events.posts.create', true, 'Enable creating posts in events', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Create Posts', 47),
(49, 'events.posts.update', true, 'Enable updating posts in events', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Update Posts', 47),
(50, 'events.posts.delete', true, 'Enable deleting posts in events', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Delete Posts', 47),
(51, 'events.posts.like', true, 'Enable liking posts in events', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Like Posts', 47),
(52, 'events.posts.media', true, 'Enable media upload for posts (images and videos)', '2025-12-01 12:00:00.000000', '2025-12-01 12:00:00.000000', 'Post Media', 47),

-- Search Features
(41, 'search', true, 'Enable basic search functionality', '2025-08-27 02:36:53.440857', '2025-08-27 02:36:53.440857', 'Search', NULL),
(42, 'search.advanced-filters', true, 'Enable advanced search filters and features', '2025-08-27 02:36:53.440857', '2025-08-27 02:36:53.440857', 'Advanced Search Filters', 41),

-- Additional User Features
(43, 'user.refresh', true, 'Enable user token refresh functionality', '2025-08-27 02:36:53.440857', '2025-08-27 02:36:53.440857', 'User Token Refresh', 4),

-- System Health Features
(44, 'system.health', true, 'Enable system health check endpoints', '2025-08-27 02:36:53.440857', '2025-08-27 02:36:53.440857', 'System Health Checks', NULL)
ON CONFLICT (id) DO NOTHING;

-- ==============================================
-- COUNTRIES DATA
-- ==============================================

-- Insert countries data (simplified - only id and name)
INSERT INTO countries (id, name) VALUES
(1, 'Afghanistan'),
(2, 'Aland Islands'),
(3, 'Albania'),
(4, 'Algeria'),
(5, 'American Samoa'),
(6, 'Andorra'),
(7, 'Angola'),
(8, 'Anguilla'),
(9, 'Antarctica'),
(10, 'Antigua and Barbuda'),
(11, 'Argentina'),
(12, 'Armenia'),
(13, 'Aruba'),
(14, 'Australia'),
(15, 'Austria'),
(16, 'Azerbaijan'),
(17, 'The Bahamas'),
(18, 'Bahrain'),
(19, 'Bangladesh'),
(20, 'Barbados'),
(21, 'Belarus'),
(22, 'Belgium'),
(23, 'Belize'),
(24, 'Benin'),
(25, 'Bermuda'),
(26, 'Bhutan'),
(27, 'Bolivia'),
(28, 'Bosnia and Herzegovina'),
(29, 'Botswana'),
(30, 'Bouvet Island'),
(31, 'Brazil'),
(32, 'British Indian Ocean Territory'),
(33, 'Brunei'),
(34, 'Bulgaria'),
(35, 'Burkina Faso'),
(36, 'Burundi'),
(37, 'Cambodia'),
(38, 'Cameroon'),
(39, 'Canada'),
(40, 'Cape Verde'),
(41, 'Cayman Islands'),
(42, 'Central African Republic'),
(43, 'Chad'),
(44, 'Chile'),
(45, 'China'),
(46, 'Christmas Island'),
(47, 'Cocos (Keeling) Islands'),
(48, 'Colombia'),
(49, 'Comoros'),
(50, 'Congo'),
(51, 'Democratic Republic of the Congo'),
(52, 'Cook Islands'),
(53, 'Costa Rica'),
(54, 'Cote D''Ivoire (Ivory Coast)'),
(55, 'Croatia'),
(56, 'Cuba'),
(57, 'Cyprus'),
(58, 'Czech Republic'),
(59, 'Denmark'),
(60, 'Djibouti'),
(61, 'Dominica'),
(62, 'Dominican Republic'),
(63, 'Timor-Leste'),
(64, 'Ecuador'),
(65, 'Egypt'),
(66, 'El Salvador'),
(67, 'Equatorial Guinea'),
(68, 'Eritrea'),
(69, 'Estonia'),
(70, 'Ethiopia'),
(71, 'Falkland Islands'),
(72, 'Faroe Islands'),
(73, 'Fiji Islands'),
(74, 'Finland'),
(75, 'France'),
(76, 'French Guiana'),
(77, 'French Polynesia'),
(78, 'French Southern Territories'),
(79, 'Gabon'),
(80, 'The Gambia'),
(81, 'Georgia'),
(82, 'Germany'),
(83, 'Ghana'),
(84, 'Gibraltar'),
(85, 'Greece'),
(86, 'Greenland'),
(87, 'Grenada'),
(88, 'Guadeloupe'),
(89, 'Guam'),
(90, 'Guatemala'),
(91, 'Guernsey'),
(92, 'Guinea'),
(93, 'Guinea-Bissau'),
(94, 'Guyana'),
(95, 'Haiti'),
(96, 'Heard Island and McDonald Islands'),
(97, 'Honduras'),
(98, 'Hong Kong S.A.R.'),
(99, 'Hungary'),
(100, 'Iceland'),
(101, 'India'),
(102, 'Indonesia'),
(103, 'Iran'),
(104, 'Iraq'),
(105, 'Ireland'),
(106, 'Israel'),
(107, 'Italy'),
(108, 'Jamaica'),
(109, 'Japan'),
(110, 'Jersey'),
(111, 'Jordan'),
(112, 'Kazakhstan'),
(113, 'Kenya'),
(114, 'Kiribati'),
(115, 'Kuwait'),
(116, 'Kyrgyzstan'),
(117, 'Laos'),
(118, 'Latvia'),
(119, 'Lebanon'),
(120, 'Lesotho'),
(121, 'Liberia'),
(122, 'Libya'),
(123, 'Liechtenstein'),
(124, 'Lithuania'),
(125, 'Luxembourg'),
(126, 'Macau S.A.R.'),
(127, 'Macedonia'),
(128, 'Madagascar'),
(129, 'Malawi'),
(130, 'Malaysia'),
(131, 'Maldives'),
(132, 'Mali'),
(133, 'Malta'),
(134, 'Man (Isle of)'),
(135, 'Marshall Islands'),
(136, 'Martinique'),
(137, 'Mauritania'),
(138, 'Mauritius'),
(139, 'Mayotte'),
(140, 'Mexico'),
(141, 'Micronesia'),
(142, 'Moldova'),
(143, 'Monaco'),
(144, 'Mongolia'),
(145, 'Montenegro'),
(146, 'Montserrat'),
(147, 'Morocco'),
(148, 'Mozambique'),
(149, 'Myanmar'),
(150, 'Namibia'),
(151, 'Nauru'),
(152, 'Nepal'),
(153, 'Netherlands'),
(154, 'Netherlands Antilles'),
(155, 'New Caledonia'),
(156, 'New Zealand'),
(157, 'Nicaragua'),
(158, 'Niger'),
(159, 'Nigeria'),
(160, 'Niue'),
(161, 'Norfolk Island'),
(162, 'North Korea'),
(163, 'Northern Mariana Islands'),
(164, 'Norway'),
(165, 'Oman'),
(166, 'Pakistan'),
(167, 'Palau'),
(168, 'Palestine'),
(169, 'Panama'),
(170, 'Papua New Guinea'),
(171, 'Paraguay'),
(172, 'Peru'),
(173, 'Philippines'),
(174, 'Pitcairn Island'),
(175, 'Poland'),
(176, 'Portugal'),
(177, 'Puerto Rico'),
(178, 'Qatar'),
(179, 'Reunion'),
(180, 'Romania'),
(181, 'Russia'),
(182, 'Rwanda'),
(183, 'Saint Helena'),
(184, 'Saint Kitts and Nevis'),
(185, 'Saint Lucia'),
(186, 'Saint Pierre and Miquelon'),
(187, 'Saint Vincent and the Grenadines'),
(188, 'Samoa'),
(189, 'San Marino'),
(190, 'Sao Tome and Principe'),
(191, 'Saudi Arabia'),
(192, 'Senegal'),
(193, 'Serbia'),
(194, 'Seychelles'),
(195, 'Sierra Leone'),
(196, 'Singapore'),
(197, 'Slovakia'),
(198, 'Slovenia'),
(199, 'Solomon Islands'),
(200, 'Somalia'),
(201, 'South Africa'),
(202, 'South Georgia and the South Sandwich Islands'),
(203, 'South Korea'),
(204, 'South Sudan'),
(205, 'Spain'),
(206, 'Sri Lanka'),
(207, 'Sudan'),
(208, 'Suriname'),
(209, 'Svalbard and Jan Mayen'),
(210, 'Swaziland'),
(211, 'Sweden'),
(212, 'Switzerland'),
(213, 'Syria'),
(214, 'Taiwan'),
(215, 'Tajikistan'),
(216, 'Tanzania'),
(217, 'Thailand'),
(218, 'Togo'),
(219, 'Tokelau'),
(220, 'Tonga'),
(221, 'Trinidad and Tobago'),
(222, 'Tunisia'),
(223, 'Turkey'),
(224, 'Turkmenistan'),
(225, 'Turks and Caicos Islands'),
(226, 'Tuvalu'),
(227, 'Uganda'),
(228, 'Ukraine'),
(229, 'United Arab Emirates'),
(230, 'United Kingdom'),
(231, 'United States'),
(232, 'United States Minor Outlying Islands'),
(233, 'Uruguay'),
(234, 'Uzbekistan'),
(235, 'Vanuatu'),
(236, 'Vatican City State (Holy See)'),
(237, 'Venezuela'),
(238, 'Vietnam'),
(239, 'Virgin Islands (British)'),
(240, 'Virgin Islands (US)'),
(241, 'Wallis and Futuna Islands'),
(242, 'Western Sahara'),
(243, 'Yemen'),
(244, 'Zambia'),
(245, 'Zimbabwe')
ON CONFLICT (id) DO NOTHING;

-- ==============================================
-- CITIES DATA
-- ==============================================

-- Insert cities data (simplified - only id, name, country_id)
INSERT INTO cities (id, name, country_id) VALUES
-- Afghanistan (country_id: 1)
(1, 'Kabul', 1),
(2, 'Kandahar', 1),
(3, 'Herat', 1),

-- Albania (country_id: 3)
(4, 'Tirana', 3),
(5, 'Durrës', 3),
(6, 'Vlorë', 3),

-- Algeria (country_id: 4)
(7, 'Algiers', 4),
(8, 'Oran', 4),
(9, 'Constantine', 4),

-- Australia (country_id: 14)
(10, 'Sydney', 14),
(11, 'Melbourne', 14),
(12, 'Brisbane', 14),
(13, 'Perth', 14),
(14, 'Adelaide', 14),
(15, 'Canberra', 14),

-- Austria (country_id: 15)
(16, 'Vienna', 15),
(17, 'Graz', 15),
(18, 'Linz', 15),
(19, 'Salzburg', 15),
(20, 'Innsbruck', 15),

-- Azerbaijan (country_id: 16)
(21, 'Baku', 16),
(22, 'Ganja', 16),
(23, 'Sumqayit', 16),

-- Bahamas (country_id: 17)
(24, 'Nassau', 17),
(25, 'Freeport', 17),

-- Bahrain (country_id: 18)
(26, 'Manama', 18),
(27, 'Riffa', 18),
(28, 'Muharraq', 18),

-- Bangladesh (country_id: 19)
(29, 'Dhaka', 19),
(30, 'Chittagong', 19),
(31, 'Khulna', 19),
(32, 'Rajshahi', 19),

-- Barbados (country_id: 20)
(33, 'Bridgetown', 20),
(34, 'Speightstown', 20),

-- Belarus (country_id: 21)
(35, 'Minsk', 21),
(36, 'Gomel', 21),
(37, 'Mogilev', 21),
(38, 'Vitebsk', 21),

-- Belgium (country_id: 22)
(39, 'Brussels', 22),
(40, 'Antwerp', 22),
(41, 'Ghent', 22),
(42, 'Charleroi', 22),
(43, 'Liège', 22),

-- Belize (country_id: 23)
(44, 'Belmopan', 23),
(45, 'Belize City', 23),
(46, 'San Ignacio', 23),

-- Benin (country_id: 24)
(47, 'Porto-Novo', 24),
(48, 'Cotonou', 24),
(49, 'Parakou', 24),

-- Bermuda (country_id: 25)
(50, 'Hamilton', 25),
(51, 'St. George', 25),

-- Bhutan (country_id: 26)
(52, 'Thimphu', 26),
(53, 'Phuntsholing', 26),
(54, 'Paro', 26),

-- Bolivia (country_id: 27)
(55, 'Sucre', 27),
(56, 'La Paz', 27),
(57, 'Santa Cruz', 27),
(58, 'Cochabamba', 27),

-- Bosnia and Herzegovina (country_id: 28)
(59, 'Sarajevo', 28),
(60, 'Banja Luka', 28),
(61, 'Tuzla', 28),
(62, 'Zenica', 28),

-- Botswana (country_id: 29)
(63, 'Gaborone', 29),
(64, 'Francistown', 29),
(65, 'Molepolole', 29),

-- Brazil (country_id: 31)
(66, 'Brasília', 31),
(67, 'São Paulo', 31),
(68, 'Rio de Janeiro', 31),
(69, 'Salvador', 31),
(70, 'Fortaleza', 31),
(71, 'Belo Horizonte', 31),
(72, 'Manaus', 31),
(73, 'Curitiba', 31),
(74, 'Recife', 31),
(75, 'Porto Alegre', 31),

-- Bulgaria (country_id: 34)
(76, 'Sofia', 34),
(77, 'Plovdiv', 34),
(78, 'Varna', 34),
(79, 'Burgas', 34),

-- Cambodia (country_id: 37)
(80, 'Phnom Penh', 37),
(81, 'Battambang', 37),
(82, 'Siem Reap', 37),
(83, 'Sihanoukville', 37),

-- Cameroon (country_id: 38)
(84, 'Yaoundé', 38),
(85, 'Douala', 38),
(86, 'Bamenda', 38),
(87, 'Bafoussam', 38),

-- Canada (country_id: 39)
(88, 'Ottawa', 39),
(89, 'Toronto', 39),
(90, 'Montreal', 39),
(91, 'Vancouver', 39),
(92, 'Calgary', 39),
(93, 'Edmonton', 39),
(94, 'Winnipeg', 39),
(95, 'Quebec City', 39),
(96, 'Hamilton', 39),
(97, 'Kitchener', 39),

-- China (country_id: 45)
(98, 'Beijing', 45),
(99, 'Shanghai', 45),
(100, 'Guangzhou', 45),
(101, 'Shenzhen', 45),
(102, 'Tianjin', 45),
(103, 'Chongqing', 45),
(104, 'Chengdu', 45),
(105, 'Nanjing', 45),
(106, 'Wuhan', 45),
(107, 'Xi''an', 45),

-- Colombia (country_id: 48)
(108, 'Bogotá', 48),
(109, 'Medellín', 48),
(110, 'Cali', 48),
(111, 'Barranquilla', 48),
(112, 'Cartagena', 48),

-- Croatia (country_id: 55)
(113, 'Zagreb', 55),
(114, 'Split', 55),
(115, 'Rijeka', 55),
(116, 'Osijek', 55),

-- Cuba (country_id: 56)
(117, 'Havana', 56),
(118, 'Santiago de Cuba', 56),
(119, 'Camagüey', 56),
(120, 'Holguín', 56),

-- Cyprus (country_id: 57)
(121, 'Nicosia', 57),
(122, 'Limassol', 57),
(123, 'Larnaca', 57),
(124, 'Paphos', 57),

-- Czech Republic (country_id: 58)
(125, 'Prague', 58),
(126, 'Brno', 58),
(127, 'Ostrava', 58),
(128, 'Plzeň', 58),

-- Denmark (country_id: 59)
(129, 'Copenhagen', 59),
(130, 'Aarhus', 59),
(131, 'Odense', 59),
(132, 'Aalborg', 59),

-- Egypt (country_id: 65)
(133, 'Cairo', 65),
(134, 'Alexandria', 65),
(135, 'Giza', 65),
(136, 'Shubra El Kheima', 65),
(137, 'Port Said', 65),

-- France (country_id: 75)
(138, 'Paris', 75),
(139, 'Marseille', 75),
(140, 'Lyon', 75),
(141, 'Toulouse', 75),
(142, 'Nice', 75),
(143, 'Nantes', 75),
(144, 'Strasbourg', 75),
(145, 'Montpellier', 75),
(146, 'Bordeaux', 75),
(147, 'Lille', 75),

-- Germany (country_id: 82)
(148, 'Berlin', 82),
(149, 'Hamburg', 82),
(150, 'Munich', 82),
(151, 'Cologne', 82),
(152, 'Frankfurt', 82),
(153, 'Stuttgart', 82),
(154, 'Düsseldorf', 82),
(155, 'Dortmund', 82),
(156, 'Essen', 82),
(157, 'Leipzig', 82),

-- Greece (country_id: 85)
(158, 'Athens', 85),
(159, 'Thessaloniki', 85),
(160, 'Patras', 85),
(161, 'Piraeus', 85),
(162, 'Larissa', 85),

-- India (country_id: 101)
(163, 'Mumbai', 101),
(164, 'Delhi', 101),
(165, 'Bangalore', 101),
(166, 'Hyderabad', 101),
(167, 'Chennai', 101),
(168, 'Kolkata', 101),
(169, 'Ahmedabad', 101),
(170, 'Pune', 101),
(171, 'Jaipur', 101),
(172, 'Surat', 101),

-- Indonesia (country_id: 102)
(173, 'Jakarta', 102),
(174, 'Surabaya', 102),
(175, 'Bandung', 102),
(176, 'Medan', 102),
(177, 'Semarang', 102),

-- Iran (country_id: 103)
(178, 'Tehran', 103),
(179, 'Mashhad', 103),
(180, 'Isfahan', 103),
(181, 'Tabriz', 103),
(182, 'Shiraz', 103),

-- Iraq (country_id: 104)
(183, 'Baghdad', 104),
(184, 'Basra', 104),
(185, 'Mosul', 104),
(186, 'Erbil', 104),

-- Ireland (country_id: 105)
(187, 'Dublin', 105),
(188, 'Cork', 105),
(189, 'Galway', 105),
(190, 'Limerick', 105),
(191, 'Waterford', 105),

-- Israel (country_id: 106)
(192, 'Jerusalem', 106),
(193, 'Tel Aviv', 106),
(194, 'Haifa', 106),
(195, 'Rishon LeZion', 106),
(196, 'Petah Tikva', 106),

-- Italy (country_id: 107)
(197, 'Rome', 107),
(198, 'Milan', 107),
(199, 'Naples', 107),
(200, 'Turin', 107),
(201, 'Palermo', 107),
(202, 'Genoa', 107),
(203, 'Bologna', 107),
(204, 'Florence', 107),
(205, 'Bari', 107),
(206, 'Catania', 107),

-- Japan (country_id: 109)
(207, 'Tokyo', 109),
(208, 'Yokohama', 109),
(209, 'Osaka', 109),
(210, 'Nagoya', 109),
(211, 'Sapporo', 109),
(212, 'Kobe', 109),
(213, 'Kyoto', 109),
(214, 'Fukuoka', 109),
(215, 'Kawasaki', 109),
(216, 'Saitama', 109),

-- Kenya (country_id: 113)
(217, 'Nairobi', 113),
(218, 'Mombasa', 113),
(219, 'Kisumu', 113),
(220, 'Nakuru', 113),

-- Malaysia (country_id: 130)
(221, 'Kuala Lumpur', 130),
(222, 'George Town', 130),
(223, 'Ipoh', 130),
(224, 'Shah Alam', 130),
(225, 'Johor Bahru', 130),

-- Mexico (country_id: 140)
(226, 'Mexico City', 140),
(227, 'Guadalajara', 140),
(228, 'Monterrey', 140),
(229, 'Puebla', 140),
(230, 'Tijuana', 140),
(231, 'Ciudad Juárez', 140),
(232, 'León', 140),
(233, 'Zapopan', 140),
(234, 'Monclova', 140),
(235, 'San Luis Potosí', 140),

-- Netherlands (country_id: 153)
(236, 'Amsterdam', 153),
(237, 'Rotterdam', 153),
(238, 'The Hague', 153),
(239, 'Utrecht', 153),
(240, 'Eindhoven', 153),

-- New Zealand (country_id: 156)
(241, 'Auckland', 156),
(242, 'Wellington', 156),
(243, 'Christchurch', 156),
(244, 'Hamilton', 156),
(245, 'Tauranga', 156),

-- Nigeria (country_id: 159)
(246, 'Lagos', 159),
(247, 'Kano', 159),
(248, 'Ibadan', 159),
(249, 'Kaduna', 159),
(250, 'Port Harcourt', 159),

-- Norway (country_id: 164)
(251, 'Oslo', 164),
(252, 'Bergen', 164),
(253, 'Trondheim', 164),
(254, 'Stavanger', 164),
(255, 'Drammen', 164),

-- Pakistan (country_id: 166)
(256, 'Karachi', 166),
(257, 'Lahore', 166),
(258, 'Faisalabad', 166),
(259, 'Rawalpindi', 166),
(260, 'Multan', 166),

-- Philippines (country_id: 173)
(261, 'Quezon City', 173),
(262, 'Manila', 173),
(263, 'Davao City', 173),
(264, 'Caloocan', 173),
(265, 'Cebu City', 173),

-- Poland (country_id: 175)
(266, 'Warsaw', 175),
(267, 'Kraków', 175),
(268, 'Łódź', 175),
(269, 'Wrocław', 175),
(270, 'Poznań', 175),

-- Portugal (country_id: 176)
(271, 'Lisbon', 176),
(272, 'Porto', 176),
(273, 'Vila Nova de Gaia', 176),
(274, 'Amadora', 176),
(275, 'Braga', 176),

-- Russia (country_id: 181)
(276, 'Moscow', 181),
(277, 'Saint Petersburg', 181),
(278, 'Novosibirsk', 181),
(279, 'Yekaterinburg', 181),
(280, 'Kazan', 181),
(281, 'Nizhny Novgorod', 181),
(282, 'Chelyabinsk', 181),
(283, 'Samara', 181),
(284, 'Omsk', 181),
(285, 'Rostov-on-Don', 181),

-- Saudi Arabia (country_id: 191)
(286, 'Riyadh', 191),
(287, 'Jeddah', 191),
(288, 'Mecca', 191),
(289, 'Medina', 191),
(290, 'Dammam', 191),

-- Singapore (country_id: 196)
(291, 'Singapore', 196),

-- South Africa (country_id: 201)
(292, 'Johannesburg', 201),
(293, 'Cape Town', 201),
(294, 'Durban', 201),
(295, 'Pretoria', 201),
(296, 'Port Elizabeth', 201),

-- South Korea (country_id: 203)
(297, 'Seoul', 203),
(298, 'Busan', 203),
(299, 'Incheon', 203),
(300, 'Daegu', 203),
(301, 'Daejeon', 203),
(302, 'Gwangju', 203),
(303, 'Suwon', 203),
(304, 'Ulsan', 203),
(305, 'Changwon', 203),
(306, 'Seongnam', 203),

-- Spain (country_id: 205)
(307, 'Madrid', 205),
(308, 'Barcelona', 205),
(309, 'Valencia', 205),
(310, 'Seville', 205),
(311, 'Zaragoza', 205),
(312, 'Málaga', 205),
(313, 'Murcia', 205),
(314, 'Palma', 205),
(315, 'Las Palmas', 205),
(316, 'Bilbao', 205),

-- Sweden (country_id: 211)
(317, 'Stockholm', 211),
(318, 'Gothenburg', 211),
(319, 'Malmö', 211),
(320, 'Uppsala', 211),
(321, 'Västerås', 211),

-- Switzerland (country_id: 212)
(322, 'Zurich', 212),
(323, 'Geneva', 212),
(324, 'Basel', 212),
(325, 'Bern', 212),
(326, 'Lausanne', 212),

-- Thailand (country_id: 217)
(327, 'Bangkok', 217),
(328, 'Chiang Mai', 217),
(329, 'Pattaya', 217),
(330, 'Phuket', 217),
(331, 'Hat Yai', 217),

-- Turkey (country_id: 223)
(332, 'Istanbul', 223),
(333, 'Ankara', 223),
(334, 'Izmir', 223),
(335, 'Bursa', 223),
(336, 'Antalya', 223),
(337, 'Adana', 223),
(338, 'Gaziantep', 223),
(339, 'Konya', 223),
(340, 'Mersin', 223),
(341, 'Diyarbakır', 223),

-- Ukraine (country_id: 228)
(342, 'Kyiv', 228),
(343, 'Kharkiv', 228),
(344, 'Odesa', 228),
(345, 'Dnipro', 228),
(346, 'Donetsk', 228),

-- United Kingdom (country_id: 230)
(347, 'London', 230),
(348, 'Birmingham', 230),
(349, 'Leeds', 230),
(350, 'Glasgow', 230),
(351, 'Sheffield', 230),
(352, 'Bradford', 230),
(353, 'Edinburgh', 230),
(354, 'Liverpool', 230),
(355, 'Manchester', 230),
(356, 'Bristol', 230),

-- United States (country_id: 231)
(357, 'New York', 231),
(358, 'Los Angeles', 231),
(359, 'Chicago', 231),
(360, 'Houston', 231),
(361, 'Phoenix', 231),
(362, 'Philadelphia', 231),
(363, 'San Antonio', 231),
(364, 'San Diego', 231),
(365, 'Dallas', 231),
(366, 'San Jose', 231),
(367, 'Austin', 231),
(368, 'Jacksonville', 231),
(369, 'Fort Worth', 231),
(370, 'Columbus', 231),
(371, 'Charlotte', 231),
(372, 'San Francisco', 231),
(373, 'Indianapolis', 231),
(374, 'Seattle', 231),
(375, 'Denver', 231),
(376, 'Washington', 231),

-- Vietnam (country_id: 238)
(377, 'Ho Chi Minh City', 238),
(378, 'Hanoi', 238),
(379, 'Da Nang', 238),
(380, 'Hai Phong', 238),
(381, 'Can Tho', 238)
ON CONFLICT (id) DO NOTHING;