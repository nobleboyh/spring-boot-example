package com.springboot.demo.repositories;

import com.springboot.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findProductByProductName(String productProductName);
}
