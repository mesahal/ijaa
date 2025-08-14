--
-- PostgreSQL database dump
--

-- Dumped from database version 12.22 (Ubuntu 12.22-0ubuntu0.20.04.4)
-- Dumped by pg_dump version 12.22 (Ubuntu 12.22-0ubuntu0.20.04.4)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: admins; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.admins (id, active, created_at, email, name, password_hash, role, updated_at) FROM stdin;
1	t	2025-07-31 01:51:12.870989	admin@ijaa.com	Administrator	$2a$12$y3fhHC5PdD5T5t2BECat4OAciXA2zhK7MS5XQLCl5l7xt.mh6KHja	ADMIN	2025-07-31 01:51:12.871015
3	t	2025-08-08 19:36:26.263214	newadmin2@ijaa.com	New Admin	$2a$12$ZldN/hDSHI1mBA/uu/.kS.fQZq72pnTzSjEnVE37/aSQ0LjhmFfiO	ADMIN	2025-08-08 19:36:26.263221
2	f	2025-08-03 11:10:12.407424	newadmin@ijaa.com	New Admin	$2a$12$cSelKrFkAWh7fQe2/52fZeLiMnV9iV75dYZTB1xCzUiECEIFzSWau	ADMIN	2025-08-08 19:57:03.398123
\.


--
-- Data for Name: announcements; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.announcements (id, active, author_email, author_name, category, content, created_at, image_url, is_urgent, title, updated_at, view_count) FROM stdin;
1328	t	admin@ijaa.com	Admin User	GENERAL	We are excited to welcome you back to our platform. Stay tuned for new features and updates.	2025-08-10 15:14:30.624232	\N	f	Welcome Back!	2025-08-10 15:14:30.624232	0
1329	t	events@ijaa.com	Event Manager	EVENT	Join us for an evening of networking and fun at the Alumni Meetup. Date: April 20, 2025. Location: Coffee House, Gulshan.	2025-08-10 15:14:30.629504	\N	f	Upcoming Event: Alumni Meetup	2025-08-10 15:14:30.629504	0
1330	t	system@ijaa.com	System Admin	NEWS	We are excited to announce the launch of our new user interface. Please check it out and provide feedback.	2025-08-10 15:14:30.63126	https://example.com/ui-update.jpg	t	Important Update: New User Interface	2025-08-10 15:14:30.63126	0
1331	t	system@ijaa.com	System Admin	URGENT	Scheduled maintenance will occur on Sunday from 2-4 AM. The platform will be temporarily unavailable.	2025-08-10 15:14:30.632972	\N	t	Urgent: System Maintenance	2025-08-10 15:14:30.632972	0
1332	t	career@ijaa.com	Career Manager	EVENT	Registration for the annual career fair is now open. Limited spots available!	2025-08-10 15:14:30.634394	https://example.com/career-fair.jpg	f	Career Fair Registration Open	2025-08-10 15:14:30.634394	0
1333	t	product@ijaa.com	Product Team	NEWS	We've launched a new alumni directory feature. Connect with fellow alumni easily!	2025-08-10 15:14:30.635978	\N	f	New Feature: Alumni Directory	2025-08-10 15:14:30.635978	0
1334	t	community@ijaa.com	Community Manager	SUCCESS	Congratulations to John Doe (Class of 2018) for being promoted to Senior Software Engineer at Google!	2025-08-10 15:14:30.637182	\N	f	Alumni Success Story: John Doe	2025-08-10 15:14:30.637182	0
1335	t	support@ijaa.com	Support Team	GENERAL	The platform will have limited support during the upcoming holidays. Regular support will resume on January 2nd.	2025-08-10 15:14:30.638411	\N	f	Holiday Schedule	2025-08-10 15:14:30.638411	0
1336	t	programs@ijaa.com	Program Manager	PROGRAM	We're launching a new mentorship program. Senior alumni can mentor junior alumni. Sign up now!	2025-08-10 15:14:30.642633	https://example.com/mentorship.jpg	t	Mentorship Program Launch	2025-08-10 15:14:30.642633	0
1337	t	research@ijaa.com	Research Team	SURVEY	Please take a moment to complete our annual alumni survey. Your feedback helps us improve our services.	2025-08-10 15:14:30.648026	\N	f	Alumni Survey	2025-08-10 15:14:30.648026	0
\.


