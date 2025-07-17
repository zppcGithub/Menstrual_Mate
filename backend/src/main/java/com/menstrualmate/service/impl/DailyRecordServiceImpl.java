package com.menstrualmate.service.impl;

import com.menstrualmate.dto.DailyRecordDTO;
import com.menstrualmate.entity.DailyRecord;
import com.menstrualmate.exception.ResourceNotFoundException;
import com.menstrualmate.repository.DailyRecordRepository;
import com.menstrualmate.service.DailyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyRecordServiceImpl implements DailyRecordService {

    @Autowired
    private DailyRecordRepository dailyRecordRepository;

    @Override
    public List<DailyRecordDTO> getAllDailyRecords() {
        return dailyRecordRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DailyRecordDTO getDailyRecordById(Long id) {
        DailyRecord record = dailyRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DailyRecord", "id", id));
        return convertToDTO(record);
    }

    @Override
    public DailyRecordDTO getDailyRecordByDate(LocalDate date) {
        return dailyRecordRepository.findByRecordDate(date)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public DailyRecordDTO createDailyRecord(DailyRecordDTO dailyRecordDTO) {
        DailyRecord dailyRecord = convertToEntity(dailyRecordDTO);
        DailyRecord saved = dailyRecordRepository.save(dailyRecord);
        return convertToDTO(saved);
    }

    @Override
    public DailyRecordDTO updateDailyRecord(Long id, DailyRecordDTO dailyRecordDTO) {
        DailyRecord existingRecord = dailyRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DailyRecord", "id", id));
        
        existingRecord.setRecordDate(dailyRecordDTO.getRecordDate());
        existingRecord.setMenstrualFlow(DailyRecord.MenstrualFlow.valueOf(dailyRecordDTO.getMenstrualFlow()));
        existingRecord.setTemperature(dailyRecordDTO.getTemperature());
        existingRecord.setWeight(dailyRecordDTO.getWeight());
        existingRecord.setNotes(dailyRecordDTO.getNotes());
        
        DailyRecord updated = dailyRecordRepository.save(existingRecord);
        return convertToDTO(updated);
    }

    @Override
    public void deleteDailyRecord(Long id) {
        DailyRecord record = dailyRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DailyRecord", "id", id));
        dailyRecordRepository.delete(record);
    }

    @Override
    public List<DailyRecordDTO> getDailyRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        return dailyRecordRepository.findByRecordDateBetweenOrderByRecordDate(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DailyRecordDTO convertToDTO(DailyRecord dailyRecord) {
        DailyRecordDTO dto = new DailyRecordDTO();
        dto.setId(dailyRecord.getId());
        dto.setRecordDate(dailyRecord.getRecordDate());
        dto.setMenstrualFlow(dailyRecord.getMenstrualFlow() != null ? dailyRecord.getMenstrualFlow().name() : null);
        dto.setTemperature(dailyRecord.getTemperature());
        dto.setWeight(dailyRecord.getWeight());
        dto.setNotes(dailyRecord.getNotes());
        return dto;
    }

    private DailyRecord convertToEntity(DailyRecordDTO dto) {
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setRecordDate(dto.getRecordDate());
        if (dto.getMenstrualFlow() != null) {
            dailyRecord.setMenstrualFlow(DailyRecord.MenstrualFlow.valueOf(dto.getMenstrualFlow()));
        }
        dailyRecord.setTemperature(dto.getTemperature());
        dailyRecord.setWeight(dto.getWeight());
        dailyRecord.setNotes(dto.getNotes());
        return dailyRecord;
    }
}