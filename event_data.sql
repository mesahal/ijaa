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
-- Data for Name: calendar_integrations; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.calendar_integrations (id, access_token, calendar_id, calendar_name, calendar_type, created_at, is_active, last_sync_error, last_sync_time, refresh_token, sync_events, sync_invitations, sync_settings, token_expiry, updated_at, username, calendar_url, sync_frequency_hours, sync_from_external, sync_recurring_events, sync_reminders, sync_to_external) FROM stdin;
\.


--
-- Data for Name: event_analytics; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_analytics (id, actual_attendees, attendance_rate, confirmed_attendees, created_at, declined_attendees, event_id, last_updated, maybe_attendees, no_shows, notes, pending_responses, response_rate, total_invitations, updated_at, average_response_time_hours, event_end_date, event_start_date, event_title, first_rsvp_time, is_completed, last_rsvp_time, organizer_username, total_comments, total_media_uploads, total_reminders, engagement_rate) FROM stdin;
165	25	85	25	2025-08-10 15:14:30.568307	3	1	2025-08-10 15:14:30.565494	5	0	\N	17	85	50	2025-08-10 15:14:30.568307	0	2025-01-01 23:00:00	2025-01-01 18:00:00	New Year Celebration 2025	2024-12-15 10:00:00	t	2024-12-30 18:00:00	john.doe	15	8	3	75
166	150	92	150	2025-08-10 15:14:30.575426	10	2	2025-08-10 15:14:30.565524	20	0	\N	20	92	200	2025-08-10 15:14:30.575426	0	2025-03-15 17:00:00	2025-03-15 09:00:00	Tech Conference 2025	2024-12-01 09:00:00	t	2025-03-10 17:00:00	jane.smith	25	12	5	88
167	60	78	60	2025-08-10 15:14:30.578472	5	3	2025-08-10 15:14:30.565534	10	0	\N	5	78	80	2025-08-10 15:14:30.578472	0	2025-02-10 16:00:00	2025-02-10 14:00:00	Online Workshop: AI Basics	2024-12-20 14:00:00	t	2025-02-08 16:00:00	mike.johnson	12	6	2	82
168	25	83	25	2025-08-10 15:14:30.581185	2	4	2025-08-10 15:14:30.565541	3	0	\N	0	83	30	2025-08-10 15:14:30.581185	0	2025-04-20 21:00:00	2025-04-20 18:00:00	Alumni Meetup - Dhaka	2025-03-01 18:00:00	t	2025-04-18 21:00:00	sarah.wilson	8	4	1	80
169	120	80	120	2025-08-10 15:14:30.584156	8	5	2025-08-10 15:14:30.565549	15	0	\N	7	80	150	2025-08-10 15:14:30.584156	0	2025-05-10 16:00:00	2025-05-10 10:00:00	Career Fair 2025	2025-01-01 10:00:00	t	2025-05-08 16:00:00	david.brown	18	10	3	85
170	80	80	80	2025-08-10 15:14:30.586603	5	6	2025-08-10 15:14:30.565555	10	0	\N	5	80	100	2025-08-10 15:14:30.586603	0	2025-06-15 23:00:00	2025-06-15 19:00:00	Alumni Reunion 2025	2025-02-01 19:00:00	f	2025-06-12 23:00:00	john.doe	15	8	2	83
171	20	80	20	2025-08-10 15:14:30.588478	2	7	2025-08-10 15:14:30.565562	3	0	\N	0	80	25	2025-08-10 15:14:30.588478	0	2025-07-05 17:00:00	2025-07-01 09:00:00	Web Development Bootcamp	2025-03-01 09:00:00	f	2025-06-28 17:00:00	jane.smith	6	4	1	85
172	45	75	45	2025-08-10 15:14:30.590453	4	8	2025-08-10 15:14:30.565569	8	0	\N	3	75	60	2025-08-10 15:14:30.590453	0	2025-08-15 22:00:00	2025-08-15 19:00:00	Startup Pitch Night	2025-04-01 19:00:00	f	2025-08-12 22:00:00	mike.johnson	10	6	2	78
173	28	80	28	2025-08-10 15:14:30.592432	2	9	2025-08-10 15:14:30.565576	4	0	\N	1	80	35	2025-08-10 15:14:30.592432	0	2025-09-20 16:00:00	2025-09-20 10:00:00	Data Science Workshop	2025-05-01 10:00:00	f	2025-09-17 16:00:00	sarah.wilson	7	4	1	82
174	65	81	65	2025-08-10 15:14:30.595613	3	10	2025-08-10 15:14:30.565582	10	0	\N	2	81	80	2025-08-10 15:14:30.595613	0	2025-10-05 18:00:00	2025-10-05 08:00:00	Alumni Sports Day	2025-06-01 08:00:00	f	2025-10-02 18:00:00	david.brown	12	8	2	84
\.


