package com.menstrualmate.service.impl;

import com.menstrualmate.entity.HealthTip;
import com.menstrualmate.repository.HealthTipRepository;
import com.menstrualmate.service.HealthTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HealthTipServiceImpl implements HealthTipService {

    @Autowired
    private HealthTipRepository healthTipRepository;

    @Override
    public List<HealthTip> getAllHealthTips() {
        return healthTipRepository.findByOrderByPhaseAscPriorityDesc();
    }

    @Override
    public List<HealthTip> getHealthTipsByPhase(String phase) {
        try {
            HealthTip.Phase phaseEnum = HealthTip.Phase.valueOf(phase.toUpperCase());
            return healthTipRepository.findByPhaseOrderByPriorityDesc(phaseEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid phase: " + phase);
        }
    }

    @Override
    public List<HealthTip> getHealthTipsByCategory(String category) {
        try {
            HealthTip.Category categoryEnum = HealthTip.Category.valueOf(category.toUpperCase());
            return healthTipRepository.findByCategoryOrderByPriorityDesc(categoryEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
}