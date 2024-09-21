package com.cafe.cafemanagement.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cafe.cafemanagement.POJO.Category;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String,  String> requestMap);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(Map<String,  String> requestMap);
}
