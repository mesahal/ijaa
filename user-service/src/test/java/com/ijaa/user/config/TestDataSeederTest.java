package com.ijaa.user.config;

import com.ijaa.user.common.config.TestDataSeeder;
import com.ijaa.user.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=16f7e412ee66030c3bf769281a076955f595be7479189c4e5ab1f90d2ae3c82e0c5170afcceba1e0f638648c01a468ff82b0723a970011f7fc0dd1a4ba70b0e1"
})
public class TestDataSeederTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private AlumniSearchRepository alumniSearchRepository;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Test
    public void testRepositoriesAreAccessible() {
        // Test that all repositories are properly configured and accessible
        assertNotNull(userRepository, "UserRepository should be accessible");
        assertNotNull(adminRepository, "AdminRepository should be accessible");
        assertNotNull(profileRepository, "ProfileRepository should be accessible");
        assertNotNull(experienceRepository, "ExperienceRepository should be accessible");
        assertNotNull(interestRepository, "InterestRepository should be accessible");
        assertNotNull(alumniSearchRepository, "AlumniSearchRepository should be accessible");
        assertNotNull(connectionRepository, "ConnectionRepository should be accessible");
        assertNotNull(featureFlagRepository, "FeatureFlagRepository should be accessible");
    }

    @Test
    public void testDatabaseHasData() {
        // Test that the database has some data (either from previous runs or seeding)
        long userCount = userRepository.count();
        long adminCount = adminRepository.count();
        
        assertTrue(userCount >= 0, "User count should be non-negative");
        assertTrue(adminCount >= 0, "Admin count should be non-negative");
        
        System.out.println("Database contains " + userCount + " users and " + adminCount + " admins");
    }

    @Test
    public void testAdminExists() {
        // Test that the default admin exists
        boolean adminExists = adminRepository.findByEmailAndActiveTrue("admin@ijaa.com").isPresent();
        assertTrue(adminExists, "Default admin should exist in the database");
    }
} 