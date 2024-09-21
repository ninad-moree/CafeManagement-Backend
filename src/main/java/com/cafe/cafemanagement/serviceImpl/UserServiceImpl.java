package com.cafe.cafemanagement.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cafe.cafemanagement.JWT.CustomerUsersDetailsService;
import com.cafe.cafemanagement.JWT.JwtFilter;
import com.cafe.cafemanagement.JWT.JwtUtil;
import com.cafe.cafemanagement.POJO.User;
import com.cafe.cafemanagement.constants.CafeConstants;
import com.cafe.cafemanagement.dao.UserDao;
import com.cafe.cafemanagement.service.UserService;
import com.cafe.cafemanagement.utils.CafeUtils;
import com.cafe.cafemanagement.utils.EmailUtils;
import com.cafe.cafemanagement.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    /* ################################## SIGN UP METHOD ################################## */
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup: {} ", requestMap);

        try {
            if(validateSignupMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email ALready Exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignupMap(Map<String, String> requestMap) {
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber") 
           && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User  getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    /* ################################## LOGIN METHOD ################################## */
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );

            if(auth.isAuthenticated()) {
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>(
                        "{\"token\":\"" + 
                        jwtUtil.generateToken(
                            customerUsersDetailsService.getUserDetail().getEmail(), 
                            customerUsersDetailsService.getUserDetail().getRole()
                        ) 
                        + "\"}", 
                        HttpStatus.OK
                    );
                } else {
                    new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval"+"\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("{}", e);
        }

        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ################################## GET ALL USERS METHOD ################################## */
    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if(jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ################################## UPDATE USERS STATUS METHOD ################################## */
    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional<User> optional =  userDao.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()) {
                    userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseEntity("user status updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User Id does not exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());

        if(status!=null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(
                jwtFilter.getCurrentUser(), 
                "Account Approved", "USER:- " + user + "\n is approved by \nADMIN:- " + jwtFilter.getCurrentUser(), 
                allAdmin
            );
        }  else {
            emailUtils.sendSimpleMessage(
                jwtFilter.getCurrentUser(), 
                "Account Disabled", "USER:- " + user + "\n is disabled by \nADMIN:- " + jwtFilter.getCurrentUser(), 
                allAdmin
            );
        }
    }
}
