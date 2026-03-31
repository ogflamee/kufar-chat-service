package com.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sia.entity.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByAd(Integer adId);

    List<Message> findBySenderOrReceiver(Integer sender, Integer receiver);

    @Query("""
            SELECT m FROM Message m
            WHERE(m.sender =: user1 AND m.receiver =: user2)
            OR (m.sender =: user2 AND m.receiver =: user1)
            ORDER BY m.createdAt
            """)
    List<Message> findChat(@Param("user1") Integer user1,@Param("user2") Integer user2);
}
