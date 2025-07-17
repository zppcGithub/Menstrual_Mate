package com.menstrualmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.menstrualmate.dto.CycleRecordDTO;
import com.menstrualmate.service.CycleRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CycleRecordController.class)
public class CycleRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CycleRecordService cycleRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    private CycleRecordDTO testCycleRecord;

    @BeforeEach
    void setUp() {
        testCycleRecord = new CycleRecordDTO();
        testCycleRecord.setId(1L);
        testCycleRecord.setStartDate(LocalDate.of(2024, 1, 1));
        testCycleRecord.setEndDate(LocalDate.of(2024, 1, 5));
        testCycleRecord.setCycleLength(28);
        testCycleRecord.setPeriodLength(5);
    }

    @Test
    void getAllCycleRecords_ShouldReturnList() throws Exception {
        List<CycleRecordDTO> cycles = Arrays.asList(testCycleRecord);
        when(cycleRecordService.getAllCycleRecords()).thenReturn(cycles);

        mockMvc.perform(get("/api/cycles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1L));

        verify(cycleRecordService, times(1)).getAllCycleRecords();
    }

    @Test
    void createCycleRecord_ShouldReturnCreated() throws Exception {
        when(cycleRecordService.createCycleRecord(any(CycleRecordDTO.class))).thenReturn(testCycleRecord);

        mockMvc.perform(post("/api/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCycleRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L));

        verify(cycleRecordService, times(1)).createCycleRecord(any(CycleRecordDTO.class));
    }

    @Test
    void updateCycleRecord_ShouldReturnUpdated() throws Exception {
        when(cycleRecordService.updateCycleRecord(anyLong(), any(CycleRecordDTO.class))).thenReturn(testCycleRecord);

        mockMvc.perform(put("/api/cycles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCycleRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L));

        verify(cycleRecordService, times(1)).updateCycleRecord(anyLong(), any(CycleRecordDTO.class));
    }

    @Test
    void deleteCycleRecord_ShouldReturnSuccess() throws Exception {
        doNothing().when(cycleRecordService).deleteCycleRecord(anyLong());

        mockMvc.perform(delete("/api/cycles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(cycleRecordService, times(1)).deleteCycleRecord(anyLong());
    }
}