package com.halyk.study.finalboss.repositories;

import com.halyk.study.finalboss.entities.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, Long> {

    @Query(
            value = "SELECT * FROM error_message " +
                    "WHERE DATE(created_at) >= :from " +
                    "AND DATE(created_at) <= :till " +
                    "ORDER BY created_at DESC " +
                    "LIMIT :limitNumber",
            nativeQuery = true
    )
    List<ErrorMessage> findAllByTwoDatesLimit(LocalDate from, LocalDate till, Integer limitNumber);

    @Query(
            value = "SELECT * FROM error_message " +
                    "WHERE DATE(created_at) >= :from " +
                    "AND DATE(created_at) <= :till " +
                    "ORDER BY created_at DESC",
            nativeQuery = true
    )
    List<ErrorMessage> findAllByTwoDates(LocalDate from, LocalDate till);

    @Query(
            value = "SELECT * FROM error_message " +
                    "WHERE id " +
                    "IN (SELECT em.id FROM error_message em " +
                    "INNER JOIN url u " +
                    "ON em.url_id = u.id " +
                    "INNER JOIN api_service apis " +
                    "ON u.api_service_id = apis.id " +
                    "WHERE apis.id IN :apiIds " +
                    "AND DATE(em.created_at) >= :from " +
                    "AND DATE(em.created_at) <= :till " +
                    "ORDER BY em.created_at DESC " +
                    "LIMIT :limitNumber)",
            nativeQuery = true
    )
    List<ErrorMessage> findAllByApiServiceUrlDateFromTillLimit(List<Long> apiIds, LocalDate from,
                                                               LocalDate till, Integer limitNumber);

    @Query(
            value = "SELECT * FROM error_message " +
                    "WHERE id " +
                    "IN (SELECT em.id FROM error_message em " +
                    "INNER JOIN url u " +
                    "ON em.url_id = u.id " +
                    "INNER JOIN api_service apis " +
                    "ON u.api_service_id = apis.id " +
                    "WHERE apis.id IN :apiIds " +
                    "AND DATE(em.created_at) >= :from " +
                    "AND DATE(em.created_at) <= :till " +
                    "ORDER BY em.created_at DESC)",
            nativeQuery = true
    )
    List<ErrorMessage> findAllByApiServiceUrlDateFromTill(List<Long> apiIds, LocalDate from, LocalDate till);
}
