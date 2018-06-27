package com.dc18TokenExchange.STSserver.controller;


import com.dc18TokenExchange.STSserver.exception.ResourceNotFoundException;
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
public class WorkplaceController {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @GetMapping("/workplace")
    public Page<Workplace> getWorkplaces(Pageable pageable){
        return workplaceRepository.findAll(pageable);
    }

    @GetMapping("/workplace/{orgNum}")
    public Workplace getDistinctByOrgNum(@PathVariable Long orgNum){
        return workplaceRepository.findDistinctByOrgNum(orgNum);
    }

    @PostMapping("/workplace")
    public Workplace createWorkplace(@Valid @RequestBody Workplace workplace){
        return workplaceRepository.save(workplace);
    }

    //Returns the workplace of a specified user
    @GetMapping("/user/{userId}/workplace")
    public Workplace getDistinctByUserId(@PathVariable Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getWorksFor();
    }
}
