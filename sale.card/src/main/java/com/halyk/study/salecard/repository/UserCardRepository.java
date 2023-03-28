package com.halyk.study.salecard.repository;

import com.halyk.study.salecard.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    Optional<UserCard> findByUserIdAndProductId(Long userId, Long cardId);

    List<UserCard> findAllByUserIdOrderByProductNumberAsc(Long userId);

    List<UserCard> findAllByUserIdOrderByProductName(Long userId);

    List<UserCard> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
