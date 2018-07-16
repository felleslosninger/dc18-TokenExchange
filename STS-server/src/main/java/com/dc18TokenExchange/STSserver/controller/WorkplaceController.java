package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.model.Workplace;
import com.dc18TokenExchange.STSserver.service.WorkplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class WorkplaceController {
    private final WorkplaceService workplaceService;

    @Autowired
    public WorkplaceController(WorkplaceService workplaceService) {
        this.workplaceService = workplaceService;
    }

    @GetMapping("/workplace")
    public Page<Workplace> getWorkplaces(Pageable pageable) {
        return workplaceService.getWorkplaces(pageable);
    }

    @GetMapping("/workplace/{orgNum}")
    public Workplace getDistinctByOrgNum(@PathVariable Long orgNum) {
        return workplaceService.getDistinctByOrgNum(orgNum);
    }

    @PostMapping("/workplace")
    public Workplace createWorkplace(@Valid @RequestBody Workplace workplace) {
        return workplaceService.createWorkplace(workplace);
    }

    @PutMapping("/workplace/{orgNum}")
    public Workplace updateWorkplace(@PathVariable Long orgNum, @Valid @RequestBody Workplace workplace) {
        return workplaceService.updateWorkplace(orgNum, workplace);
    }

    @DeleteMapping("/workplace/{orgNum}")
    public ResponseEntity deleteWorkplace(@PathVariable Long orgNum) {
        return workplaceService.deleteWorkplace(orgNum);
    }

    @GetMapping("/user/{userId}/workplace")
    public Workplace getDistinctWorkplaceByUserId(@PathVariable Long userId) {
        return workplaceService.getDistinctWorkplaceByUserId(userId);
    }
}