--
-- Data for Name: event_comments; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_comments (id, content, created_at, event_id, is_deleted, is_edited, likes, parent_comment_id, replies, updated_at, username) FROM stdin;
851	This is going to be an amazing celebration! 🎉	2025-08-10 15:14:30.312025	1	f	f	0	\N	0	2025-08-10 15:14:30.312025	john.doe
852	Can't wait to see the fireworks display!	2025-08-10 15:14:30.318195	1	f	f	0	\N	0	2025-08-10 15:14:30.318195	jane.smith
853	Will there be live music?	2025-08-10 15:14:30.319667	1	f	f	0	\N	0	2025-08-10 15:14:30.319667	mike.johnson
854	The venue looks perfect for this event	2025-08-10 15:14:30.320746	1	f	f	0	\N	0	2025-08-10 15:14:30.320746	sarah.wilson
855	Looking forward to meeting everyone!	2025-08-10 15:14:30.321667	1	f	f	0	\N	0	2025-08-10 15:14:30.321667	david.brown
856	Great lineup of speakers this year!	2025-08-10 15:14:30.322595	2	f	f	0	\N	0	2025-08-10 15:14:30.322595	john.doe
857	The networking sessions are always valuable	2025-08-10 15:14:30.323426	2	f	f	0	\N	0	2025-08-10 15:14:30.323426	jane.smith
858	Will there be workshops on the latest tech trends?	2025-08-10 15:14:30.324276	2	f	f	0	\N	0	2025-08-10 15:14:30.324276	mike.johnson
859	Perfect opportunity to learn from industry experts	2025-08-10 15:14:30.325217	2	f	f	0	\N	0	2025-08-10 15:14:30.325217	sarah.wilson
860	The venue is easily accessible	2025-08-10 15:14:30.326225	2	f	f	0	\N	0	2025-08-10 15:14:30.326225	david.brown
861	AI is transforming every industry!	2025-08-10 15:14:30.327285	3	f	f	0	\N	0	2025-08-10 15:14:30.327285	john.doe
862	This workshop will be perfect for beginners	2025-08-10 15:14:30.330011	3	f	f	0	\N	0	2025-08-10 15:14:30.330011	jane.smith
863	Online format makes it accessible to everyone	2025-08-10 15:14:30.332008	3	f	f	0	\N	0	2025-08-10 15:14:30.332008	mike.johnson
864	Looking forward to the hands-on exercises	2025-08-10 15:14:30.334182	3	f	f	0	\N	0	2025-08-10 15:14:30.334182	sarah.wilson
865	Will there be certificates provided?	2025-08-10 15:14:30.335833	3	f	f	0	\N	0	2025-08-10 15:14:30.335833	david.brown
866	Coffee and networking - perfect combination!	2025-08-10 15:14:30.33697	4	f	f	0	\N	0	2025-08-10 15:14:30.33697	john.doe
867	Great venue choice for casual networking	2025-08-10 15:14:30.337927	4	f	f	0	\N	0	2025-08-10 15:14:30.337927	jane.smith
868	Will there be any structured networking activities?	2025-08-10 15:14:30.338814	4	f	f	0	\N	0	2025-08-10 15:14:30.338814	mike.johnson
869	Looking forward to meeting fellow alumni	2025-08-10 15:14:30.339649	4	f	f	0	\N	0	2025-08-10 15:14:30.339649	sarah.wilson
870	Perfect timing for a weekend meetup	2025-08-10 15:14:30.340391	4	f	f	0	\N	0	2025-08-10 15:14:30.340391	david.brown
871	Great opportunity to explore new career paths	2025-08-10 15:14:30.341109	5	f	f	0	\N	0	2025-08-10 15:14:30.341109	john.doe
872	Will there be companies from the tech sector?	2025-08-10 15:14:30.342052	5	f	f	0	\N	0	2025-08-10 15:14:30.342052	jane.smith
873	Perfect timing for job seekers	2025-08-10 15:14:30.342883	5	f	f	0	\N	0	2025-08-10 15:14:30.342883	mike.johnson
874	Looking forward to the company presentations	2025-08-10 15:14:30.343626	5	f	f	0	\N	0	2025-08-10 15:14:30.343626	sarah.wilson
875	Will there be resume review sessions?	2025-08-10 15:14:30.345181	5	f	f	0	\N	0	2025-08-10 15:14:30.345181	david.brown
\.


