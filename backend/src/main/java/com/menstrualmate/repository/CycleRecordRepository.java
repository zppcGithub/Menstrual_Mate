package com.menstrualmate.repository;

import com.menstrualmate.entity.CycleRecord;
import com.menstrualmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CycleRecordRepository extends JpaRepository<CycleRecord, Long> {
    List<CycleRecord> findByUserOrderByStartDateDesc(User user);
    
    List<CycleRecord> findByUserAndStartDateBetweenOrderByStartDate(User user, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT cr FROM CycleRecord cr WHERE cr.user.id = :userId AND cr.startDate <= :date AND (cr.endDate IS NULL OR cr.endDate >= :date)")
    CycleRecord findActiveCycleForDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Query("SELECT AVG(cr.cycleLength) FROM CycleRecord cr WHERE cr.user.id = :userId AND cr.cycleLength IS NOT NULL")
    Double findAverageCycleLength(@Param("userId") Long userId);
    
    @Query("SELECT AVG(cr.periodLength) FROM CycleRecord cr WHERE cr.user.id = :userId AND cr.periodLength IS NOT NULL")
    Double findAveragePeriodLength(@Param("userId") Long userId);
    
    CycleRecord findFirstByUserOrderByStartDateDesc(User user);
}