package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    @Query("SELECT c FROM Connection c WHERE " +
            "((c.requesterUsername = :username1 AND c.receiverUsername = :username2) OR " +
            "(c.requesterUsername = :username2 AND c.receiverUsername = :username1)) " +
            "AND c.status = com.ijaa.user.domain.entity.Connection.ConnectionStatus.ACCEPTED")
    Optional<Connection> findAcceptedConnection(@Param("username1") String username1,
                                                @Param("username2") String username2);

    @Query("SELECT c.receiverUsername FROM Connection c WHERE c.requesterUsername = :username " +
            "AND c.status = com.ijaa.user.domain.entity.Connection.ConnectionStatus.ACCEPTED " +
            "UNION " +
            "SELECT c.requesterUsername FROM Connection c WHERE c.receiverUsername = :username " +
            "AND c.status = com.ijaa.user.domain.entity.Connection.ConnectionStatus.ACCEPTED")
    List<String> findConnectedUsernames(@Param("username") String username);
}
