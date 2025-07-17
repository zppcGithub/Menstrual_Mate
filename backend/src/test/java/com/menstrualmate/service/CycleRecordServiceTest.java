package com.menstrualmate.service;

import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.entity.CycleRecord;
import com.menstrualmate.entity.User;
import com.menstrualmate.exception.ResourceNotFoundException;
import com.menstrualmate.repository.CycleRecordRepository;
import com.menstrualmate.repository.UserRepository;
import com.menstrualmate.service.impl.CycleRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CycleRecordServiceTest {

    @Mock
    private CycleRecordRepository cycleRecordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CycleRecordServiceImpl cycleRecordService;

    private User testUser;
    private CycleRecord testCycleRecord;
    private CycleRecordDTO testCycleRecordDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testCycleRecord = new CycleRecord();
        testCycleRecord.setId(1L);
        testCycleRecord.setStartDate(LocalDate.of(2024, 1, 1));
        testCycleRecord.setEndDate(LocalDate.of(2024, 1, 5));
        testCycleRecord.setCycleLength(28);
        testCycleRecord.setPeriodLength(5);
        testCycleRecord.setUser(testUser);

        testCycleRecordDTO = new CycleRecordDTO();
        testCycleRecordDTO.setStartDate(LocalDate.of(2024, 1, 1));
        testCycleRecordDTO.setEndDate(LocalDate.of(2024, 1, 5));
        testCycleRecordDTO.setCycleLength(28);
        testCycleRecordDTO.setPeriodLength(5);
    }

    @Test
    void getAllCycleRecords_ShouldReturnList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cycleRecordRepository.findByUserOrderByStartDateDesc(testUser))
                .thenReturn(Arrays.asList(testCycleRecord));

        List<CycleRecordDTO> result = cycleRecordService.getAllCycleRecords();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(cycleRecordRepository, times(1)).findByUserOrderByStartDateDesc(testUser);
    }

    @Test
    void getCycleRecordById_ShouldReturnRecord() {
        when(cycleRecordRepository.findById(1L)).thenReturn(Optional.of(testCycleRecord));

        CycleRecordDTO result = cycleRecordService.getCycleRecordById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cycleRecordRepository, times(1)).findById(1L);
    }

    @Test
    void getCycleRecordById_ShouldThrowException_WhenNotFound() {
        when(cycleRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cycleRecordService.getCycleRecordById(1L);
        });
    }

    @Test
    void createCycleRecord_ShouldCreateAndReturn() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cycleRecordRepository.save(any(CycleRecord.class))).thenReturn(testCycleRecord);

        CycleRecordDTO result = cycleRecordService.createCycleRecord(testCycleRecordDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cycleRecordRepository, times(1)).save(any(CycleRecord.class));
    }

    @Test
    void updateCycleRecord_ShouldUpdateAndReturn() {
        when(cycleRecordRepository.findById(1L)).thenReturn(Optional.of(testCycleRecord));
        when(cycleRecordRepository.save(any(CycleRecord.class))).thenReturn(testCycleRecord);

        CycleRecordDTO result = cycleRecordService.updateCycleRecord(1L, testCycleRecordDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cycleRecordRepository, times(1)).findById(1L);
        verify(cycleRecordRepository, times(1)).save(any(CycleRecord.class));
    }

    @Test
    void deleteCycleRecord_ShouldDelete() {
        when(cycleRecordRepository.findById(1L)).thenReturn(Optional.of(testCycleRecord));
        doNothing().when(cycleRecordRepository).delete(testCycleRecord);

        assertDoesNotThrow(() -> cycleRecordService.deleteCycleRecord(1L));
        verify(cycleRecordRepository, times(1)).delete(testCycleRecord);
    }

    @Test
    void predictNextCycle_ShouldReturnPrediction() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cycleRecordRepository.findAverageCycleLength(1L)).thenReturn(28.0);
        when(cycleRecordRepository.findFirstByUserOrderByStartDateDesc(testUser)).thenReturn(testCycleRecord);
        when(cycleRecordRepository.findAveragePeriodLength(1L)).thenReturn(5.0);

        CycleRecordDTO result = cycleRecordService.predictNextCycle();

        assertNotNull(result);
        assertEquals(LocalDate.of(2024, 1, 29), result.getStartDate());
        assertEquals(28, result.getCycleLength());
        assertEquals(5, result.getPeriodLength());
    }
}