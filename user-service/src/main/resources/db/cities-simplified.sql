--
-- Simplified Cities Table - Only essential columns
--

-- Drop existing table if exists
DROP TABLE IF EXISTS public.cities CASCADE;

-- Create simplified cities table
CREATE TABLE public.cities (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    country_id bigint NOT NULL
);

-- Add primary key
ALTER TABLE public.cities ADD CONSTRAINT cities_pkey PRIMARY KEY (id);

-- Create sequence for auto-incrementing ID
CREATE SEQUENCE IF NOT EXISTS cities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Set the sequence as default for id column
ALTER TABLE public.cities ALTER COLUMN id SET DEFAULT nextval('cities_id_seq');
ALTER SEQUENCE cities_id_seq OWNED BY public.cities.id;

-- Add foreign key constraint to countries table
ALTER TABLE public.cities ADD CONSTRAINT cities_country_id_fkey 
    FOREIGN KEY (country_id) REFERENCES public.countries(id);

-- Create index on country_id for better performance
CREATE INDEX cities_country_id_idx ON public.cities(country_id);

-- Insert sample city data (only id, name, country_id)
-- Note: This is a small sample. You can add more cities as needed.
INSERT INTO public.cities (id, name, country_id) VALUES
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
(381, 'Can Tho', 238);

-- Set sequence to continue from the last inserted ID
SELECT setval('cities_id_seq', (SELECT MAX(id) FROM public.cities));
