package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.AlumniSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumniSearchRepository extends JpaRepository<AlumniSearch, Long> {

    Optional<AlumniSearch> findByUsername(String username);

    @Query("SELECT a FROM AlumniSearch a WHERE a.isVisible = true " +
            "AND (:searchQuery IS NULL OR :searchQuery = '' OR " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(a.profession) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(a.company) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) " +
            "AND (:batch IS NULL OR :batch = '' OR a.batch = :batch) " +
            "AND (:department IS NULL OR :department = '' OR a.department = :department) " +
            "AND (:profession IS NULL OR :profession = '' OR " +
            "LOWER(a.profession) LIKE LOWER(CONCAT('%', :profession, '%'))) " +
            "AND (:location IS NULL OR :location = '' OR " +
            "LOWER(a.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<AlumniSearch> findAlumniWithFilters(
            @Param("searchQuery") String searchQuery,
            @Param("batch") String batch,
            @Param("profession") String profession,
            @Param("location") String location,
            Pageable pageable
    );
}
