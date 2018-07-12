package com.dc18TokenExchange.Resourceserver.controller;

import com.dc18TokenExchange.Resourceserver.StringRequestParser;
import com.dc18TokenExchange.Resourceserver.model.Workplace;
import com.dc18TokenExchange.Resourceserver.model.WorkplaceDAO;
import com.dc18TokenExchange.Resourceserver.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static sun.security.krb5.Confounder.longValue;

@RestController
public class WorkplaceController {

    @Autowired
    private WorkplaceService workplaceService;

    @Autowired
    private StringRequestParser stringRequestParser;


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
    public Workplace updateWorkplace(@Valid @RequestBody WorkplaceDAO workplaceDAO){
        return workplaceService.updateWorkplace(workplaceDAO);
    }

    @DeleteMapping("/workplace/{orgNum}")
    public ResponseEntity deleteWorkplace(@PathVariable Long orgNum){
        return workplaceService.deleteWorkplace(orgNum);
    }


    //Returns the colors specified as a map, i.e. a theme the organization can use
    @PostMapping("/workplace/theme")
    public HttpEntity<Map> getTheme(@Valid @RequestBody String orgNum){

        Long orgNumLong = stringRequestParser.getLongValueFromString(orgNum, "orgNum=");

        return workplaceService.getTheme(orgNumLong);
    }

    //Returns the logo for the specific company
    @PostMapping("/workplace/logo")
    public HttpEntity<byte[]> getImage(@Valid @RequestBody String orgNum){

        Long orgNumLong = stringRequestParser.getLongValueFromString(orgNum, "orgNum=");

        return workplaceService.getLogo(orgNumLong);
    }

    //Returns the logo for the specific company
    @PostMapping("/workplace/background")
    public HttpEntity<byte[]> getBackground(@Valid @RequestBody String orgNum){

        Long orgNumLong = stringRequestParser.getLongValueFromString(orgNum, "orgNum=");

        return workplaceService.getBackground(orgNumLong);
    }

    //Returns the main url for the home page of the specific company
    @GetMapping("/workplace/{orgNum}/homepage")
    public String getHomeUrl(@PathVariable String orgNum){

        Long orgNumLong = stringRequestParser.getLongValueFromString(orgNum, "orgNum=");

        return workplaceService.getHomeUrl(orgNumLong);
    }
}
