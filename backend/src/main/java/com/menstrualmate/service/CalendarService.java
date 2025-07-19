package com.menstrualmate.service;

import com.menstrualmate.dto.CalendarViewDTO;

import java.time.LocalDate;
import java.util.Map;

public interface CalendarService {
    
    CalendarViewDTO getCalendarView(LocalDate yearMonth);
    
    Map<String, Object> getDailyEvents(LocalDate date);
    
    Map<String, Object> getAnalysisData(LocalDate startDate, LocalDate endDate);
    
    Map<String, Object> getAnalysisTrends();
    
    Map<String, Object> getSymptomFrequency();
}