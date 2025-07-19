
package com.menstrualmate.controller;

import com.menstrualmate.dto.ApiResponse;
import com.menstrualmate.entity.Symptom;
import com.menstrualmate.repository.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@CrossOrigin(origins = "*")
public class SymptomController {

    @Autowired
    private SymptomRepository symptomRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Symptom>>> getAllSymptoms() {
        List<Symptom> symptoms = symptomRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(symptoms));
    }
}
