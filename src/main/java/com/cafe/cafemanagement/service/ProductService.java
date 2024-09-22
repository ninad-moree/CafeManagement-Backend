package com.cafe.cafemanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.cafemanagement.wrapper.ProductWrapper;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String,  String> requestMap);
    
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String,  String> requestMap);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateProductStatus(Map<String,  String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProductByCategory(Integer id);

     ResponseEntity<ProductWrapper> getProductById(Integer id);
}