--
-- Data for Name: event_invitations; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_invitations (id, created_at, event_id, invited_by_username, invited_username, is_read, is_responded, personal_message, updated_at) FROM stdin;
513	2025-08-10 15:14:30.420287	1	john.doe	alex.chen	f	f	You're invited to our New Year celebration!	2025-08-10 15:14:30.420287
514	2025-08-10 15:14:30.425114	1	jane.smith	emma.wilson	f	f	Join us for an amazing New Year party!	2025-08-10 15:14:30.425114
515	2025-08-10 15:14:30.428743	1	mike.johnson	robert.kim	f	f	Don't miss the New Year celebration!	2025-08-10 15:14:30.428743
516	2025-08-10 15:14:30.431514	2	john.doe	alex.chen	f	f	You're invited to our tech conference!	2025-08-10 15:14:30.431514
517	2025-08-10 15:14:30.434301	2	jane.smith	emma.wilson	f	f	Great networking opportunity at the tech conference	2025-08-10 15:14:30.434301
518	2025-08-10 15:14:30.436849	2	mike.johnson	robert.kim	f	f	Join us for the annual tech conference	2025-08-10 15:14:30.436849
519	2025-08-10 15:14:30.439517	3	john.doe	alex.chen	f	f	Learn AI basics in our online workshop!	2025-08-10 15:14:30.439517
520	2025-08-10 15:14:30.442512	3	jane.smith	emma.wilson	f	f	Perfect workshop for data science enthusiasts	2025-08-10 15:14:30.442512
521	2025-08-10 15:14:30.445262	3	mike.johnson	robert.kim	f	f	Join our AI workshop to learn new skills	2025-08-10 15:14:30.445262
522	2025-08-10 15:14:30.447698	4	john.doe	alex.chen	f	f	Join us for coffee and networking!	2025-08-10 15:14:30.447698
523	2025-08-10 15:14:30.450125	4	jane.smith	emma.wilson	f	f	Casual meetup with fellow alumni	2025-08-10 15:14:30.450125
524	2025-08-10 15:14:30.452167	4	mike.johnson	robert.kim	f	f	Great opportunity to network over coffee	2025-08-10 15:14:30.452167
525	2025-08-10 15:14:30.45384	5	john.doe	alex.chen	f	f	Explore job opportunities at our career fair!	2025-08-10 15:14:30.45384
526	2025-08-10 15:14:30.455465	5	jane.smith	emma.wilson	f	f	Great companies will be at the career fair	2025-08-10 15:14:30.455465
527	2025-08-10 15:14:30.457212	5	mike.johnson	robert.kim	f	f	Don't miss the career opportunities!	2025-08-10 15:14:30.457212
\.


