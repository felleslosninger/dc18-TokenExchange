package com.dc18TokenExchange.Resourceserver.controller;

import com.dc18TokenExchange.Resourceserver.model.Workplace;
import com.dc18TokenExchange.Resourceserver.model.WorkplaceDAO;
import com.dc18TokenExchange.Resourceserver.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class WorkplaceController {

    @Autowired
    private WorkplaceService workplaceService;


    @GetMapping("/workplace")
    public Page<Workplace> getWorkplaces(Pageable pageable){
        return workplaceService.getWorkplaces(pageable);
    }

    @GetMapping("/workplace/{orgNum}")
    public Workplace getDistinctByOrgNum(@PathVariable Long orgNum){
        return workplaceService.getDistinctByOrgNum(orgNum);
    }

    @PostMapping("/workplace")
    public Workplace createWorkplace(@Valid @RequestBody WorkplaceDAO workplaceDAO){
        return workplaceService.createWorkplace(workplaceDAO);
    }

    @PutMapping("/workplace/{orgNum}")
    public Workplace updateWorkplace(@PathVariable Long orgNum, @Valid @RequestBody WorkplaceDAO workplaceDAO){
        return workplaceService.updateWorkplace(orgNum, workplaceDAO);
    }

    @DeleteMapping("/workplace/{orgNum}")
    public ResponseEntity deleteWorkplace(@PathVariable Long orgNum){
        return workplaceService.deleteWorkplace(orgNum);
    }

    //Returns the logo for the specific company
    @GetMapping("/workplace/{orgNum}/logo")
    public ResponseEntity<byte[]> getImage(@PathVariable Long orgNum){
        return workplaceService.getLogo(orgNum);
    }

    //Returns the colors specified as a map, i.e. a theme the organization can use
    @GetMapping("/workplace/{orgNum}/theme")
    public ResponseEntity<Map> getTheme(@PathVariable Long orgNum){
        return workplaceService.getTheme(orgNum);
    }
}
