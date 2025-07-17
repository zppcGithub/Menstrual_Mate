package com.menstrualmate.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "symptom_records")
@Data
public class SymptomRecord {
    @EmbeddedId
    private SymptomRecordId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dailyRecordId")
    @JoinColumn(name = "daily_record_id")
    private DailyRecord dailyRecord;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("symptomId")
    @JoinColumn(name = "symptom_id")
    private Symptom symptom;
    
    @Column(name = "intensity")
    private Integer intensity = 1;
}

