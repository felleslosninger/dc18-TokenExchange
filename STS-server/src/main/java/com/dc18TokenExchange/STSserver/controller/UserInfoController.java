package com.dc18TokenExchange.STSserver.controller;


import com.dc18TokenExchange.STSserver.exception.ResourceNotFoundException;
import com.dc18TokenExchange.STSserver.model.UserInfo;
import com.dc18TokenExchange.STSserver.model.Workplace;
import com.dc18TokenExchange.STSserver.repository.UserInfoRepository;
import com.dc18TokenExchange.STSserver.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;


    @GetMapping("/user")
    public Page<UserInfo> getUsers(Pageable pageable){
        return userInfoRepository.findAll(pageable);
    }

    @GetMapping("/user/{userId}")
    public UserInfo getDistinctByUserId(@PathVariable Long userId){
        return userInfoRepository.findDistinctByUserId(userId);
    }

    @PostMapping("/user")
    public UserInfo createUserInfo(@Valid @RequestBody UserInfo userInfo){
        return userInfoRepository.save(userInfo);
    }
}
