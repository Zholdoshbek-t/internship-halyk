package com.halyk.study.finalboss.repositories;

import com.halyk.study.finalboss.entities.TelegramMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramMessageRepository extends JpaRepository<TelegramMessage, Long> {

    Optional<TelegramMessage> findByName(String name);
}
