package com.cafe.cafemanagement.serviceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagement.dao.BillDao;
import com.cafe.cafemanagement.dao.CategoryDao;
import com.cafe.cafemanagement.dao.ProductDao;
import com.cafe.cafemanagement.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao  productDao;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<Map<String, Object>> getDetails() {
        Map<String, Object> map = new HashMap<>();

        map.put("category", categoryDao.count());
        map.put("product", productDao.count());
        map.put("bill", billDao.count());

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
