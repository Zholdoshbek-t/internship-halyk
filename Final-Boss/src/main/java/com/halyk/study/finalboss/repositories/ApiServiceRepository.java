package com.halyk.study.finalboss.repositories;

import com.halyk.study.finalboss.entities.ApiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ApiServiceRepository extends JpaRepository<ApiService, Long> {

    Optional<ApiService> findByName(String name);

    List<ApiService> findAllByOrderByName();

    @Query(
            value = "SELECT * FROM api_service",
            nativeQuery = true
    )
    List<ApiService> findAllApiServiceIds();

    @Query(
            value = "SELECT api_service_id FROM chats_api_services " +
                    "WHERE chat_id = :chatId",
            nativeQuery = true
    )
    List<Long> getApiServiceIdsNotByChatId(Long chatId);

    @Query(
            value = "SELECT * FROM api_service " +
                    "WHERE id NOT IN :ids " +
                    "ORDER BY name",
            nativeQuery = true
    )
    List<ApiService> findAllByIds(List<Long> ids);

    @Modifying
    @Query(
            value = "DELETE FROM chats_api_services " +
                    "WHERE chat_id = :chatId " +
                    "AND api_service_id = :apiServiceId",
            nativeQuery = true
    )
    void removeChatApiService(Long chatId, Long apiServiceId);
}
