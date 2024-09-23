package com.cafe.cafemanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafe.cafemanagement.POJO.Bill;

public interface BillDao extends JpaRepository<Bill, Integer> {

}
