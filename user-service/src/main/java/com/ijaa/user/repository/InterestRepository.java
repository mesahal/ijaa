package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Interest> findByUserIdIn(List<String> userIds); // New method for bulk fetching
    boolean existsByUserIdAndInterestIgnoreCase(String userId, String interest);
    Optional<Interest> findByIdAndUserId(Long id, String userId);
}
