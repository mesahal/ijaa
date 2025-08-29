package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

    Optional<FeatureFlag> findByName(String name);
    
    boolean existsByName(String name);
    
    List<FeatureFlag> findByEnabledTrue();
    
    List<FeatureFlag> findByEnabledFalse();
    
    List<FeatureFlag> findByParentIsNull();
    
    List<FeatureFlag> findByParentId(Long parentId);
    
    @Query("SELECT f FROM FeatureFlag f WHERE f.parent IS NULL ORDER BY f.name")
    List<FeatureFlag> findAllTopLevelFlags();
    
    @Query("SELECT f FROM FeatureFlag f WHERE f.name LIKE %:prefix% ORDER BY f.name")
    List<FeatureFlag> findByNameStartingWith(@Param("prefix") String prefix);
    
    @Query("SELECT f FROM FeatureFlag f WHERE f.enabled = true AND (f.parent IS NULL OR f.parent.enabled = true)")
    List<FeatureFlag> findEnabledFlagsWithEnabledParents();
} 