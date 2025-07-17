package com.menstrualmate.service.impl;

import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.entity.CycleRecord;
import com.menstrualmate.entity.User;
import com.menstrualmate.exception.ResourceNotFoundException;
import com.menstrualmate.repository.CycleRecordRepository;
import com.menstrualmate.repository.UserRepository;
import com.menstrualmate.service.CycleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CycleRecordServiceImpl implements CycleRecordService {

    @Autowired
    private CycleRecordRepository cycleRecordRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Long DEFAULT_USER_ID = 1L;

    @Override
    public List<CycleRecordDTO> getAllCycleRecords() {
        User user = getDefaultUser();
        return cycleRecordRepository.findByUserOrderByStartDateDesc(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CycleRecordDTO getCycleRecordById(Long id) {
        CycleRecord cycleRecord = cycleRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CycleRecord", "id", id));
        return convertToDTO(cycleRecord);
    }

    @Override
    public CycleRecordDTO createCycleRecord(CycleRecordDTO cycleRecordDTO) {
        CycleRecord cycleRecord = convertToEntity(cycleRecordDTO);
        cycleRecord.setUser(getDefaultUser());
        
        // 自动计算周期长度和经期长度
        if (cycleRecord.getEndDate() != null) {
            cycleRecord.setPeriodLength(
                (int) java.time.temporal.ChronoUnit.DAYS.between(
                    cycleRecord.getStartDate(), cycleRecord.getEndDate()) + 1
            );
        }
        
        // 计算上一个周期的长度
        CycleRecord lastRecord = cycleRecordRepository.findFirstByUserOrderByStartDateDesc(getDefaultUser());
        if (lastRecord != null) {
            cycleRecord.setCycleLength(
                (int) java.time.temporal.ChronoUnit.DAYS.between(
                    lastRecord.getStartDate(), cycleRecord.getStartDate())
            );
        }
        
        CycleRecord saved = cycleRecordRepository.save(cycleRecord);
        return convertToDTO(saved);
    }

    @Override
    public CycleRecordDTO updateCycleRecord(Long id, CycleRecordDTO cycleRecordDTO) {
        CycleRecord existingRecord = cycleRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CycleRecord", "id", id));
        
        existingRecord.setStartDate(cycleRecordDTO.getStartDate());
        existingRecord.setEndDate(cycleRecordDTO.getEndDate());
        existingRecord.setCycleLength(cycleRecordDTO.getCycleLength());
        existingRecord.setPeriodLength(cycleRecordDTO.getPeriodLength());
        
        CycleRecord updated = cycleRecordRepository.save(existingRecord);
        return convertToDTO(updated);
    }

    @Override
    public void deleteCycleRecord(Long id) {
        CycleRecord cycleRecord = cycleRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CycleRecord", "id", id));
        cycleRecordRepository.delete(cycleRecord);
    }

    @Override
    public List<CycleRecordDTO> getCycleRecordsByDateRange(String startDate, String endDate) {
        User user = getDefaultUser();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        return cycleRecordRepository.findByUserAndStartDateBetweenOrderByStartDate(user, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CycleRecordDTO getLatestCycleRecord() {
        User user = getDefaultUser();
        CycleRecord latest = cycleRecordRepository.findFirstByUserOrderByStartDateDesc(user);
        return latest != null ? convertToDTO(latest) : null;
    }

    @Override
    public CycleRecordDTO predictNextCycle() {
        User user = getDefaultUser();
        Double avgCycleLength = cycleRecordRepository.findAverageCycleLength(user.getId());
        
        if (avgCycleLength == null) {
            return null;
        }
        
        CycleRecord latestRecord = cycleRecordRepository.findFirstByUserOrderByStartDateDesc(user);
        if (latestRecord == null) {
            return null;
        }
        
        CycleRecordDTO prediction = new CycleRecordDTO();
        prediction.setStartDate(latestRecord.getStartDate().plusDays(avgCycleLength.longValue()));
        prediction.setCycleLength(avgCycleLength.intValue());
        
        Double avgPeriodLength = cycleRecordRepository.findAveragePeriodLength(user.getId());
        if (avgPeriodLength != null) {
            prediction.setPeriodLength(avgPeriodLength.intValue());
            prediction.setEndDate(prediction.getStartDate().plusDays(avgPeriodLength.intValue() - 1));
        }
        
        return prediction;
    }

    private User getDefaultUser() {
        return userRepository.findById(DEFAULT_USER_ID)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", DEFAULT_USER_ID));
    }

    private CycleRecordDTO convertToDTO(CycleRecord cycleRecord) {
        CycleRecordDTO dto = new CycleRecordDTO();
        dto.setId(cycleRecord.getId());
        dto.setStartDate(cycleRecord.getStartDate());
        dto.setEndDate(cycleRecord.getEndDate());
        dto.setCycleLength(cycleRecord.getCycleLength());
        dto.setPeriodLength(cycleRecord.getPeriodLength());
        return dto;
    }

    private CycleRecord convertToEntity(CycleRecordDTO dto) {
        CycleRecord cycleRecord = new CycleRecord();
        cycleRecord.setStartDate(dto.getStartDate());
        cycleRecord.setEndDate(dto.getEndDate());
        cycleRecord.setCycleLength(dto.getCycleLength());
        cycleRecord.setPeriodLength(dto.getPeriodLength());
        return cycleRecord;
    }
}