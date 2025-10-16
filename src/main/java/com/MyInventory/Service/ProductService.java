package com.MyInventory.Service;

import com.MyInventory.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.MyInventory.Entity.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public Product addNewProduct(Product product)
    {
        return this.productRepository.save(product);
    }


    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public List<Product> findProductByCategory(String category) {
        return this.productRepository.findByCategory(category);
    }
}
