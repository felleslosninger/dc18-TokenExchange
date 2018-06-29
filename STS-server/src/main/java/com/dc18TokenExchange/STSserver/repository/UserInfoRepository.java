package com.dc18TokenExchange.STSserver.repository;


import com.dc18TokenExchange.STSserver.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
    UserInfo findDistinctByUserId(Long userId);
}
