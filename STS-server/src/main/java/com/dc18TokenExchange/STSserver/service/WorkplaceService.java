package com.dc18TokenExchange.STSserver.service;


import com.dc18TokenExchange.STSserver.exception.ResourceNotFoundException;
import com.dc18TokenExchange.STSserver.model.Workplace;
import com.dc18TokenExchange.STSserver.repository.UserInfoRepository;
import com.dc18TokenExchange.STSserver.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// This class makes use of the repositories available to the database so that we can retrieve information about the tables.

@Service
public class WorkplaceService {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;


    //Gets all workplaces
    public Page<Workplace> getWorkplaces(Pageable pageable){
        return workplaceRepository.findAll(pageable);
    }

    //Gets a single user based on userId
    public Workplace getDistinctByOrgNum(Long userId){
        return workplaceRepository.findDistinctByOrgNum(userId);
    }

    //Creates new workplace
    public Workplace createWorkplace(Workplace workplace){
        return workplaceRepository.save(workplace);
    }

    //Changes workplace row
    public Workplace updateWorkplace(Long orgNum, Workplace workplace) {

        return workplaceRepository.findById(orgNum)
                .map(thisWorkplace -> {
                            thisWorkplace.setOrgName(workplace.getOrgName());
                            thisWorkplace.setOrgNum(workplace.getOrgNum());
                            return workplaceRepository.save(thisWorkplace);
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("Organization not found with orgNum " + orgNum));
    }

    //Deletes workplace row
    public ResponseEntity deleteWorkplace(Long orgNum) {

        return workplaceRepository.findById(orgNum)
                .map(thisWorkplace -> {
                            workplaceRepository.delete(thisWorkplace);
                            return ResponseEntity.ok().build();
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("Organization not found with orgNum " + orgNum));
    }


    //Returns the workplace of a specified user. Used for token generation for the new STS-token returned to the client.
    public Workplace getDistinctWorkplaceByUserId(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getWorksFor();
    }

    //Returns the workplace name as string of a specified user. Used for token generation for the new STS-token returned to the client.
    public String getDistinctWorkplaceNameByUserIdAsString(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getWorksFor().getOrgName();
    }

    //Returns the workplace number as string of a specified user. Used for token generation for the new STS-token returned to the client.
    public Long getDistinctWorkplaceNumByUserIdAsString(Long userId){
        return userInfoRepository.findDistinctByUserId(userId).getWorksFor().getOrgNum();
    }
}
