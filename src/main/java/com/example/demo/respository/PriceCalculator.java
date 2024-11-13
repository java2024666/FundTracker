package com.example.demo.respository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PriceCalculator {
    BigDecimal calculatePriceChange(String productId, String startDate, String endDate);
    BigDecimal calculatePriceChangeRate(String productId, String StartDate, String endDate);
}
