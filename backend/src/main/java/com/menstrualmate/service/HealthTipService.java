package com.menstrualmate.service;

import com.menstrualmate.entity.HealthTip;
import java.util.List;

public interface HealthTipService {
    List<HealthTip> getAllHealthTips();
    List<HealthTip> getHealthTipsByPhase(String phase);
    List<HealthTip> getHealthTipsByCategory(String category);
}