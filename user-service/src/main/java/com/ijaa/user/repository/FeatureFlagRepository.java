package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

    Optional<FeatureFlag> findByFeatureName(String featureName);
    
    boolean existsByFeatureName(String featureName);
    
    List<FeatureFlag> findByEnabledTrue();
    
    List<FeatureFlag> findByEnabledFalse();
} 