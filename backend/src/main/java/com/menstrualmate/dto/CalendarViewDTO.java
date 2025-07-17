package com.menstrualmate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class CalendarViewDTO {
    private String month;
    private int year;
    private List<CalendarDayDTO> days;
    private List<CycleRecordDTO> cycles;
    
    @Data
    public static class CalendarDayDTO {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        private String phase;
        private String menstrualFlow;
        private List<String> symptoms;
        private boolean isPredicted;
        private boolean isToday;
    }
}