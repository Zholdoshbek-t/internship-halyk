package com.halyk.study.finalboss.repositories;

import com.halyk.study.finalboss.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByChatId(Long id);

    @Query(
            value = "SELECT * FROM chat " +
                    "WHERE id " +
                    "IN (SELECT chat_id FROM chats_api_services " +
                    "WHERE api_service_id = :apiServiceId)",
            nativeQuery = true
    )
    List<Chat> getChatIdsByApiService(Long apiServiceId);
}
