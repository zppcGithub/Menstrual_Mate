package com.menstrualmate.controller;

import com.menstrualmate.dto.ApiResponse;
import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.service.CycleRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycles")
@CrossOrigin(origins = "*")
public class CycleRecordController {

    @Autowired
    private CycleRecordService cycleRecordService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CycleRecordDTO>>> getAllCycleRecords() {
        List<CycleRecordDTO> cycles = cycleRecordService.getAllCycleRecords();
        return ResponseEntity.ok(ApiResponse.success(cycles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CycleRecordDTO>> getCycleRecordById(@PathVariable Long id) {
        CycleRecordDTO cycle = cycleRecordService.getCycleRecordById(id);
        return ResponseEntity.ok(ApiResponse.success(cycle));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CycleRecordDTO>> createCycleRecord(@Valid @RequestBody CycleRecordDTO cycleRecordDTO) {
        CycleRecordDTO created = cycleRecordService.createCycleRecord(cycleRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cycle record created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CycleRecordDTO>> updateCycleRecord(@PathVariable Long id, @Valid @RequestBody CycleRecordDTO cycleRecordDTO) {
        CycleRecordDTO updated = cycleRecordService.updateCycleRecord(id, cycleRecordDTO);
        return ResponseEntity.ok(ApiResponse.success("Cycle record updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCycleRecord(@PathVariable Long id) {
        cycleRecordService.deleteCycleRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Cycle record deleted successfully", null));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<CycleRecordDTO>> getLatestCycleRecord() {
        CycleRecordDTO latest = cycleRecordService.getLatestCycleRecord();
        return ResponseEntity.ok(ApiResponse.success(latest));
    }

    @GetMapping("/predict")
    public ResponseEntity<ApiResponse<CycleRecordDTO>> predictNextCycle() {
        CycleRecordDTO prediction = cycleRecordService.predictNextCycle();
        return ResponseEntity.ok(ApiResponse.success(prediction));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<CycleRecordDTO>>> getCycleRecordsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<CycleRecordDTO> cycles = cycleRecordService.getCycleRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(cycles));
    }
}