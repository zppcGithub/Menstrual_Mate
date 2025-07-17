package com.menstrualmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_tips")
@Data
public class HealthTip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "phase", nullable = false, length = 20)
    private Phase phase;
    
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 20)
    private Category category;
    
    @Column(name = "priority")
    private Integer priority = 1;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public enum Phase {
        MENSTRUAL, FOLLICULAR, OVULATION, LUTEAL
    }
    
    public enum Category {
        HEALTH, DIET, EXERCISE, SKINCARE
    }
}