package com.cafe.cafemanagement.restimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cafe.cafemanagement.rest.DashboardRest;
import com.cafe.cafemanagement.service.DashboardService;

@RestController
public class DashboardRestImpl implements DashboardRest {

    @Autowired
    DashboardService  dashboardService;


    @Override
    public ResponseEntity<Map<String, Object>> getDetails() {
        return  dashboardService.getDetails();

    }

}
