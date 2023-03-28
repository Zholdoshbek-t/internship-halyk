package com.halyk.study.salecard.repository;

import com.halyk.study.salecard.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findAllByOrderByNumberAsc();

    List<Product> findAllByOrderByName();

    List<Product> findAllByOrderByCreatedAtDesc();

    List<Product> findAllByNumberIsGreaterThan(Integer number);
}