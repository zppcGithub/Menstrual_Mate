package com.menstrualmate.controller;

import com.menstrualmate.dto.ApiResponse;
import com.menstrualmate.dto.CalendarViewDTO;
import com.menstrualmate.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/view")
    public ResponseEntity<ApiResponse<CalendarViewDTO>> getCalendarView(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate yearMonth) {
        CalendarViewDTO view = calendarService.getCalendarView(yearMonth);
        return ResponseEntity.ok(ApiResponse.success(view));
    }

    @GetMapping("/events/{date}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDailyEvents(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Object> events = calendarService.getDailyEvents(date);
        return ResponseEntity.ok(ApiResponse.success(events));
    }

    @GetMapping("/analysis")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalysisData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> analysis = calendarService.getAnalysisData(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(analysis));
    }
}