package com.ijaa.gateway.repository;

import com.ijaa.gateway.domain.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    
    boolean existsByToken(String token);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistedToken bt WHERE bt.userId = :userId AND bt.userType = :userType")
    void deleteAllUserTokens(@Param("userId") String userId, @Param("userType") String userType);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM BlacklistedToken bt WHERE bt.expiryDate < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
