package com.example.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponse {
    
    @JsonProperty("Data")
    private List<Product> productdata;

    public List<Product> getProductData(){
        return productdata;
    }

    public void setData(List<Product> productdata){
        this.productdata = productdata;
    }

    public static class Product {

        @JsonProperty("id")
        private String productId;

        @JsonProperty("name")
        private String productName;

        @JsonProperty("shortName")
        private String shortName;

        @JsonProperty("dataGrouping")
        private boolean dataGrouping;


        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public boolean isDataGrouping() {
            return dataGrouping;
        }

        public void setDataGrouping(boolean dataGrouping) {
            this.dataGrouping = dataGrouping;
        }

        //Price data
        @JsonProperty("data")
        private List<List<Object>> priceData;


        public List<List<Object>> getPriceData() {
            return priceData;
        }

        public void setPriceData(List<List<Object>> priceData) {
            this.priceData = priceData;
        }

        
    }
}
