package com.dc18TokenExchange.STSserver.service;

import com.dc18TokenExchange.STSserver.model.Workplace;
import com.dc18TokenExchange.STSserver.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// This class makes use of the repositories available to the database so that we can retrieve information about the tables. Not currently used, but needs to be implemented rather than in the REST controllers

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    public Workplace getUserWorkplace(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getWorksFor();
    }
}
