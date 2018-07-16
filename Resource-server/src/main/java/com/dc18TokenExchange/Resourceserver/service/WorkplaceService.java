package com.dc18TokenExchange.Resourceserver.service;

import com.dc18TokenExchange.Resourceserver.ImageHandling;
import com.dc18TokenExchange.Resourceserver.exception.ResourceNotFoundException;
import com.dc18TokenExchange.Resourceserver.model.Workplace;
import com.dc18TokenExchange.Resourceserver.model.WorkplaceDAO;
import com.dc18TokenExchange.Resourceserver.repository.WorkplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Workplace> getWorkplaces(Pageable pageable) {
        return workplaceRepository.findAll(pageable);
    }

    //Gets a single user based on userId
    public Workplace getDistinctByOrgNum(Long userId) {
        return workplaceRepository.findDistinctByOrgNum(userId);
    }

    //Creates new workplace
    public Workplace createWorkplace(WorkplaceDAO workplaceDAO) {
        Workplace workplace = new Workplace();
        byte[] logoFile = imageHandling.saveImage(workplaceDAO.getLogo_path());
        byte[] backgroundFile = imageHandling.saveImage(workplaceDAO.getBackground_path());
        workplace.setOrgName(workplaceDAO.getOrgName());
        workplace.setOrgNum(workplaceDAO.getOrgNum());
        workplace.setPri_col(workplaceDAO.getPri_col());
        workplace.setSec_col(workplaceDAO.getSec_col());
        workplace.setLogo_img(logoFile);
        workplace.setBackground_img(backgroundFile);
        workplace.setHome_url(workplaceDAO.getHome_url());
        return workplaceRepository.save(workplace);
    }

    //Changes workplace row
    public Workplace updateWorkplace(WorkplaceDAO workplaceDAO) {
        byte[] logoFile = imageHandling.saveImage(workplaceDAO.getLogo_path());
        byte[] backgroundFile = imageHandling.saveImage(workplaceDAO.getBackground_path());
        return workplaceRepository.findById(workplaceDAO.getOrgNum())
                .map(thisWorkplace -> {
                            thisWorkplace.setOrgName(workplaceDAO.getOrgName());
                            thisWorkplace.setOrgNum(workplaceDAO.getOrgNum());
                            thisWorkplace.setPri_col(workplaceDAO.getPri_col());
                            thisWorkplace.setPri_col(workplaceDAO.getPri_col());
                            thisWorkplace.setLogo_img(logoFile);
                            thisWorkplace.setBackground_img(backgroundFile);
                            thisWorkplace.setHome_url(workplaceDAO.getHome_url());
                            return workplaceRepository.save(thisWorkplace);
                        }
                ).orElseThrow(() -> new ResourceNotFoundException("Organization not found with orgNum " + workplaceDAO.getOrgNum()));
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

    //Returns the colors specified as a map, i.e. a theme the organization can use
    public HttpEntity<Map> getTheme(Long orgNum) {
        //imageHandling.getImage(newPath,workplaceRepository.findDistinctByOrgNum(orgNum));
        Workplace workplace = workplaceRepository.findDistinctByOrgNum(orgNum);
        Map<String, String> theme = new HashMap<>();
        theme.put("pri_col", workplace.getPri_col());
        theme.put("sec_col", workplace.getSec_col());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(theme, headers);
    }

    //Returns the logo for the specific company
    public HttpEntity<byte[]> getLogo(Long orgNum) {
        byte[] logo = workplaceRepository.findDistinctByOrgNum(orgNum).getLogo_img();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(logo.length);
        return new HttpEntity<>(logo, headers);
    }

    //Returns the provided background for the specific company
    public HttpEntity<byte[]> getBackground(Long orgNum) {
        byte[] background = workplaceRepository.findDistinctByOrgNum(orgNum).getBackground_img();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(background.length);
        return new HttpEntity<>(background, headers);
    }

    //Returns the main url for the home page of the specific company
    public String getHomeUrl(Long orgNum) {
        return workplaceRepository.findDistinctByOrgNum(orgNum).getHome_url();
    }
}
