package com.dc18TokenExchange.STSserver.repository;

import com.dc18TokenExchange.STSserver.model.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    Workplace findDistinctByOrgNum(Long orgNum);
}
