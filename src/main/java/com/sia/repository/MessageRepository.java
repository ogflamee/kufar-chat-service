package com.sia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sia.entity.Message;

import java.util.List;

/**
 * Репозиторий для работы с сообщениями.
 * Обеспечивает доступ к данным в БД.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByAd(Integer ad);

    List<Message> findBySenderOrReceiver(Integer sender, Integer receiver);

    @Query("""
            SELECT m FROM Message m
            WHERE(m.sender = :firstUserId AND m.receiver = :secondUserId)
            OR (m.sender = :secondUserId AND m.receiver = :firstUserId)
            ORDER BY m.createdAt
            """)
    List<Message> findChat(@Param("firstUserId") Integer firstUserId,
                           @Param("secondUserId") Integer secondUserId);

    @Query("""
            SELECT m FROM Message m
            WHERE m.ad = :adId
            AND (
            (m.sender = :firstUserId AND m.receiver = :secondUserId)
            OR (m.sender = :secondUserId AND m.receiver = :firstUserId)
            )
            ORDER BY m.createdAt
            """)
    List<Message> findChatByAdAndUsers(@Param("adId") Integer adId,
                           @Param("firstUserId") Integer firstUserId,
                           @Param("secondUserId") Integer secondUserId);
}
