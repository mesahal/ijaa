package com.ijaa.user.common.config;

import com.ijaa.user.domain.entity.*;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after AdminDataSeeder
public class TestDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;
    private final InterestRepository interestRepository;
    private final AlumniSearchRepository alumniSearchRepository;
    private final ConnectionRepository connectionRepository;
    private final FeatureFlagRepository featureFlagRepository;
    private final EventRepository eventRepository;
    private final AnnouncementRepository announcementRepository;
    private final ReportRepository reportRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting to seed test data...");
        
        // Always seed admin management data
        seedFeatureFlags();
        seedEvents();
        seedAnnouncements();
        seedReports();
        
        // Only seed user data if no users exist
        if (userRepository.count() == 0) {
            seedUsers();
            seedProfiles();
            seedExperiences();
            seedInterests();
            seedAlumniSearch();
            seedConnections();
        } else {
            log.info("Users already exist, skipping user data seeding.");
        }
        
        log.info("Test data seeding completed successfully!");
    }

    private void seedUsers() {
        log.info("Seeding users...");
        
        List<User> users = Arrays.asList(
            createUser("USER_ABC123XYZ", "john.doe", "password123"),
            createUser("USER_DEF456UVW", "jane.smith", "password123"),
            createUser("USER_GHI789RST", "mike.johnson", "password123"),
            createUser("USER_JKL012MNO", "sarah.wilson", "password123"),
            createUser("USER_PQR345STU", "david.brown", "password123")
        );
        
        userRepository.saveAll(users);
        log.info("Users seeded successfully!");
    }

    private User createUser(String userId, String username, String password) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        return user;
    }

    private void seedProfiles() {
        log.info("Seeding profiles...");
        
        List<Profile> profiles = Arrays.asList(
            createProfile("john.doe", "USER_ABC123XYZ", "John Doe", "Software Engineer", "Dhaka, Bangladesh", "Passionate software engineer with 5+ years of experience", "+8801712345678", "linkedin.com/in/johndoe", "johndoe.com", "2018", "john.doe@email.com", "facebook.com/johndoe"),
            createProfile("jane.smith", "USER_DEF456UVW", "Jane Smith", "Data Scientist", "Chittagong, Bangladesh", "Data scientist specializing in machine learning and AI", "+8801812345678", "linkedin.com/in/janesmith", "janesmith.com", "2019", "jane.smith@email.com", "facebook.com/janesmith"),
            createProfile("mike.johnson", "USER_GHI789RST", "Mike Johnson", "Product Manager", "Sylhet, Bangladesh", "Product manager with expertise in agile methodologies", "+8801912345678", "linkedin.com/in/mikejohnson", "mikejohnson.com", "2017", "mike.johnson@email.com", "facebook.com/mikejohnson"),
            createProfile("sarah.wilson", "USER_JKL012MNO", "Sarah Wilson", "UX Designer", "Rajshahi, Bangladesh", "Creative UX designer focused on user-centered design", "+8802012345678", "linkedin.com/in/sarahwilson", "sarahwilson.com", "2020", "sarah.wilson@email.com", "facebook.com/sarahwilson"),
            createProfile("david.brown", "USER_PQR345STU", "David Brown", "DevOps Engineer", "Khulna, Bangladesh", "DevOps engineer with cloud infrastructure expertise", "+8802112345678", "linkedin.com/in/davidbrown", "davidbrown.com", "2016", "david.brown@email.com", "facebook.com/davidbrown")
        );
        
        profileRepository.saveAll(profiles);
        log.info("Profiles seeded successfully!");
    }

    private Profile createProfile(String username, String userId, String name, String profession, String location, String bio, String phone, String linkedIn, String website, String batch, String email, String facebook) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setUserId(userId);
        profile.setName(name);
        profile.setProfession(profession);
        profile.setLocation(location);
        profile.setBio(bio);
        profile.setPhone(phone);
        profile.setLinkedIn(linkedIn);
        profile.setWebsite(website);
        profile.setBatch(batch);
        profile.setEmail(email);
        profile.setFacebook(facebook);
        profile.setShowPhone(true);
        profile.setShowLinkedIn(true);
        profile.setShowWebsite(true);
        profile.setShowEmail(true);
        profile.setShowFacebook(true);
        profile.setConnections(0);
        return profile;
    }

    private void seedExperiences() {
        log.info("Seeding experiences...");
        
        List<Experience> experiences = Arrays.asList(
            createExperience("john.doe", "USER_ABC123XYZ", "Senior Software Engineer", "TechCorp", "2020-2023", "Led development of microservices architecture"),
            createExperience("jane.smith", "USER_DEF456UVW", "Data Scientist", "DataTech", "2021-2023", "Developed ML models for predictive analytics"),
            createExperience("mike.johnson", "USER_GHI789RST", "Product Manager", "ProductHub", "2019-2023", "Managed product roadmap and strategy"),
            createExperience("sarah.wilson", "USER_JKL012MNO", "UX Designer", "DesignStudio", "2020-2023", "Created user-centered design solutions"),
            createExperience("david.brown", "USER_PQR345STU", "DevOps Engineer", "CloudTech", "2018-2023", "Implemented CI/CD pipelines and cloud infrastructure")
        );
        
        experienceRepository.saveAll(experiences);
        log.info("Experiences seeded successfully!");
    }

    private Experience createExperience(String username, String userId, String title, String company, String period, String description) {
        Experience experience = new Experience();
        experience.setUsername(username);
        experience.setUserId(userId);
        experience.setTitle(title);
        experience.setCompany(company);
        experience.setPeriod(period);
        experience.setDescription(description);
        return experience;
    }

    private void seedInterests() {
        log.info("Seeding interests...");
        
        List<Interest> interests = Arrays.asList(
            createInterest("john.doe", "USER_ABC123XYZ", "Java Development"),
            createInterest("john.doe", "USER_ABC123XYZ", "Spring Boot"),
            createInterest("jane.smith", "USER_DEF456UVW", "Machine Learning"),
            createInterest("jane.smith", "USER_DEF456UVW", "Python"),
            createInterest("mike.johnson", "USER_GHI789RST", "Product Strategy"),
            createInterest("sarah.wilson", "USER_JKL012MNO", "UI/UX Design"),
            createInterest("david.brown", "USER_PQR345STU", "Docker"),
            createInterest("david.brown", "USER_PQR345STU", "Kubernetes")
        );
        
        interestRepository.saveAll(interests);
        log.info("Interests seeded successfully!");
    }

    private Interest createInterest(String username, String userId, String interest) {
        Interest interestObj = new Interest();
        interestObj.setUsername(username);
        interestObj.setUserId(userId);
        interestObj.setInterest(interest);
        return interestObj;
    }

    private void seedAlumniSearch() {
        log.info("Seeding alumni search data...");
        
        List<AlumniSearch> alumniSearches = Arrays.asList(
            createAlumniSearch("john.doe", "John Doe", "2018", "Computer Science", "Software Engineer", "TechCorp", "Dhaka, Bangladesh", "avatar1.jpg", "Passionate software engineer with 5+ years of experience", 15, Arrays.asList("Java", "Spring Boot", "Microservices"), true),
            createAlumniSearch("jane.smith", "Jane Smith", "2019", "Data Science", "Data Scientist", "DataTech", "Chittagong, Bangladesh", "avatar2.jpg", "Data scientist specializing in machine learning and AI", 12, Arrays.asList("Python", "Machine Learning", "AI"), true),
            createAlumniSearch("mike.johnson", "Mike Johnson", "2017", "Business Administration", "Product Manager", "ProductHub", "Sylhet, Bangladesh", "avatar3.jpg", "Product manager with expertise in agile methodologies", 8, Arrays.asList("Product Management", "Agile", "Strategy"), true),
            createAlumniSearch("sarah.wilson", "Sarah Wilson", "2020", "Design", "UX Designer", "DesignStudio", "Rajshahi, Bangladesh", "avatar4.jpg", "Creative UX designer focused on user-centered design", 6, Arrays.asList("UX Design", "UI Design", "Prototyping"), true),
            createAlumniSearch("david.brown", "David Brown", "2016", "Computer Science", "DevOps Engineer", "CloudTech", "Khulna, Bangladesh", "avatar5.jpg", "DevOps engineer with cloud infrastructure expertise", 20, Arrays.asList("Docker", "Kubernetes", "AWS"), true)
        );
        
        alumniSearchRepository.saveAll(alumniSearches);
        log.info("Alumni search data seeded successfully!");
    }

    private AlumniSearch createAlumniSearch(String username, String name, String batch, String department, String profession, String company, String location, String avatar, String bio, Integer connections, List<String> skills, Boolean isVisible) {
        AlumniSearch alumniSearch = new AlumniSearch();
        alumniSearch.setUsername(username);
        alumniSearch.setName(name);
        alumniSearch.setBatch(batch);
        alumniSearch.setDepartment(department);
        alumniSearch.setProfession(profession);
        alumniSearch.setCompany(company);
        alumniSearch.setLocation(location);
        alumniSearch.setAvatar(avatar);
        alumniSearch.setBio(bio);
        alumniSearch.setConnections(connections);
        alumniSearch.setSkills(skills);
        alumniSearch.setIsVisible(isVisible);
        return alumniSearch;
    }

    private void seedConnections() {
        log.info("Seeding connections...");
        
        List<Connection> connections = Arrays.asList(
            createConnection("john.doe", "jane.smith", "ACCEPTED"),
            createConnection("john.doe", "mike.johnson", "PENDING"),
            createConnection("jane.smith", "sarah.wilson", "ACCEPTED"),
            createConnection("mike.johnson", "david.brown", "REJECTED"),
            createConnection("sarah.wilson", "john.doe", "PENDING")
        );
        
        connectionRepository.saveAll(connections);
        log.info("Connections seeded successfully!");
    }

    private Connection createConnection(String requesterUsername, String receiverUsername, String status) {
        Connection connection = new Connection();
        connection.setRequesterUsername(requesterUsername);
        connection.setReceiverUsername(receiverUsername);
        connection.setStatus(Connection.ConnectionStatus.valueOf(status));
        return connection;
    }

    private void seedFeatureFlags() {
        log.info("Seeding feature flags...");
        
        // Clear existing feature flags to avoid duplicates
        featureFlagRepository.deleteAll();
        
        List<FeatureFlag> featureFlags = Arrays.asList(
            createFeatureFlag("NEW_UI", true, "Enable new user interface with modern design"),
            createFeatureFlag("CHAT_FEATURE", false, "Enable real-time chat functionality between alumni"),
            createFeatureFlag("EVENT_REGISTRATION", true, "Enable event registration system for alumni events"),
            createFeatureFlag("PAYMENT_INTEGRATION", false, "Enable payment processing for event registrations"),
            createFeatureFlag("SOCIAL_LOGIN", true, "Enable social media login options (Google, Facebook)"),
            createFeatureFlag("DARK_MODE", true, "Enable dark mode theme for better user experience"),
            createFeatureFlag("NOTIFICATIONS", true, "Enable push notifications for important updates"),
            createFeatureFlag("ADVANCED_SEARCH", false, "Enable advanced search with filters and sorting"),
            createFeatureFlag("ALUMNI_DIRECTORY", true, "Enable public alumni directory feature"),
            createFeatureFlag("MENTORSHIP_PROGRAM", false, "Enable mentorship program matching")
        );
        
        featureFlagRepository.saveAll(featureFlags);
        log.info("Feature flags seeded successfully!");
    }

    private FeatureFlag createFeatureFlag(String featureName, Boolean enabled, String description) {
        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setFeatureName(featureName);
        featureFlag.setEnabled(enabled);
        featureFlag.setDescription(description);
        return featureFlag;
    }

    private void seedEvents() {
        log.info("Seeding events...");
        
        // Clear existing events to avoid duplicates
        eventRepository.deleteAll();
        
        List<Event> events = Arrays.asList(
            createEvent("New Year Celebration 2025", "Celebrate the new year with fireworks, music, and networking opportunities", LocalDateTime.of(2025, 1, 1, 18, 0), LocalDateTime.of(2025, 1, 1, 23, 0), "Dhaka Club", "CELEBRATION", false, null, 200, "Admin User", "admin@ijaa.com"),
            createEvent("Tech Conference 2025", "Annual technology conference with industry experts and networking sessions", LocalDateTime.of(2025, 3, 15, 9, 0), LocalDateTime.of(2025, 3, 15, 17, 0), "Bangabandhu International Conference Center", "CONFERENCE", false, null, 500, "Event Manager", "events@ijaa.com"),
            createEvent("Online Workshop: AI Basics", "Learn the fundamentals of artificial intelligence and machine learning", LocalDateTime.of(2025, 2, 10, 14, 0), LocalDateTime.of(2025, 2, 10, 16, 0), "Virtual", "WORKSHOP", true, "https://meet.google.com/abc-defg-hij", 100, "Tech Lead", "tech@ijaa.com"),
            createEvent("Alumni Meetup - Dhaka", "Connect with fellow alumni over coffee and networking in Dhaka", LocalDateTime.of(2025, 4, 20, 18, 0), LocalDateTime.of(2025, 4, 20, 21, 0), "Coffee House, Gulshan", "MEETUP", false, null, 50, "Community Manager", "community@ijaa.com"),
            createEvent("Career Fair 2025", "Explore job opportunities with leading companies and startups", LocalDateTime.of(2025, 5, 10, 10, 0), LocalDateTime.of(2025, 5, 10, 16, 0), "University Campus", "CAREER_FAIR", false, null, 300, "Career Services", "career@ijaa.com"),
            createEvent("Alumni Reunion 2025", "Annual alumni reunion with dinner and entertainment", LocalDateTime.of(2025, 6, 15, 19, 0), LocalDateTime.of(2025, 6, 15, 23, 0), "Radisson Blu Hotel", "REUNION", false, null, 150, "Reunion Coordinator", "reunion@ijaa.com"),
            createEvent("Web Development Bootcamp", "Intensive web development bootcamp for beginners", LocalDateTime.of(2025, 7, 1, 9, 0), LocalDateTime.of(2025, 7, 5, 17, 0), "Tech Hub Dhaka", "BOOTCAMP", false, null, 30, "Education Team", "education@ijaa.com")
        );
        
        eventRepository.saveAll(events);
        log.info("Events seeded successfully!");
    }

    private Event createEvent(String title, String description, LocalDateTime startDate, LocalDateTime endDate, String location, String eventType, Boolean isOnline, String meetingLink, Integer maxParticipants, String organizerName, String organizerEmail) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setLocation(location);
        event.setEventType(eventType);
        event.setActive(true);
        event.setIsOnline(isOnline);
        event.setMeetingLink(meetingLink);
        event.setMaxParticipants(maxParticipants);
        event.setCurrentParticipants(0);
        event.setOrganizerName(organizerName);
        event.setOrganizerEmail(organizerEmail);
        return event;
    }

    private void seedAnnouncements() {
        log.info("Seeding announcements...");
        
        // Clear existing announcements to avoid duplicates
        announcementRepository.deleteAll();
        
        List<Announcement> announcements = Arrays.asList(
            createAnnouncement("Welcome Back!", "We are excited to welcome you back to our platform. Stay tuned for new features and updates.", "GENERAL", false, "Admin User", "admin@ijaa.com", null),
            createAnnouncement("Upcoming Event: Alumni Meetup", "Join us for an evening of networking and fun at the Alumni Meetup. Date: April 20, 2025. Location: Coffee House, Gulshan.", "EVENT", false, "Event Manager", "events@ijaa.com", null),
            createAnnouncement("Important Update: New User Interface", "We are excited to announce the launch of our new user interface. Please check it out and provide feedback.", "NEWS", true, "System Admin", "system@ijaa.com", "https://example.com/ui-update.jpg"),
            createAnnouncement("Urgent: System Maintenance", "Scheduled maintenance will occur on Sunday from 2-4 AM. The platform will be temporarily unavailable.", "URGENT", true, "System Admin", "system@ijaa.com", null),
            createAnnouncement("Career Fair Registration Open", "Registration for the annual career fair is now open. Limited spots available!", "EVENT", false, "Career Manager", "career@ijaa.com", "https://example.com/career-fair.jpg"),
            createAnnouncement("New Feature: Alumni Directory", "We've launched a new alumni directory feature. Connect with fellow alumni easily!", "NEWS", false, "Product Team", "product@ijaa.com", null),
            createAnnouncement("Alumni Success Story: John Doe", "Congratulations to John Doe (Class of 2018) for being promoted to Senior Software Engineer at Google!", "SUCCESS", false, "Community Manager", "community@ijaa.com", null),
            createAnnouncement("Holiday Schedule", "The platform will have limited support during the upcoming holidays. Regular support will resume on January 2nd.", "GENERAL", false, "Support Team", "support@ijaa.com", null),
            createAnnouncement("Mentorship Program Launch", "We're launching a new mentorship program. Senior alumni can mentor junior alumni. Sign up now!", "PROGRAM", true, "Program Manager", "programs@ijaa.com", "https://example.com/mentorship.jpg"),
            createAnnouncement("Alumni Survey", "Please take a moment to complete our annual alumni survey. Your feedback helps us improve our services.", "SURVEY", false, "Research Team", "research@ijaa.com", null)
        );
        
        announcementRepository.saveAll(announcements);
        log.info("Announcements seeded successfully!");
    }

    private Announcement createAnnouncement(String title, String content, String category, Boolean isUrgent, String authorName, String authorEmail, String imageUrl) {
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setCategory(category);
        announcement.setActive(true);
        announcement.setIsUrgent(isUrgent);
        announcement.setAuthorName(authorName);
        announcement.setAuthorEmail(authorEmail);
        announcement.setImageUrl(imageUrl);
        announcement.setViewCount(0);
        return announcement;
    }

    private void seedReports() {
        log.info("Seeding reports...");
        
        // Clear existing reports to avoid duplicates
        reportRepository.deleteAll();
        
        List<Report> reports = Arrays.asList(
            createReport("User Behavior Report", "User jane.smith posted offensive content on the platform.", "USER_REPORT", "PENDING", "MEDIUM", "John Doe", "john.doe@email.com", null, null),
            createReport("Content Moderation", "User sarah.wilson posted spam on the platform.", "CONTENT_REPORT", "IN_PROGRESS", "HIGH", "Mike Johnson", "mike.johnson@email.com", "Admin User", "Investigating spam content"),
            createReport("Bug Report: Login Issue", "Users experiencing login issues on mobile devices.", "BUG_REPORT", "PENDING", "URGENT", "David Brown", "david.brown@email.com", null, null),
            createReport("Feature Request: Dark Mode", "Add dark mode option for better user experience.", "FEATURE_REQUEST", "RESOLVED", "LOW", "Sarah Wilson", "sarah.wilson@email.com", "Admin User", "Dark mode will be implemented in next release"),
            createReport("Profile Information", "User mike.johnson provided incorrect information on their profile.", "USER_REPORT", "CLOSED", "MEDIUM", "David Brown", "david.brown@email.com", "Admin User", "Profile information corrected"),
            createReport("System Performance Issue", "Users reporting slow loading times during peak hours.", "SYSTEM_REPORT", "IN_PROGRESS", "HIGH", "System Monitor", "system@ijaa.com", "Tech Lead", "Investigating server performance"),
            createReport("Feature Request: Video Calls", "Add video calling feature for alumni networking.", "FEATURE_REQUEST", "PENDING", "MEDIUM", "Alex Chen", "alex.chen@email.com", null, null),
            createReport("Spam Account", "Multiple fake accounts created with similar patterns.", "SECURITY_REPORT", "RESOLVED", "HIGH", "Security Team", "security@ijaa.com", "Admin User", "Fake accounts removed and IP blocked"),
            createReport("Mobile App Bug", "App crashes when accessing profile page on iOS devices.", "BUG_REPORT", "PENDING", "URGENT", "Mobile User", "mobile@ijaa.com", null, null),
            createReport("Accessibility Request", "Improve accessibility features for visually impaired users.", "FEATURE_REQUEST", "IN_PROGRESS", "MEDIUM", "Accessibility Advocate", "accessibility@ijaa.com", "UX Team", "Working on screen reader support"),
            createReport("Data Privacy Concern", "User concerned about data sharing practices.", "PRIVACY_REPORT", "RESOLVED", "LOW", "Privacy User", "privacy@ijaa.com", "Legal Team", "Privacy policy updated and communicated"),
            createReport("Event Registration Issue", "Users unable to register for upcoming events.", "BUG_REPORT", "PENDING", "HIGH", "Event Coordinator", "events@ijaa.com", null, null)
        );
        
        reportRepository.saveAll(reports);
        log.info("Reports seeded successfully!");
    }

    private Report createReport(String title, String description, String category, String status, String priority, String reporterName, String reporterEmail, String assignedTo, String adminNotes) {
        Report report = new Report();
        report.setTitle(title);
        report.setDescription(description);
        report.setCategory(category);
        report.setStatus(status);
        report.setPriority(priority);
        report.setReporterName(reporterName);
        report.setReporterEmail(reporterEmail);
        report.setAssignedTo(assignedTo);
        report.setAdminNotes(adminNotes);
        report.setAttachmentUrl(null);
        return report;
    }
} 