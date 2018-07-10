package com.dc18TokenExchange.STSserver.service;

import com.dc18TokenExchange.STSserver.exception.ResourceNotFoundException;
import com.dc18TokenExchange.STSserver.model.UserInfo;
import com.dc18TokenExchange.STSserver.model.UserInfoDAO;
import com.dc18TokenExchange.STSserver.repository.UserInfoRepository;
import com.dc18TokenExchange.STSserver.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// This class makes use of the repositories available to the database so that we can retrieve information about the tables.

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private WorkplaceRepository workplaceRepository;


    //Gets all users
    public Page<UserInfo> getUsers(Pageable pageable){
        return userInfoRepository.findAll(pageable);
    }

    //Gets a single user based on userId
    public UserInfo getDistinctByUserId(Long userId){
        return userInfoRepository.findDistinctByUserId(userId);
    }

    //Creates new user
    public UserInfo createUserInfo(UserInfoDAO userInfoDAO){ //NOTE: This method generates a UserInfo object, but does NOT accept it as input. Rather, it uses UserInfoDAO as a proxy for generating a new UserInfo object.

        UserInfo userInfo = new UserInfo();

        userInfo.setFirstName(userInfoDAO.getFirstName());
        userInfo.setLastName(userInfoDAO.getLastName());
        userInfo.setUserId(userInfoDAO.getUserId());
        userInfo.setWorksFor(workplaceRepository.findDistinctByOrgNum(userInfoDAO.getOrgNum()));

        return userInfoRepository.save(userInfo); //This line will just save userInfo as-is

        /*Workplace workplace = workplaceRepository.findDistinctByOrgNum(userInfo.getWorksFor())

        return workplaceRepository.findDistinctByOrgNum(userInfo.getWorksFor().getOrgNum())
                .map(thisUserInfo -> {
                            userInfoRepository.setWorksFor(thisUserInfo);
                            return userInfoRepository.save(userInfo);
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));*/
    }

    //Changes user row
    public UserInfo updateUserInfo(Long userId, UserInfo userInfo) {

        return userInfoRepository.findById(userId)
                .map(thisUserInfo -> {
                    thisUserInfo.setWorksFor(userInfo.getWorksFor());
                    thisUserInfo.setFirstName(userInfo.getFirstName());
                    thisUserInfo.setLastName(userInfo.getLastName());
                    return userInfoRepository.save(thisUserInfo);
                }
                ).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    //Deletes user row
    public ResponseEntity deleteUserInfo(Long userId) {

        return userInfoRepository.findById(userId)
                .map(thisUserInfo -> {
                    userInfoRepository.delete(thisUserInfo);
                    return ResponseEntity.ok().build();
                }
                ).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }


    //Returns users first name as string
    public String getUserInfoFirstNameAsString(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getFirstName();
    }

    //Returns users last name as string
    public String getUserInfoLastNameAsString(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getLastName();
    }
}
