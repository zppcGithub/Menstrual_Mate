package com.menstrualmate.controller;

import com.menstrualmate.dto.ApiResponse;
import com.menstrualmate.dto.DailyRecordDTO;
import com.menstrualmate.service.DailyRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-records")
@CrossOrigin(origins = "*")
public class DailyRecordController {

    @Autowired
    private DailyRecordService dailyRecordService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DailyRecordDTO>>> getAllDailyRecords() {
        List<DailyRecordDTO> records = dailyRecordService.getAllDailyRecords();
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DailyRecordDTO>> getDailyRecordById(@PathVariable Long id) {
        DailyRecordDTO record = dailyRecordService.getDailyRecordById(id);
        return ResponseEntity.ok(ApiResponse.success(record));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ApiResponse<DailyRecordDTO>> getDailyRecordByDate(@PathVariable LocalDate date) {
        DailyRecordDTO record = dailyRecordService.getDailyRecordByDate(date);
        return ResponseEntity.ok(ApiResponse.success(record));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DailyRecordDTO>> createDailyRecord(@Valid @RequestBody DailyRecordDTO dailyRecordDTO) {
        DailyRecordDTO created = dailyRecordService.createDailyRecord(dailyRecordDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Daily record created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DailyRecordDTO>> updateDailyRecord(@PathVariable Long id, @Valid @RequestBody DailyRecordDTO dailyRecordDTO) {
        DailyRecordDTO updated = dailyRecordService.updateDailyRecord(id, dailyRecordDTO);
        return ResponseEntity.ok(ApiResponse.success("Daily record updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDailyRecord(@PathVariable Long id) {
        dailyRecordService.deleteDailyRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Daily record deleted successfully", null));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<DailyRecordDTO>>> getDailyRecordsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<DailyRecordDTO> records = dailyRecordService.getDailyRecordsByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(records));
    }
}