package com.halyk.study.finalboss.repositories;

import com.halyk.study.finalboss.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByAddress(String address);

    List<Url> findAllByApiServiceId(Long id);
}
