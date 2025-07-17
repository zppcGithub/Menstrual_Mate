package com.menstrualmate.repository;

import com.menstrualmate.entity.HealthTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthTipRepository extends JpaRepository<HealthTip, Long> {
    
    List<HealthTip> findByPhaseOrderByPriorityDesc(HealthTip.Phase phase);
    
    List<HealthTip> findByCategoryOrderByPriorityDesc(HealthTip.Category category);
    
    List<HealthTip> findByPhaseAndCategoryOrderByPriorityDesc(HealthTip.Phase phase, HealthTip.Category category);
    
    List<HealthTip> findByOrderByPhaseAscPriorityDesc();
}