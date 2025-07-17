package com.menstrualmate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SymptomRecordId implements Serializable {
    
    @Column(name = "daily_record_id")
    private Long dailyRecordId;
    
    @Column(name = "symptom_id")
    private Long symptomId;
    
    // Default constructor
    public SymptomRecordId() {
    }
    
    public SymptomRecordId(Long dailyRecordId, Long symptomId) {
        this.dailyRecordId = dailyRecordId;
        this.symptomId = symptomId;
    }
    
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
        return Objects.equals(dailyRecordId, that.dailyRecordId) &&
                Objects.equals(symptomId, that.symptomId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dailyRecordId, symptomId);
    }
}