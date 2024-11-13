package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.ApiResponse;


@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductsDataService productsDataService;  // 注入 ProductsDataService

    @Value("${api.url}")
    private String API_URL;
   
    public ApiResponse getData(Map<String, Object> requestBody) {
        // 構造請求的 JSON 主體
        Map<String, Object> req = new HashMap<>();
        req.put("req", requestBody);
        
        // 使用 POST 請求，並將 JSON 回應轉換為 ApiRespones 物件
        ApiResponse response = restTemplate.postForObject(API_URL, req, ApiResponse.class);


        if (response != null) {
            productsDataService.saveData(response);  // 調用保存方法
        }

        return response;
    }
}
