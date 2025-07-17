package com.menstrualmate.service;

import com.menstrualmate.dto.DailyRecordDTO;
import java.time.LocalDate;
import java.util.List;

public interface DailyRecordService {
    List<DailyRecordDTO> getAllDailyRecords();
    DailyRecordDTO getDailyRecordById(Long id);
    DailyRecordDTO getDailyRecordByDate(LocalDate date);
    DailyRecordDTO createDailyRecord(DailyRecordDTO dailyRecordDTO);
    DailyRecordDTO updateDailyRecord(Long id, DailyRecordDTO dailyRecordDTO);
    void deleteDailyRecord(Long id);
    List<DailyRecordDTO> getDailyRecordsByDateRange(LocalDate startDate, LocalDate endDate);
}