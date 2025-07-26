package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByUsernameOrderByCreatedAtDesc(String username);
    void deleteByIdAndUsername(Long id, String username);
}
