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

@Embeddable
class SymptomRecordId implements java.io.Serializable {
    @Column(name = "daily_record_id")
    private Long dailyRecordId;
    
    @Column(name = "symptom_id")
    private Long symptomId;
    
    // Getters, setters, equals, and hashCode
    public Long getDailyRecordId() {
        return dailyRecordId;
    }
    
    public void setDailyRecordId(Long dailyRecordId) {
        this.dailyRecordId = dailyRecordId;
    }
    
    public Long getSymptomId() {
        return symptomId;
    }
    
    public void setSymptomId(Long symptomId) {
        this.symptomId = symptomId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymptomRecordId that = (SymptomRecordId) o;
        return java.util.Objects.equals(dailyRecordId, that.dailyRecordId) &&
               java.util.Objects.equals(symptomId, that.symptomId);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(dailyRecordId, symptomId);
    }
}