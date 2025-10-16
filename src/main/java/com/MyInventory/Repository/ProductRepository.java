package com.MyInventory.Repository;

import com.MyInventory.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByProductName(String productName);

    List<Product> findByCategory(String category);
}
