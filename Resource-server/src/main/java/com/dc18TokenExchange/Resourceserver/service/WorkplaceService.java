package com.dc18TokenExchange.Resourceserver.service;


import com.dc18TokenExchange.Resourceserver.ImageHandling;
import com.dc18TokenExchange.Resourceserver.exception.ResourceNotFoundException;
import com.dc18TokenExchange.Resourceserver.model.Workplace;
import com.dc18TokenExchange.Resourceserver.model.WorkplaceDAO;
import com.dc18TokenExchange.Resourceserver.repository.WorkplaceRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// This class makes use of the repositories available to the database so that we can retrieve information about the tables.

@Service
public class WorkplaceService {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    @Autowired
    private ImageHandling imageHandling;


    //Gets all workplaces
    public Page<Workplace> getWorkplaces(Pageable pageable){
        return workplaceRepository.findAll(pageable);
    }

    //Gets a single user based on userId
    public Workplace getDistinctByOrgNum(Long userId){
        return workplaceRepository.findDistinctByOrgNum(userId);
    }

    //Creates new workplace
    public Workplace createWorkplace(WorkplaceDAO workplaceDAO){

        Workplace workplace = new Workplace();

        byte[] bFile = imageHandling.saveImage(workplaceDAO.getPath());

        workplace.setOrgName(workplaceDAO.getOrgName());
        workplace.setOrgNum(workplaceDAO.getOrgNum());
        workplace.setPri_col(workplaceDAO.getPri_col());
        workplace.setSec_col(workplaceDAO.getSec_col());
        workplace.setImage(bFile);

        return workplaceRepository.save(workplace);
    }

    //Changes workplace row
    public Workplace updateWorkplace(Long orgNum, WorkplaceDAO workplaceDAO) {

        byte[] bFile = imageHandling.saveImage(workplaceDAO.getPath());

        return workplaceRepository.findById(orgNum)
                .map(thisWorkplace -> {
                            thisWorkplace.setOrgName(workplaceDAO.getOrgName());
                            thisWorkplace.setOrgNum(workplaceDAO.getOrgNum());
                            thisWorkplace.setPri_col(workplaceDAO.getPri_col());
                            thisWorkplace.setPri_col(workplaceDAO.getPri_col());
                            thisWorkplace.setImage(bFile);
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


    //Returns the logo for the specific company
    public ResponseEntity<byte[]> getLogo(Long orgNum){
        //imageHandling.getImage(newPath,workplaceRepository.findDistinctByOrgNum(orgNum));
        byte[] logo = workplaceRepository.findDistinctByOrgNum(orgNum).getImage();

        return new ResponseEntity<>(logo, HttpStatus.OK);
    }

    //Returns the colors specified as a map, i.e. a theme the organization can use
    public ResponseEntity<Map> getTheme(Long orgNum){
        //imageHandling.getImage(newPath,workplaceRepository.findDistinctByOrgNum(orgNum));
        Workplace workplace = workplaceRepository.findDistinctByOrgNum(orgNum);

        Map<String, String> theme = new HashMap<>();
        theme.put("pri_col", workplace.getPri_col());
        theme.put("sec_col", workplace.getSec_col());

        return new ResponseEntity<>(theme, HttpStatus.OK);
    }
}
