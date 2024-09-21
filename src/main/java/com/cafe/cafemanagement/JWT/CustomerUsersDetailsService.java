package com.cafe.cafemanagement.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.ArrayList;

import com.cafe.cafemanagement.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerUsersDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    private com.cafe.cafemanagement.POJO.User userDetail;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByName: {}", username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail)) 
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else 
            throw new UsernameNotFoundException("User not found.");
    }

    public com.cafe.cafemanagement.POJO.User getUserDetail() {
        // com.cafe.cafemanagement.POJO.User user = userDetail;
        // user.setPassword(null);
        // return user;

        return userDetail;
    }
}
