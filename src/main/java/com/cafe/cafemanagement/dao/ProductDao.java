package com.cafe.cafemanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagement.POJO.Product;
import com.cafe.cafemanagement.wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

}
