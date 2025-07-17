package com.menstrualmate.controller;

import com.menstrualmate.dto.ApiResponse;
import com.menstrualmate.entity.HealthTip;
import com.menstrualmate.service.HealthTipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tips")
@CrossOrigin(origins = "*")
public class HealthTipController {

    @Autowired
    private HealthTipService healthTipService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HealthTip>>> getAllHealthTips() {
        List<HealthTip> tips = healthTipService.getAllHealthTips();
        return ResponseEntity.ok(ApiResponse.success(tips));
    }

    @GetMapping("/phase/{phase}")
    public ResponseEntity<ApiResponse<List<HealthTip>>> getHealthTipsByPhase(@PathVariable String phase) {
        List<HealthTip> tips = healthTipService.getHealthTipsByPhase(phase);
        return ResponseEntity.ok(ApiResponse.success(tips));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<HealthTip>>> getHealthTipsByCategory(@PathVariable String category) {
        List<HealthTip> tips = healthTipService.getHealthTipsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(tips));
    }
}