package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.models.Price;
import com.example.demo.respository.PriceCalculator;
import com.example.demo.service.ApiService;
import com.example.demo.service.ProductsDataService;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ProductController {
    
    @Autowired
    private ApiService  apiService;

    @Autowired
    private ProductsDataService productsDataService;

    @Autowired
    private PriceCalculator priceCalculatorService;

    //fetch資料
    @PostMapping("/fetch")
    public ResponseEntity<ApiResponse> fetchProductData(@RequestBody Map<String, Object> requestBody){
        ApiResponse response = apiService.getData(requestBody);
        return ResponseEntity.ok(response);
    }

    //查詢當日價格
    @GetMapping("/product/{productId}/datePrice")
    public ResponseEntity<?> getPriceByDate(
        @PathVariable String productId,
        @RequestParam String date) {
        try {
        Optional<Price> price = productsDataService.getPriceByDate(productId, date);
        if (price.isPresent()) {
            return ResponseEntity.ok(price.get());
        } else {
            return ResponseEntity.notFound().build();
        }
        } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //修改當日價格
    @PutMapping("/product/{productId}/updatePrice")
    public ResponseEntity<?> updatePrice(
        @PathVariable String productId,
        @RequestParam String date,
        @RequestParam BigDecimal price) {
        try {
            Optional<Price> updatedPrice = productsDataService.updatePrice(productId, date, price);
            if (updatedPrice.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "更新成功");
                response.put("data", updatedPrice.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to update price");
        }
    }

    //新增價格
    @PostMapping("/product/{productId}/addPrice")
    public ResponseEntity<?> addPrice(
        @PathVariable String productId,
        @RequestParam String date,
        @RequestParam BigDecimal price) {
        try {
            Optional<Price> newPrice = productsDataService.addPrice(productId, date, price);
            if (newPrice.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "新增成功");
                response.put("data", newPrice.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add price");
        }
    }

    //刪除當日價格
    @DeleteMapping("product/{productId}/deletePrice")
    public ResponseEntity<?> deletePrice(
        @PathVariable String productId,
        @RequestParam String date) {
        try {
            Optional<Price> deletedPrice = productsDataService.delectPrice(productId, date);
            if (deletedPrice.isPresent()) {
                return ResponseEntity.ok("刪除成功");
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to delete price");
        }
    }

    //計算價格漲跌
    @GetMapping("/product/{productId}/priceChange")
        public ResponseEntity<?> getPriceChange(
            @PathVariable String productId,
            @RequestParam String startDate,
            @RequestParam String endDate
        ){
            try{
                BigDecimal priceChange = priceCalculatorService.calculatePriceChange(productId, startDate, endDate);
                return ResponseEntity.ok("漲跌:" + priceChange);
            }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }catch(Exception e){
                return ResponseEntity.internalServerError().body("Failed to calculate price change");
            }
        }

        //計算漲跌幅
        @GetMapping("/product/{productId}/priceChangeRate")
        public ResponseEntity<?> getPriceChangeRate(
            @PathVariable String productId,
            @RequestParam String startDate,
            @RequestParam String endDate
        ){
            try{
                BigDecimal priceChangeRate = priceCalculatorService.calculatePriceChangeRate(productId, startDate, endDate);
                return ResponseEntity.ok("漲跌幅:" + priceChangeRate);
            }catch(IllegalArgumentException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }catch(Exception e){
                return ResponseEntity.internalServerError().body("Failed to calculate price change rate");
            }
        }

    }


