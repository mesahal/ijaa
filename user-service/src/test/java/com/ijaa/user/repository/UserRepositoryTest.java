package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.User;
import com.ijaa.user.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.dao.DataIntegrityViolationException;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = TestDataBuilder.createTestUser();
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_SAVE_1", "saveuser1");
        // Act
        User savedUser = userRepository.saveAndFlush(user);
        entityManager.clear();
        // Assert
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(user.getUserId(), savedUser.getUserId());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
    }

    @Test
    @DisplayName("Should find user by username successfully")
    void findByUsername_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_FIND_1", "finduser1");
        User savedUser = userRepository.saveAndFlush(user);
        entityManager.clear();
        // Act
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals(savedUser.getUserId(), foundUser.get().getUserId());
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when username not found")
    void findByUsername_NotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should check if username exists successfully")
    void existsByUsername_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_EXISTS_1", "existsuser1");
        userRepository.saveAndFlush(user);
        entityManager.clear();

        // Act
        boolean exists = userRepository.existsByUsername(user.getUsername());

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when username does not exist")
    void existsByUsername_NotFound() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }

    @Test
    @DisplayName("Should check if user ID exists successfully")
    void existsByUserId_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_EXISTS_2", "existsuser2");
        userRepository.saveAndFlush(user);
        entityManager.clear();

        // Act
        boolean exists = userRepository.existsByUserId(user.getUserId());

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when user ID does not exist")
    void existsByUserId_NotFound() {
        // Act
        boolean exists = userRepository.existsByUserId("NONEXISTENT_USER_ID");

        // Assert
        assertFalse(exists);
    }



    @Test
    @DisplayName("Should update user successfully")
    void update_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_UPDATE_1", "updateuser1");
        User savedUser = userRepository.saveAndFlush(user);
        String newPassword = "newPassword123";
        savedUser.setPassword(newPassword);
        entityManager.clear();

        // Act
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals(newPassword, updatedUser.getPassword());
        
        // Verify in database
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertEquals(newPassword, foundUser.getPassword());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void delete_Success() {
        // Arrange
        User user = TestDataBuilder.createTestUser("USER_DELETE_1", "deleteuser1");
        User savedUser = userRepository.saveAndFlush(user);
        entityManager.clear();

        // Act
        userRepository.delete(savedUser);

        // Assert
        User foundUser = entityManager.find(User.class, savedUser.getId());
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Should handle unique constraint on username")
    void save_DuplicateUsername() {
        // Arrange
        User firstUser = TestDataBuilder.createTestUser("USER_DUPLICATE_1", "duplicateuser1");
        User secondUser = TestDataBuilder.createTestUser("USER_DUPLICATE_2", "duplicateuser1"); // Same username
        userRepository.saveAndFlush(firstUser);
        // This should throw an exception due to unique constraint
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(secondUser);
        });
    }

    @Test
    @DisplayName("Should handle unique constraint on user ID")
    void save_DuplicateUserId() {
        // Arrange
        User firstUser = TestDataBuilder.createTestUser("USER_DUPLICATE_3", "differentuser1");
        User secondUser = TestDataBuilder.createTestUser("USER_DUPLICATE_3", "differentuser2"); // Same user ID
        userRepository.saveAndFlush(firstUser);
        // This should throw an exception due to unique constraint
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(secondUser);
        });
    }

    @Test
    @DisplayName("Should handle null username gracefully")
    void save_NullUsername() {
        // Arrange
        User userWithNullUsername = TestDataBuilder.createTestUser("USER_NULL_1", "nulluser1");
        userWithNullUsername.setUsername(null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(userWithNullUsername);
        });
    }

    @Test
    @DisplayName("Should handle null user ID gracefully")
    void save_NullUserId() {
        // Arrange
        User userWithNullUserId = TestDataBuilder.createTestUser("USER_NULL_2", "nulluser2");
        userWithNullUserId.setUserId(null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(userWithNullUserId);
        });
    }

    @Test
    @DisplayName("Should handle null password gracefully")
    void save_NullPassword() {
        // Arrange
        User userWithNullPassword = TestDataBuilder.createTestUser("USER_NULL_3", "nulluser3");
        userWithNullPassword.setPassword(null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            userRepository.saveAndFlush(userWithNullPassword);
        });
    }
} 