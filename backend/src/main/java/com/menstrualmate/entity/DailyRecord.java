package com.menstrualmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "daily_records")
@Data
public class DailyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "record_date", unique = true, nullable = false)
    private LocalDate recordDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "menstrual_flow", length = 20)
    private MenstrualFlow menstrualFlow;
    
    @Column(name = "temperature", precision = 4, scale = 1)
    private BigDecimal temperature;
    
    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "dailyRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SymptomRecord> symptomRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum MenstrualFlow {
        SPOTTING, LIGHT, MEDIUM, HEAVY
    }
}