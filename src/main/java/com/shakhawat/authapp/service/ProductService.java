package com.shakhawat.authapp.service;

import com.shakhawat.authapp.dto.ProductDto;
import com.shakhawat.authapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product saveProduct(ProductDto product);

    Product getProductById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> searchProducts(String query, Pageable pageable);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
