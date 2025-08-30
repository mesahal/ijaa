package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByUserId(String userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(String userId);
    
    // Google OAuth methods
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
    boolean existsByEmail(String email);
    boolean existsByGoogleId(String googleId);
    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);
    
    // Dashboard statistics methods
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    Long countByActiveTrue();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = false")
    Long countByActiveFalse();
}