--
-- Data for Name: event_media; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_media (id, caption, created_at, event_id, file_name, file_size, file_type, file_url, is_approved, likes, media_type, updated_at, uploaded_by_username) FROM stdin;
307	New Year Celebration Banner	2025-08-10 15:14:30.370437	1	new_year_banner.jpg	2048576	image/jpeg	https://example.com/media/new_year_banner.jpg	t	0	IMAGE	2025-08-10 15:14:30.370437	john.doe
308	Venue Preview	2025-08-10 15:14:30.374304	1	venue_photo.jpg	1536000	image/jpeg	https://example.com/media/venue_photo.jpg	t	0	IMAGE	2025-08-10 15:14:30.374304	jane.smith
309	Conference Logo	2025-08-10 15:14:30.375803	2	tech_conference_logo.png	512000	image/png	https://example.com/media/tech_conference_logo.png	t	0	IMAGE	2025-08-10 15:14:30.375803	mike.johnson
310	Speaker Lineup	2025-08-10 15:14:30.377797	2	speaker_lineup.pdf	1024000	application/pdf	https://example.com/media/speaker_lineup.pdf	t	0	DOCUMENT	2025-08-10 15:14:30.377797	sarah.wilson
311	Workshop Poster	2025-08-10 15:14:30.380426	3	ai_workshop_poster.jpg	3072000	image/jpeg	https://example.com/media/ai_workshop_poster.jpg	t	0	IMAGE	2025-08-10 15:14:30.380426	david.brown
312	Workshop Agenda	2025-08-10 15:14:30.382655	3	workshop_agenda.pdf	512000	application/pdf	https://example.com/media/workshop_agenda.pdf	t	0	DOCUMENT	2025-08-10 15:14:30.382655	john.doe
313	Venue Photo	2025-08-10 15:14:30.384346	4	coffee_house_photo.jpg	2048000	image/jpeg	https://example.com/media/coffee_house_photo.jpg	t	0	IMAGE	2025-08-10 15:14:30.384346	jane.smith
314	Career Fair Banner	2025-08-10 15:14:30.386188	5	career_fair_banner.jpg	4096000	image/jpeg	https://example.com/media/career_fair_banner.jpg	t	0	IMAGE	2025-08-10 15:14:30.386188	mike.johnson
315	Participating Companies	2025-08-10 15:14:30.388143	5	participating_companies.pdf	1536000	application/pdf	https://example.com/media/participating_companies.pdf	t	0	DOCUMENT	2025-08-10 15:14:30.388143	sarah.wilson
\.


