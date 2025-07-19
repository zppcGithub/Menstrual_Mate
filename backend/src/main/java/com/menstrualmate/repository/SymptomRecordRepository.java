package com.menstrualmate.repository;

import com.menstrualmate.entity.DailyRecord;
import com.menstrualmate.entity.Symptom;
import com.menstrualmate.entity.SymptomRecord;
import com.menstrualmate.entity.SymptomRecordId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface SymptomRecordRepository extends JpaRepository<SymptomRecord, SymptomRecordId> {
    
    List<SymptomRecord> findByDailyRecord(DailyRecord dailyRecord);
    
    List<SymptomRecord> findBySymptom(Symptom symptom);
    
    @Query("SELECT sr FROM SymptomRecord sr JOIN sr.dailyRecord dr WHERE dr.recordDate BETWEEN :startDate AND :endDate")
    List<SymptomRecord> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sr FROM SymptomRecord sr JOIN sr.dailyRecord dr JOIN sr.symptom s WHERE s.type = :type AND dr.recordDate BETWEEN :startDate AND :endDate")
    List<SymptomRecord> findBySymptomTypeAndDateRange(@Param("type") Symptom.SymptomType type, 
                                                     @Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT s.name as name, COUNT(sr) as count FROM SymptomRecord sr JOIN sr.symptom s GROUP BY s.id, s.name ORDER BY count DESC")
    List<Map<String, Object>> findSymptomFrequency();
}