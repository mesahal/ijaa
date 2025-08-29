package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByUserId(String userId);

    @Query("SELECT p FROM Profile p WHERE " +
            "(:currentUsername IS NULL OR p.username != :currentUsername) AND " +
            "(:searchQuery IS NULL OR :searchQuery = '' OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(p.profession) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(p.bio) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
            "(:batch IS NULL OR :batch = '' OR p.batch = :batch) " +
            "AND (:profession IS NULL OR :profession = '' OR " +
            "LOWER(p.profession) LIKE LOWER(CONCAT('%', :profession, '%'))) " +
            "AND (:location IS NULL OR :location = '' OR " +
            "LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Profile> findProfilesWithFilters(
            @Param("searchQuery") String searchQuery,
            @Param("batch") String batch,
            @Param("profession") String profession,
            @Param("location") String location,
            @Param("currentUsername") String currentUsername,
            Pageable pageable
    );

    // Metadata queries for search filters
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.username != :username")
    long countByUsernameNot(@Param("username") String username);

    @Query("SELECT DISTINCT p.batch FROM Profile p WHERE p.username != :username AND p.batch IS NOT NULL ORDER BY p.batch DESC")
    List<String> findDistinctBatchesByUsernameNot(@Param("username") String username);

    @Query("SELECT DISTINCT p.profession FROM Profile p WHERE p.username != :username AND p.profession IS NOT NULL ORDER BY p.profession ASC")
    List<String> findDistinctProfessionsByUsernameNot(@Param("username") String username);

    @Query("SELECT DISTINCT p.location FROM Profile p WHERE p.username != :username AND p.location IS NOT NULL ORDER BY p.location ASC")
    List<String> findDistinctLocationsByUsernameNot(@Param("username") String username);
}