--
-- Data for Name: event_participations; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_participations (id, created_at, event_id, message, participant_username, status, updated_at) FROM stdin;
855	2025-08-10 15:14:30.215942	1	Looking forward to the celebration! 🎉	john.doe	GOING	2025-08-10 15:14:30.215942
856	2025-08-10 15:14:30.22198	1	Can't wait to see everyone!	jane.smith	GOING	2025-08-10 15:14:30.22198
857	2025-08-10 15:14:30.22456	1	I'll try to make it	mike.johnson	MAYBE	2025-08-10 15:14:30.22456
858	2025-08-10 15:14:30.226839	1	Excited for the new year!	sarah.wilson	GOING	2025-08-10 15:14:30.226839
859	2025-08-10 15:14:30.230366	1	Sorry, I have other plans	david.brown	NOT_GOING	2025-08-10 15:14:30.230366
860	2025-08-10 15:14:30.233295	2	Great opportunity for networking	john.doe	GOING	2025-08-10 15:14:30.233295
861	2025-08-10 15:14:30.236101	2	Looking forward to the tech talks	jane.smith	GOING	2025-08-10 15:14:30.236101
862	2025-08-10 15:14:30.238423	2	Will be there for sure	mike.johnson	GOING	2025-08-10 15:14:30.238423
863	2025-08-10 15:14:30.241475	2	Depends on my schedule	sarah.wilson	MAYBE	2025-08-10 15:14:30.241475
864	2025-08-10 15:14:30.243222	2	Tech conferences are always valuable	david.brown	GOING	2025-08-10 15:14:30.243222
865	2025-08-10 15:14:30.246522	3	AI is the future!	john.doe	GOING	2025-08-10 15:14:30.246522
866	2025-08-10 15:14:30.249267	3	Perfect for my data science career	jane.smith	GOING	2025-08-10 15:14:30.249267
867	2025-08-10 15:14:30.251446	3	Online workshops are convenient	mike.johnson	GOING	2025-08-10 15:14:30.251446
868	2025-08-10 15:14:30.253584	3	I'll join if I'm free	sarah.wilson	MAYBE	2025-08-10 15:14:30.253584
869	2025-08-10 15:14:30.255317	3	Not my area of interest	david.brown	NOT_GOING	2025-08-10 15:14:30.255317
870	2025-08-10 15:14:30.256863	4	Love meeting fellow alumni	john.doe	GOING	2025-08-10 15:14:30.256863
871	2025-08-10 15:14:30.258305	4	Coffee and networking sounds perfect	jane.smith	GOING	2025-08-10 15:14:30.258305
872	2025-08-10 15:14:30.259999	4	I'll try to join	mike.johnson	MAYBE	2025-08-10 15:14:30.259999
873	2025-08-10 15:14:30.262572	4	Looking forward to it!	sarah.wilson	GOING	2025-08-10 15:14:30.262572
874	2025-08-10 15:14:30.264646	4	Always good to network	david.brown	GOING	2025-08-10 15:14:30.264646
875	2025-08-10 15:14:30.26686	5	Great opportunity to explore new career paths	john.doe	GOING	2025-08-10 15:14:30.26686
876	2025-08-10 15:14:30.268794	5	Will there be companies from the tech sector?	jane.smith	GOING	2025-08-10 15:14:30.268794
877	2025-08-10 15:14:30.270774	5	Perfect timing for job seekers	mike.johnson	GOING	2025-08-10 15:14:30.270774
878	2025-08-10 15:14:30.272982	5	Looking forward to the company presentations	sarah.wilson	GOING	2025-08-10 15:14:30.272982
879	2025-08-10 15:14:30.274711	5	Will there be resume review sessions?	david.brown	GOING	2025-08-10 15:14:30.274711
\.


--
-- Data for Name: event_reminders; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_reminders (id, channel, created_at, custom_message, event_id, is_active, is_sent, reminder_time, reminder_type, updated_at, username) FROM stdin;
\.


