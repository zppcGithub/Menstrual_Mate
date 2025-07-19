package com.menstrualmate.service.impl;

import com.menstrualmate.dto.CalendarViewDTO;
import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.entity.CycleRecord;
import com.menstrualmate.entity.DailyRecord;
import com.menstrualmate.repository.CycleRecordRepository;
import com.menstrualmate.repository.DailyRecordRepository;
import com.menstrualmate.repository.SymptomRecordRepository;
import com.menstrualmate.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    private CycleRecordRepository cycleRecordRepository;

    @Autowired
    private DailyRecordRepository dailyRecordRepository;

    @Autowired
    private SymptomRecordRepository symptomRecordRepository;

    @Override
    public CalendarViewDTO getCalendarView(LocalDate yearMonth) {
        CalendarViewDTO view = new CalendarViewDTO();
        view.setMonth(yearMonth.getMonth().toString());
        view.setYear(yearMonth.getYear());

        YearMonth yearMonthObj = YearMonth.of(yearMonth.getYear(), yearMonth.getMonth());
        LocalDate startDate = yearMonthObj.atDay(1);
        LocalDate endDate = yearMonthObj.atEndOfMonth();

        List<CalendarViewDTO.CalendarDayDTO> days = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            CalendarViewDTO.CalendarDayDTO day = new CalendarViewDTO.CalendarDayDTO();
            day.setDate(date);
            day.setToday(date.equals(today));
            day.setPhase(determinePhase(date));
            
            dailyRecordRepository.findByRecordDate(date).ifPresent(record -> {
                if (record.getMenstrualFlow() != null) {
                    day.setMenstrualFlow(record.getMenstrualFlow().name());
                }
            });
            
            days.add(day);
        }

        view.setDays(days);
        view.setCycles(cycleRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));

        return view;
    }

    @Override
    public Map<String, Object> getDailyEvents(LocalDate date) {
        // This logic can be part of getCalendarView or a separate implementation.
        return null;
    }

    @Override
    public Map<String, Object> getAnalysisData(LocalDate startDate, LocalDate endDate) {
        // This can be a combination of the other analysis methods.
        return null;
    }

    @Override
    public Map<String, Object> getAnalysisTrends() {
        Map<String, Object> trends = new HashMap<>();
        List<CycleRecord> records = cycleRecordRepository.findAllByOrderByStartDateAsc();

        // Cycle Length Trend
        Map<String, Object> cycleLengthTrend = new HashMap<>();
        cycleLengthTrend.put("labels", records.stream()
                .map(r -> r.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .collect(Collectors.toList()));
        cycleLengthTrend.put("values", records.stream()
                .map(CycleRecord::getCycleLength)
                .collect(Collectors.toList()));
        trends.put("cycle_length_trend", cycleLengthTrend);

        // Period Length Analysis
        Map<String, Object> periodLengthAnalysis = new HashMap<>();
        periodLengthAnalysis.put("labels", records.stream()
                .map(r -> r.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .collect(Collectors.toList()));
        periodLengthAnalysis.put("values", records.stream()
                .map(CycleRecord::getPeriodLength)
                .collect(Collectors.toList()));
        trends.put("period_length_analysis", periodLengthAnalysis);

        return trends;
    }

    @Override
    public Map<String, Object> getSymptomFrequency() {
        Map<String, Object> frequency = new HashMap<>();
        List<Map<String, Object>> symptomStats = symptomRecordRepository.findSymptomFrequency();

        frequency.put("symptom_frequency", symptomStats.stream()
                .map(stat -> {
                    Map<String, Object> formattedStat = new HashMap<>();
                    formattedStat.put("name", stat.get("name"));
                    formattedStat.put("value", stat.get("count"));
                    return formattedStat;
                })
                .collect(Collectors.toList()));
        return frequency;
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

    private String determinePhase(LocalDate date) {
        List<CycleRecord> cycles = cycleRecordRepository.findAll();
        if (cycles.isEmpty()) return "UNKNOWN";

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
}