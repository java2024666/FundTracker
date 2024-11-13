package com.example.demo.respository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import com.example.demo.models.Price;


public interface PricesRepository extends JpaRepository<Price, Long> {
    @Query("SELECT p FROM Price p WHERE p.productId = :productId " +
       "AND FUNCTION('DATE', p.priceDateTime) = :date")
    Optional<Price> findPriceByProductIdAndPriceDate(@Param("productId") String productId, @Param("date") LocalDate date);
    Optional<Price> findFirstByProductIdAndPriceDateTimeBetweenOrderByPriceDateTimeDesc(
    String productId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}