--
-- Data for Name: event_templates; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.event_templates (id, created_at, created_by_username, default_duration_minutes, default_end_time, default_start_time, description, event_type, invite_message, is_online, is_public, is_recurring, location, max_participants, meeting_link, notes, organizer_email, organizer_name, privacy, recurrence_days, recurrence_interval, recurrence_type, tags, template_name, title, updated_at, usage_count, category, default_recurrence_days, default_recurrence_interval, default_recurrence_type, is_active, name, supports_recurrence, total_ratings, average_rating) FROM stdin;
210	2025-08-10 15:14:30.481045	john.doe	480	17:00:00	09:00:00	Annual technology conference with industry experts	CONFERENCE	Join us for the latest in technology!	f	t	f	Conference Center	500	\N	\N	tech@ijaa.com	Tech Lead	PUBLIC	\N	\N	\N	tech,conference,networking	Tech Conference Template	Annual Tech Conference	2025-08-10 15:14:30.481045	0	CONFERENCE	\N	1	\N	t	Tech Conference Template	f	0	0
211	2025-08-10 15:14:30.488091	jane.smith	180	13:00:00	10:00:00	Hands-on workshop for skill development	WORKSHOP	Learn new skills in this interactive workshop!	f	t	f	Training Room	50	\N	\N	workshop@ijaa.com	Workshop Lead	PUBLIC	\N	\N	\N	workshop,learning,skills	Workshop Template	Interactive Workshop	2025-08-10 15:14:30.488091	0	WORKSHOP	\N	1	\N	t	Workshop Template	f	0	0
212	2025-08-10 15:14:30.490059	mike.johnson	120	20:00:00	18:00:00	Casual networking meetup for alumni	MEETUP	Connect with fellow alumni!	f	t	f	Coffee Shop	30	\N	\N	community@ijaa.com	Community Manager	PUBLIC	\N	\N	\N	networking,alumni,coffee	Networking Meetup	Alumni Networking	2025-08-10 15:14:30.490059	0	NETWORKING	\N	1	\N	t	Networking Meetup	f	0	0
213	2025-08-10 15:14:30.49175	sarah.wilson	360	16:00:00	10:00:00	Job fair with multiple companies	CAREER_FAIR	Explore job opportunities!	f	t	f	University Hall	200	\N	\N	career@ijaa.com	Career Services	PUBLIC	\N	\N	\N	career,jobs,fair	Career Fair Template	Career Fair	2025-08-10 15:14:30.49175	0	CAREER	\N	1	\N	t	Career Fair Template	f	0	0
214	2025-08-10 15:14:30.493749	david.brown	60	15:00:00	14:00:00	Virtual webinar with remote participation	WEBINAR	Join our online webinar!	t	t	f	Virtual	100	https://meet.google.com/	\N	webinar@ijaa.com	Webinar Host	PUBLIC	\N	\N	\N	webinar,online,virtual	Online Webinar	Online Webinar	2025-08-10 15:14:30.493749	0	WEBINAR	\N	1	\N	t	Online Webinar	f	0	0
215	2025-08-10 15:14:30.496508	john.doe	120	18:00:00	16:00:00	Recurring study group for students	STUDY_GROUP	Join our weekly study sessions!	f	f	f	Library	20	\N	\N	study@ijaa.com	Study Coordinator	PRIVATE	\N	\N	\N	study,academic,weekly	Weekly Study Group	Weekly Study Group	2025-08-10 15:14:30.496508	0	ACADEMIC	MONDAY,WEDNESDAY,FRIDAY	1	WEEKLY	t	Weekly Study Group	t	0	0
216	2025-08-10 15:14:30.498561	jane.smith	180	22:00:00	19:00:00	Monthly dinner for alumni networking	DINNER	Join us for dinner and networking!	f	f	f	Restaurant	40	\N	\N	dinner@ijaa.com	Dinner Coordinator	PRIVATE	\N	\N	\N	dinner,alumni,monthly	Monthly Alumni Dinner	Monthly Alumni Dinner	2025-08-10 15:14:30.498561	0	SOCIAL	\N	1	MONTHLY	t	Monthly Alumni Dinner	t	0	0
217	2025-08-10 15:14:30.500195	mike.johnson	240	22:00:00	18:00:00	Annual reunion with dinner and entertainment	REUNION	Annual alumni reunion celebration!	f	t	f	Hotel Ballroom	150	\N	\N	reunion@ijaa.com	Reunion Coordinator	PUBLIC	\N	\N	\N	reunion,annual,celebration	Annual Reunion	Annual Alumni Reunion	2025-08-10 15:14:30.500195	0	REUNION	\N	1	\N	t	Annual Reunion	f	0	0
218	2025-08-10 15:14:30.501366	sarah.wilson	600	18:00:00	08:00:00	Annual sports day with various activities	SPORTS	Join us for a day of sports and fun!	f	t	f	Sports Complex	100	\N	\N	sports@ijaa.com	Sports Coordinator	PUBLIC	\N	\N	\N	sports,activities,fun	Sports Day	Alumni Sports Day	2025-08-10 15:14:30.501366	0	SPORTS	\N	1	\N	t	Sports Day	f	0	0
219	2025-08-10 15:14:30.502647	david.brown	60	16:00:00	15:00:00	One-on-one mentorship sessions	MENTORSHIP	Connect with mentors for career guidance!	f	f	f	Meeting Room	10	\N	\N	mentorship@ijaa.com	Mentorship Coordinator	PRIVATE	\N	\N	\N	mentorship,career,guidance	Mentorship Program	Mentorship Session	2025-08-10 15:14:30.502647	0	MENTORSHIP	\N	1	\N	t	Mentorship Program	f	0	0
\.


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.events (id, active, created_at, current_participants, description, end_date, event_type, is_online, location, max_participants, meeting_link, organizer, start_date, title, updated_at, organizer_email, organizer_name, created_by_username, invite_message, privacy) FROM stdin;
1172	t	2025-08-10 15:14:30.138397	0	Celebrate the new year with fireworks, music, and networking opportunities	2025-01-01 23:00:00	CELEBRATION	f	Dhaka Club	200	\N	\N	2025-01-01 18:00:00	New Year Celebration 2025	2025-08-10 15:14:30.138397	admin@ijaa.com	Admin User	john.doe	You're invited to join us at this exciting event!	PUBLIC
1173	t	2025-08-10 15:14:30.147776	0	Annual technology conference with industry experts and networking sessions	2025-03-15 17:00:00	CONFERENCE	f	Bangabandhu International Conference Center	500	\N	\N	2025-03-15 09:00:00	Tech Conference 2025	2025-08-10 15:14:30.147776	events@ijaa.com	Event Manager	jane.smith	You're invited to join us at this exciting event!	PUBLIC
1174	t	2025-08-10 15:14:30.150965	0	Learn the fundamentals of artificial intelligence and machine learning	2025-02-10 16:00:00	WORKSHOP	t	Virtual	100	https://meet.google.com/abc-defg-hij	\N	2025-02-10 14:00:00	Online Workshop: AI Basics	2025-08-10 15:14:30.150965	tech@ijaa.com	Tech Lead	mike.johnson	You're invited to join us at this exciting event!	PUBLIC
1175	t	2025-08-10 15:14:30.153876	0	Connect with fellow alumni over coffee and networking in Dhaka	2025-04-20 21:00:00	MEETUP	f	Coffee House, Gulshan	50	\N	\N	2025-04-20 18:00:00	Alumni Meetup - Dhaka	2025-08-10 15:14:30.153876	community@ijaa.com	Community Manager	sarah.wilson	You're invited to join us at this exciting event!	PUBLIC
1176	t	2025-08-10 15:14:30.157163	0	Explore job opportunities with leading companies and startups	2025-05-10 16:00:00	CAREER_FAIR	f	University Campus	300	\N	\N	2025-05-10 10:00:00	Career Fair 2025	2025-08-10 15:14:30.157163	career@ijaa.com	Career Services	david.brown	You're invited to join us at this exciting event!	PUBLIC
1177	t	2025-08-10 15:14:30.161086	0	Annual alumni reunion with dinner and entertainment	2025-06-15 23:00:00	REUNION	f	Radisson Blu Hotel	150	\N	\N	2025-06-15 19:00:00	Alumni Reunion 2025	2025-08-10 15:14:30.161086	reunion@ijaa.com	Reunion Coordinator	john.doe	You're invited to join us at this exciting event!	PUBLIC
1178	t	2025-08-10 15:14:30.165203	0	Intensive web development bootcamp for beginners	2025-07-05 17:00:00	BOOTCAMP	f	Tech Hub Dhaka	30	\N	\N	2025-07-01 09:00:00	Web Development Bootcamp	2025-08-10 15:14:30.165203	education@ijaa.com	Education Team	jane.smith	You're invited to join us at this exciting event!	PUBLIC
1179	t	2025-08-10 15:14:30.168787	0	Watch innovative startups pitch their ideas to investors	2025-08-15 22:00:00	PITCH_NIGHT	f	Innovation Hub	80	\N	\N	2025-08-15 19:00:00	Startup Pitch Night	2025-08-10 15:14:30.168787	startup@ijaa.com	Startup Coordinator	mike.johnson	You're invited to join us at this exciting event!	PUBLIC
1180	t	2025-08-10 15:14:30.171219	0	Hands-on workshop on data science and machine learning	2025-09-20 16:00:00	WORKSHOP	f	Tech Institute	40	\N	\N	2025-09-20 10:00:00	Data Science Workshop	2025-08-10 15:14:30.171219	data@ijaa.com	Data Science Lead	sarah.wilson	You're invited to join us at this exciting event!	PUBLIC
1181	t	2025-08-10 15:14:30.173406	0	Annual sports day with various activities and competitions	2025-10-05 18:00:00	SPORTS	f	University Sports Complex	120	\N	\N	2025-10-05 08:00:00	Alumni Sports Day	2025-08-10 15:14:30.173406	sports@ijaa.com	Sports Coordinator	david.brown	You're invited to join us at this exciting event!	PUBLIC
\.


