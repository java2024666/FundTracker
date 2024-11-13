package com.example.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Products {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String productId;

    
    private String productName; 
    
    @Column(nullable = true)
    private String shortName;

    private boolean dataGrouping = false;

    public Products( String productId, String productName, String shortName){
        this.productId = productId;
        this.productName = productName;
        this.shortName = shortName;
    }
    
    public Products() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    


}
