package com.menstrualmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "symptoms")
@Data
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private SymptomType type;
    
    @Column(name = "icon", length = 50)
    private String icon;
    
    @Column(name = "color", length = 7)
    private String color = "#666666";
    
    @Column(name = "is_custom")
    private Boolean isCustom = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "symptom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SymptomRecord> symptomRecords;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum SymptomType {
        SYMPTOM, MOOD, ACTIVITY
    }
}