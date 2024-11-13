package com.example.demo.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.Products;
import java.util.List;
import java.util.Optional;


public interface ProductsRepository extends JpaRepository<Products, Long> {
    Optional<Products> findByProductId(String productId);
}