--
-- Data for Name: recurring_events; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.recurring_events (id, active, created_at, created_by_username, current_participants, description, end_date, event_type, invite_message, is_online, is_series, is_template, location, max_participants, meeting_link, organizer_email, organizer_name, parent_event_id, privacy, recurrence_days, recurrence_end_date, recurrence_interval, recurrence_type, start_date, title, updated_at, generate_instances, max_occurrences) FROM stdin;
101	t	2025-08-10 15:14:30.525507	john.doe	0	Weekly study sessions for students	2025-01-06 18:00:00	STUDY_GROUP	Join our recurring event!	f	f	f	Library	20	\N	study@ijaa.com	Study Coordinator	\N	PUBLIC	MONDAY,WEDNESDAY,FRIDAY	2025-12-31 18:00:00	1	WEEKLY	2025-01-06 16:00:00	Weekly Study Group	2025-08-10 15:14:30.525507	t	52
102	t	2025-08-10 15:14:30.53341	jane.smith	0	Monthly dinner for alumni networking	2025-01-15 22:00:00	DINNER	Join our recurring event!	f	f	f	Restaurant	40	\N	dinner@ijaa.com	Dinner Coordinator	\N	PUBLIC	\N	2025-12-31 22:00:00	1	MONTHLY	2025-01-15 19:00:00	Monthly Alumni Dinner	2025-08-10 15:14:30.53341	t	12
103	t	2025-08-10 15:14:30.535829	mike.johnson	0	Bi-weekly technology discussions	2025-01-08 20:00:00	MEETUP	Join our recurring event!	f	f	f	Tech Hub	30	\N	tech@ijaa.com	Tech Lead	\N	PUBLIC	WEDNESDAY	2025-12-31 20:00:00	2	WEEKLY	2025-01-08 18:00:00	Bi-weekly Tech Meetup	2025-08-10 15:14:30.535829	t	26
104	t	2025-08-10 15:14:30.537935	sarah.wilson	0	Quarterly board meetings for alumni association	2025-01-20 16:00:00	MEETING	Join our recurring event!	f	f	f	Conference Room	15	\N	board@ijaa.com	Board Secretary	\N	PUBLIC	\N	2025-12-31 16:00:00	3	MONTHLY	2025-01-20 14:00:00	Quarterly Board Meeting	2025-08-10 15:14:30.537935	t	4
105	t	2025-08-10 15:14:30.539394	david.brown	0	Daily morning exercise sessions	2025-01-01 07:00:00	EXERCISE	Join our recurring event!	f	f	f	Sports Complex	50	\N	fitness@ijaa.com	Fitness Instructor	\N	PUBLIC	MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY	2025-12-31 07:00:00	1	DAILY	2025-01-01 06:00:00	Daily Morning Exercise	2025-08-10 15:14:30.539394	t	260
\.


--
-- Name: calendar_integrations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.calendar_integrations_id_seq', 1, false);


--
-- Name: event_analytics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_analytics_id_seq', 174, true);


--
-- Name: event_comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_comments_id_seq', 875, true);


--
-- Name: event_invitations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_invitations_id_seq', 527, true);


--
-- Name: event_media_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_media_id_seq', 315, true);


--
-- Name: event_participations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_participations_id_seq', 879, true);


--
-- Name: event_reminders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_reminders_id_seq', 1, false);


--
-- Name: event_templates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.event_templates_id_seq', 219, true);


--
-- Name: events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.events_id_seq', 1181, true);


--
-- Name: recurring_events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.recurring_events_id_seq', 105, true);


--
-- PostgreSQL database dump complete
--

