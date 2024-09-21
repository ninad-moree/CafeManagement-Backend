package com.cafe.cafemanagement.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagement.JWT.JwtFilter;
import com.cafe.cafemanagement.POJO.Category;
import com.cafe.cafemanagement.constants.CafeConstants;
import com.cafe.cafemanagement.dao.CategoryDao;
import com.cafe.cafemanagement.service.CategoryService;
import com.cafe.cafemanagement.utils.CafeUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                if(validateCategoryMap(requestMap, false)) {
                    categoryDao.save(getCategoryFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity("Category Added Successfully", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")) {
            if(requestMap.containsKey("id") && validateId) 
                return true;
            else if(!validateId)
                return true;
        } 
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();

        if(isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }

        category.setName(requestMap.get("name"));
        return category;
    }


}
