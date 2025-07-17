package com.menstrualmate.service;

import com.menstrualmate.dto.CalendarViewDTO;
import com.menstrualmate.dto.CalendarViewDTO.CalendarDayDTO;
import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.entity.CycleRecord;
import com.menstrualmate.entity.DailyRecord;
import com.menstrualmate.repository.CycleRecordRepository;
import com.menstrualmate.repository.DailyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    @Autowired
    private CycleRecordRepository cycleRecordRepository;

    @Autowired
    private DailyRecordRepository dailyRecordRepository;

    public CalendarViewDTO getCalendarView(LocalDate yearMonth) {
        CalendarViewDTO view = new CalendarViewDTO();
        view.setMonth(yearMonth.getMonth().toString());
        view.setYear(yearMonth.getYear());

        YearMonth yearMonthObj = YearMonth.of(yearMonth.getYear(), yearMonth.getMonth());
        LocalDate startDate = yearMonthObj.atDay(1);
        LocalDate endDate = yearMonthObj.atEndOfMonth();

        List<CalendarDayDTO> days = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            CalendarDayDTO day = new CalendarDayDTO();
            day.setDate(date);
            day.setToday(date.equals(today));
            day.setPhase(determinePhase(date));
            
            dailyRecordRepository.findByRecordDate(date).ifPresent(record -> {
                day.setMenstrualFlow(record.getMenstrualFlow() != null ? record.getMenstrualFlow().name() : null);
            });
            
            days.add(day);
        }

        view.setDays(days);
        view.setCycles(cycleRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        return view;
    }

    public Map<String, Object> getDailyEvents(LocalDate date) {
        Map<String, Object> events = new HashMap<>();
        
        dailyRecordRepository.findByRecordDate(date).ifPresent(record -> {
            events.put("dailyRecord", record);
            events.put("phase", determinePhase(date));
        });
        
        return events;
    }

    public Map<String, Object> getAnalysisData(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> analysis = new HashMap<>();
        
        List<CycleRecord> cycles = cycleRecordRepository.findAll();
        List<DailyRecord> dailyRecords = dailyRecordRepository.findByRecordDateBetweenOrderByRecordDate(startDate, endDate);
        
        analysis.put("totalCycles", cycles.size());
        analysis.put("averageCycleLength", calculateAverageCycleLength(cycles));
        analysis.put("averagePeriodLength", calculateAveragePeriodLength(cycles));
        analysis.put("dailyRecordsCount", dailyRecords.size());
        
        return analysis;
    }

    private String determinePhase(LocalDate date) {
        List<CycleRecord> cycles = cycleRecordRepository.findAll();
        if (cycles.isEmpty()) return "UNKNOWN";

        CycleRecord latestCycle = cycles.get(0);
        for (CycleRecord cycle : cycles) {
            if (date.isEqual(cycle.getStartDate()) || date.isAfter(cycle.getStartDate())) {
                if (cycle.getEndDate() == null || !date.isAfter(cycle.getEndDate())) {
                    return "MENSTRUAL";
                }
            }
        }
        
        return "FOLLICULAR";
    }

    private Double calculateAverageCycleLength(List<CycleRecord> cycles) {
        return cycles.stream()
                .mapToInt(CycleRecord::getCycleLength)
                .filter(length -> length > 0)
                .average()
                .orElse(0.0);
    }

    private Double calculateAveragePeriodLength(List<CycleRecord> cycles) {
        return cycles.stream()
                .mapToInt(CycleRecord::getPeriodLength)
                .filter(length -> length > 0)
                .average()
                .orElse(0.0);
    }

    private CycleRecordDTO convertToDTO(CycleRecord cycleRecord) {
        CycleRecordDTO dto = new CycleRecordDTO();
        dto.setId(cycleRecord.getId());
        dto.setStartDate(cycleRecord.getStartDate());
        dto.setEndDate(cycleRecord.getEndDate());
        dto.setCycleLength(cycleRecord.getCycleLength());
        dto.setPeriodLength(cycleRecord.getPeriodLength());
        return dto;
    }
}