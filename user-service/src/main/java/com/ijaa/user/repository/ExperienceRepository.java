package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Experience> findByIdAndUserId(Long id, String userId);
}
