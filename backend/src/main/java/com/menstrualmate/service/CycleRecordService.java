package com.menstrualmate.service;

import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.entity.CycleRecord;
import java.util.List;

public interface CycleRecordService {
    List<CycleRecordDTO> getAllCycleRecords();
    CycleRecordDTO getCycleRecordById(Long id);
    CycleRecordDTO createCycleRecord(CycleRecordDTO cycleRecordDTO);
    CycleRecordDTO updateCycleRecord(Long id, CycleRecordDTO cycleRecordDTO);
    void deleteCycleRecord(Long id);
    List<CycleRecordDTO> getCycleRecordsByDateRange(String startDate, String endDate);
    CycleRecordDTO getLatestCycleRecord();
    CycleRecordDTO predictNextCycle();
}