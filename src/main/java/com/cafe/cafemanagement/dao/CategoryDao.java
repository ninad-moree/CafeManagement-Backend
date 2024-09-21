package com.cafe.cafemanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagement.POJO.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

    List<Category> getAllCategories();

}
