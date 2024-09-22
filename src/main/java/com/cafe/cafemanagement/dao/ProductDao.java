package com.cafe.cafemanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagement.POJO.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

}
