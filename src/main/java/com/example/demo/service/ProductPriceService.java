package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Price;
import com.example.demo.respository.PriceCalculator;

@Service
public class ProductPriceService implements PriceCalculator{

    @Autowired
    private ProductsDataService productsDataService;

    //計算價格漲跌
    @Override
    public BigDecimal calculatePriceChange(String productId, String startDate, String endDate){
        Optional<Price> startPrice = productsDataService.getPriceByDate(productId, startDate);
        Optional<Price> endPrice = productsDataService.getPriceByDate(productId, endDate);

        if(!startPrice.isPresent() || !endPrice.isPresent()){
            throw new IllegalArgumentException("Price not found ");
        }

        return endPrice.get().getPrice().subtract(startPrice.get().getPrice());
    }

    //計算漲跌幅
    @Override
    public BigDecimal calculatePriceChangeRate(String productId, String startDate, String endDate) {
        Optional<Price> startPrice = productsDataService.getPriceByDate(productId, startDate);
        Optional<Price> endPrice = productsDataService.getPriceByDate(productId, endDate);

        if (!startPrice.isPresent() || !endPrice.isPresent()) {
            throw new IllegalArgumentException("Price not found ");
        }

        BigDecimal priceChange = endPrice.get().getPrice().subtract(startPrice.get().getPrice());
        
        return priceChange.divide(startPrice.get().getPrice(), 4, RoundingMode.HALF_UP)
                         .multiply(new BigDecimal("100"));
    }
}