--
-- Data for Name: connections; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.connections (id, created_at, receiver_username, requester_username, status) FROM stdin;
1	\N	rakib.hassan	current_user	ACCEPTED
2	\N	sarah.ahmed	current_user	PENDING
3	\N	current_user	fatima.khan	ACCEPTED
\.


--
-- Data for Name: experiences; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.experiences (id, company, description, period, title, created_at, updated_at, username, user_id) FROM stdin;
6	Tech Solutions Ltd	szxx zxc sdcvsdvsd	xcvxcv xcv cx	Software Engineer	2025-07-29 00:29:54.541851	2025-07-29 00:29:54.541899	mdsahal.info@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540e
7	Tech Solutions Ltd	zcxvxxxxxxxxxxxxxxxxxxxxxx	sdc dddd dzc	dczxczd	2025-08-02 11:53:13.341344	2025-08-02 11:53:13.341384	afrineva28@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540d
\.


--
-- Data for Name: interests; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.interests (id, created_at, interest, updated_at, user_id, username) FROM stdin;
1	2025-07-29 00:30:04.73926	bnvjh	2025-07-29 00:30:04.739421	ce6ceb99-edd2-4448-b85c-a85ad187540e	mdsahal.info@gmail.com
2	2025-07-29 00:30:09.834097	kjnljoi	2025-07-29 00:30:09.83413	ce6ceb99-edd2-4448-b85c-a85ad187540e	mdsahal.info@gmail.com
3	2025-08-02 02:23:14.281905	,jbnkjj	2025-08-02 02:23:14.281992	ce6ceb99-edd2-4448-b85c-a85ad187540d	afrineva28@gmail.com
4	2025-08-02 23:23:50.436443	cvbcvbcv	2025-08-02 23:23:50.436467	ce6ceb99-edd2-4448-b85c-a85ad187540e	mdsahal.info@gmail.com
\.


--
-- Data for Name: profiles; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.profiles (id, batch, bio, created_at, facebook, github, instagram, linkedin, location, name, phone, profession, skills, twitter, updated_at, website, linked_in, show_linked_in, show_phone, show_website, username, department, email, show_email, connections, show_facebook, user_id) FROM stdin;
3	2018	Designing intuitive user experiences.	2025-07-27 23:40:20.539007	https://facebook.com/ayesha.r	\N	\N	\N	Chittagong, Bangladesh	Ayesha Rahman	+8801711000001	UI/UX Designer	\N	\N	2025-07-27 23:40:20.551153	https://ayesha.design	https://linkedin.com/in/ayeshar	t	f	t	ayesha@gmail.com	\N	ayesha@example.com	t	0	f	ce6ceb99-edd2-4448-b85c-a85ad187540c
2	9	I am a teacher.	2025-07-27 20:53:57.35387	https://linkedin.com/in/johnsmith	\N	\N	\N	Dhaka	Saima Rahman	01813279398	Lecturer	\N	\N	2025-07-30 13:43:40.429239	https://johnsmith.dev	https://linkedin.com/in/johnsmith	t	t	t	afrineva28@gmail.com	\N	me.sahal2000@gmail.com	t	0	t	ce6ceb99-edd2-4448-b85c-a85ad187540d
4	string	string	2025-08-02 16:06:16.630105	string	\N	\N	\N	string	string	string	string	\N	\N	2025-08-02 16:06:16.659099	string	string	t	t	t	admin@ijaa.com	\N	string	t	0	t	8bd54f0a-a5f8-4ef5-a6ad-c14406c0bcaf
1	9	Software engineer and content creator Software engineer and content creator Software engineer and content creator Software engineer and content creator Software engineer and content creator	\N	https://facebook.com/mdsahal	\N	\N	\N	Bangladesh	Md Sahal	+8801700000012	Software Engineer	\N	\N	2025-08-02 23:23:24.487315	https://sahal.dev	https://linkedin.com/in/mdsahal	t	f	t	mdsahal.info@gmail.com	Ophthalmology	sahal@example.com	t	\N	f	ce6ceb99-edd2-4448-b85c-a85ad187540e
\.


