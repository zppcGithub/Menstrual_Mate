package com.menstrualmate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DailyRecordDTO {
    private Long id;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;
    
    private String menstrualFlow;
    private BigDecimal temperature;
    private BigDecimal weight;
    private String notes;
    
    private List<SymptomRecordDTO> symptoms;
}

@Data
class SymptomRecordDTO {
    private Long symptomId;
    private String symptomName;
    private String symptomType;
    private Integer intensity;
    private String icon;
    private String color;
}