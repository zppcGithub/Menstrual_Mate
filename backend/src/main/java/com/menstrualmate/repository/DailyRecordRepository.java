package com.menstrualmate.repository;

import com.menstrualmate.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    
    Optional<DailyRecord> findByRecordDate(LocalDate recordDate);
    
    List<DailyRecord> findByRecordDateBetweenOrderByRecordDate(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT dr FROM DailyRecord dr WHERE dr.menstrualFlow IS NOT NULL ORDER BY dr.recordDate DESC")
    List<DailyRecord> findDaysWithMenstrualFlow();
    
    @Query("SELECT dr FROM DailyRecord dr WHERE dr.recordDate >= :startDate AND dr.menstrualFlow IS NOT NULL ORDER BY dr.recordDate")
    List<DailyRecord> findMenstrualDaysFromDate(@Param("startDate") LocalDate startDate);
    
    @Query("SELECT MIN(dr.recordDate) FROM DailyRecord dr WHERE dr.menstrualFlow IS NOT NULL")
    LocalDate findFirstMenstrualRecordDate();
}