--
-- Data for Name: reports; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.reports (id, admin_notes, assigned_to, attachment_url, category, created_at, description, priority, reporter_email, reporter_name, status, title, updated_at) FROM stdin;
1582	\N	\N	\N	USER_REPORT	2025-08-10 15:14:30.669497	User jane.smith posted offensive content on the platform.	MEDIUM	john.doe@email.com	John Doe	PENDING	User Behavior Report	2025-08-10 15:14:30.669497
1583	Investigating spam content	Admin User	\N	CONTENT_REPORT	2025-08-10 15:14:30.673944	User sarah.wilson posted spam on the platform.	HIGH	mike.johnson@email.com	Mike Johnson	IN_PROGRESS	Content Moderation	2025-08-10 15:14:30.673944
1584	\N	\N	\N	BUG_REPORT	2025-08-10 15:14:30.675557	Users experiencing login issues on mobile devices.	URGENT	david.brown@email.com	David Brown	PENDING	Bug Report: Login Issue	2025-08-10 15:14:30.675557
1585	Dark mode will be implemented in next release	Admin User	\N	FEATURE_REQUEST	2025-08-10 15:14:30.677339	Add dark mode option for better user experience.	LOW	sarah.wilson@email.com	Sarah Wilson	RESOLVED	Feature Request: Dark Mode	2025-08-10 15:14:30.677339
1586	Profile information corrected	Admin User	\N	USER_REPORT	2025-08-10 15:14:30.679437	User mike.johnson provided incorrect information on their profile.	MEDIUM	david.brown@email.com	David Brown	CLOSED	Profile Information	2025-08-10 15:14:30.679437
1587	Investigating server performance	Tech Lead	\N	SYSTEM_REPORT	2025-08-10 15:14:30.681426	Users reporting slow loading times during peak hours.	HIGH	system@ijaa.com	System Monitor	IN_PROGRESS	System Performance Issue	2025-08-10 15:14:30.681426
1588	\N	\N	\N	FEATURE_REQUEST	2025-08-10 15:14:30.682925	Add video calling feature for alumni networking.	MEDIUM	alex.chen@email.com	Alex Chen	PENDING	Feature Request: Video Calls	2025-08-10 15:14:30.682925
1589	Fake accounts removed and IP blocked	Admin User	\N	SECURITY_REPORT	2025-08-10 15:14:30.684126	Multiple fake accounts created with similar patterns.	HIGH	security@ijaa.com	Security Team	RESOLVED	Spam Account	2025-08-10 15:14:30.684126
1590	\N	\N	\N	BUG_REPORT	2025-08-10 15:14:30.685235	App crashes when accessing profile page on iOS devices.	URGENT	mobile@ijaa.com	Mobile User	PENDING	Mobile App Bug	2025-08-10 15:14:30.685235
1591	Working on screen reader support	UX Team	\N	FEATURE_REQUEST	2025-08-10 15:14:30.686351	Improve accessibility features for visually impaired users.	MEDIUM	accessibility@ijaa.com	Accessibility Advocate	IN_PROGRESS	Accessibility Request	2025-08-10 15:14:30.686351
1592	Privacy policy updated and communicated	Legal Team	\N	PRIVACY_REPORT	2025-08-10 15:14:30.687823	User concerned about data sharing practices.	LOW	privacy@ijaa.com	Privacy User	RESOLVED	Data Privacy Concern	2025-08-10 15:14:30.687823
1593	\N	\N	\N	BUG_REPORT	2025-08-10 15:14:30.68925	Users unable to register for upcoming events.	HIGH	events@ijaa.com	Event Coordinator	PENDING	Event Registration Issue	2025-08-10 15:14:30.68925
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.users (id, password, username, user_id, active) FROM stdin;
7	$2a$12$tBQKboN0E6fojUS.w/GdieuER4yecurGEttfEnSg3j10lzo6vnXBW	nusrat@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540a	t
2	$2a$12$BCx8pF0cDIWEt7gh6BnpKOFtMonFkVL0I2VPVzphcz1UkFxTv7wvm	mdsahal.info@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540e	t
6	$2a$12$s4DWWOuO0U2wWAI9Z5zjuec/8aorvGssloOUdMoor20b0LNU2EVty	tanvir@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540b	t
5	$2a$12$PivPX2JgNXltTnJeVcj9R./VMIyg2UTJHb2MqzvBtf5b9r0jXBc.q	ayesha@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540c	t
4	$2a$12$TbiMi3JvVxIooCwZLSnXje/pMyvOu0kNaRBdk2AZyMnL0dfEwIqDK	afrineva28@gmail.com	ce6ceb99-edd2-4448-b85c-a85ad187540d	t
9	$2a$12$6O/0xQyqCiuKRsWMlADwUOCmuh4OnpL7BlJeiB/nI98GGw1hIPK7e	testuser456	2d235b30-be17-4f66-8949-2c8c3065bb28	t
11	$2a$12$QqCtyrKXsxm4UPckzNE5e.F1Kg5A9HrE1WyXXoD6lWIquHjz.QZEO	testuser999	e68eff10-201a-4f4c-96d7-e5a227477720	t
12	$2a$12$Qc7RQ112XZudU.07yJdHTuEQGTPQUBeHrMAo1XGETC/0PN7frwL3m	testuser1000	4c78c4f8-2a7a-40f1-b2fd-587efb1c16b6	t
13	$2a$12$Z9No1R1va0ez3r.QyYqSs.2johjNZPGvaJzLuhlwJGdRk/miCmc8.	testuser1234	72d98c42-5ad1-4be5-9e52-a31ecb9c5d7f	t
8	$2a$12$QldFwO5Ehw352d22eEgpwOvbD93Z1Qzg7CPVO9u2y7rjBjNoUCcEm	testuser123	a52c4916-3f71-401a-98ba-9c1441b8afcf	f
15	$2a$12$CtS93Np9WqrROvPv.bTm/.Ftq7GIxNo2tbOybReR0t3pxxdr1S5me	testuser	74e90977-a050-4ef9-acc6-5d03cad34727	t
16	$2a$12$UdCxaovhbkLN/ecuD9g2wett2h9my/H4MwhM5VlGUST.g1xopVRXG	testuser2	db28f57a-019f-4667-a607-2d87000e4c52	t
17	$2a$12$Rxou9UBWEq5G0mUEEXWIteqTp3Cokf29YS5ivOlyGArYy73OnaecK	testuser3	c95bcb26-87ec-49be-8bc0-b23516f11b2d	t
18	$2a$12$aZnjGEswW65dpi03mCcHHOgk9cFqFv3IdvYO04zNPDNSJU7g0Zb8K	newuser123	87fa22ba-e390-4af9-abcd-894be751b943	t
19	$2a$12$TtxLTm6Ra10pzgBJENvmYO1lcTlaoLa6iQzw1EZwPy05TSQuyztPe	newuser456	88013526-81c3-4c7e-b464-5c684ac493f3	t
20	$2a$12$Ku7L9dJlVBH90RShsGJQ.up6FeqbTeFY8Sp90ErFjYuxTQHccENym	gatewayuser789	83cef4b6-7042-4f3d-8ab8-8c8219a744a4	t
21	$2a$12$YRCICWmJaCz1LAyE0lPl6eb49cRyCJppxpPBXMQAmITlx2EMbjveO	testuser4	012c7c13-3b50-4fa6-92db-be225ec5e29e	t
22	$2a$12$4wwlNYARqR6tqSBZO4I.N.8EYhQ/mKLDSKLZ83lGbIUSBcmU0uUGy	testuser@example.com	1b825f1a-9350-410a-bd81-dbd00544532a	t
\.


--
-- Name: admins_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.admins_id_seq', 3, true);


--
-- Name: announcements_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.announcements_id_seq', 1337, true);


--
-- Name: connections_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.connections_id_seq', 3, true);


--
-- Name: experiences_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.experiences_id_seq', 7, true);


--
-- Name: interests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.interests_id_seq', 4, true);


--
-- Name: profiles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.profiles_id_seq', 4, true);


--
-- Name: reports_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.reports_id_seq', 1593, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.users_id_seq', 22, true);


--
-- PostgreSQL database dump complete
--

