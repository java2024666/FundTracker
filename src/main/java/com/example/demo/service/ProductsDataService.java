package com.example.demo.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.models.Price;
import com.example.demo.models.Products;
import com.example.demo.respository.ProductsRepository;
import com.example.demo.respository.PricesRepository;

@Service
public class ProductsDataService {

    @Autowired
    private ProductsRepository productsRepository;  // 操作 products 資料表
    @Autowired
    private PricesRepository pricesRepository;  // 操作 prices 資料表

    //將timestamp轉換成LocalDateTime
    private LocalDateTime convertTimestampToDateTime(Long timestamp) {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.of("Asia/Taipei")
    );
    }

    //儲存Data到資料庫
    public void saveData(ApiResponse apiResponse) {
        if (apiResponse.getProductData() != null) {
            // 遍歷 API 回應中的資料
            for (ApiResponse.Product productData : apiResponse.getProductData()) {
                //檢查產品是否存在
                Optional<Products> existingProduct = productsRepository.findByProductId(productData.getProductId());
                Products product;
                if(existingProduct.isPresent()){
                    product = existingProduct.get();
                    product.setProductName(productData.getProductName());
                    product.setShortName(productData.getShortName());
                    product.setDataGrouping(productData.isDataGrouping());
                }else{
                product = new Products(
                    productData.getProductId(),
                    productData.getProductName(),
                    productData.getShortName()
                );
                // 設置資料是否分組的欄位
                product.setDataGrouping(productData.isDataGrouping());
                }

                // 保存或更新產品資料
                productsRepository.save(product);

                // 保存價格資訊
                if (productData.getPriceData() != null) {
                    for (List<Object> priceData : productData.getPriceData()) {
                        // 創建 Price 實體並設置資料
                        Price price = new Price();
                        price.setProductId(productData.getProductId());  // 設置產品 ID

                        // 處理時間戳記
                        Long timestamp = ((Number) priceData.get(0)).longValue();  // 取得時間戳，轉換為 DateTime
                        price.setPriceDateTime(convertTimestampToDateTime(timestamp));
                        // 處理價格
                        String priceStr = priceData.get(1).toString();  // 價格為 String 型別，轉為 BigDecimal
                        price.setPrice(new BigDecimal(priceStr));  // 將價格設定為 BigDecimal

                        // 保存價格資料
                        pricesRepository.save(price);  // 保存價格
                    }
                }
            }
        }
    }

    //查詢當日價格
    public Optional<Price> getPriceByDate(String productId, String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
            
            // 設定該日期的起始時間和結束時間
            LocalDateTime startOfDay = localDate.atStartOfDay();
            LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();
            
            // 查詢該日期範圍內的價格
            return pricesRepository.findFirstByProductIdAndPriceDateTimeBetweenOrderByPriceDateTimeDesc(
            productId, startOfDay, endOfDay);
        } catch (Exception e) {
            throw new IllegalArgumentException("格式錯誤,請使用 yyyy/MM/dd");
        }
    }

    //修改當日價格
    public Optional<Price> updatePrice(String productId, String date, BigDecimal price){
        //先搜尋當日價格是否存在
        Optional<Price>  existingPrice = getPriceByDate(productId, date);
        
        if(existingPrice.isPresent()){
            Price priceRecord = existingPrice.get();
            priceRecord.setPrice(price);
            return Optional.of(pricesRepository.save(priceRecord));
        }
    return Optional.empty();
    }

    //新增價格
    public Optional<Price> addPrice(String productId, String date, BigDecimal price){
        try {
            // 檢查產品是否存在
            Optional<Products> existingProduct = productsRepository.findByProductId(productId);
            if (!existingProduct.isPresent()) {
                return Optional.empty();
            }
    
            // 檢查該日期是否已有價格
            Optional<Price> existingPrice = getPriceByDate(productId, date);
            if (existingPrice.isPresent()) {
                throw new IllegalArgumentException("Price already exists for this date");
            }
    
            // 解析日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.parse(date, formatter);
    
            // 創建新的價格記錄
            Price newPrice = new Price();
            newPrice.setProductId(productId);
            newPrice.setPriceDateTime(localDate.atTime(8,0));
            newPrice.setPrice(price);
    
            // 儲存並返回
            Price savedPrice = pricesRepository.save(newPrice);
            return Optional.of(savedPrice);
            
        } catch (IllegalArgumentException e) {
            throw e;  // 重新拋出參數錯誤
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to add price: " + e.getMessage());
        }
    }

    //刪除價格
    public Optional<Price> delectPrice(String productId, String date){
        try {
            Optional<Price> existingPrice = getPriceByDate(productId, date);
            if(existingPrice.isPresent()){
                pricesRepository.delete(existingPrice.get());
                return Optional.of(existingPrice.get());
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete price: " + e.getMessage());
        }
    }
}
