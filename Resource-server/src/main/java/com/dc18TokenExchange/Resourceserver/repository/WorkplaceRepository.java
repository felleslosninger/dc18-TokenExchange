package com.dc18TokenExchange.Resourceserver.repository;

import com.dc18TokenExchange.Resourceserver.model.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    Workplace findDistinctByOrgNum(Long orgNum);
}
