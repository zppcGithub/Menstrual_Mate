package com.menstrualmate.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.menstrualmate.dto.CycleRecordDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class CycleRecordIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndGetCycleRecord() throws Exception {
        CycleRecordDTO cycleRecordDTO = new CycleRecordDTO();
        cycleRecordDTO.setStartDate(LocalDate.of(2024, 1, 1));
        cycleRecordDTO.setEndDate(LocalDate.of(2024, 1, 5));
        cycleRecordDTO.setCycleLength(28);
        cycleRecordDTO.setPeriodLength(5);

        // 创建周期记录
        String response = mockMvc.perform(post("/api/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cycleRecordDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").exists())
                .andReturn().getResponse().getContentAsString();

        // 提取创建的ID
        Long createdId = objectMapper.readTree(response).path("data").path("id").asLong();

        // 获取周期记录
        mockMvc.perform(get("/api/cycles/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.startDate").value("2024-01-01"))
                .andExpect(jsonPath("$.data.endDate").value("2024-01-05"));
    }

    @Test
    void testGetAllCycleRecords() throws Exception {
        // 创建测试数据
        for (int i = 1; i <= 3; i++) {
            CycleRecordDTO dto = new CycleRecordDTO();
            dto.setStartDate(LocalDate.of(2024, i, 1));
            dto.setEndDate(LocalDate.of(2024, i, 5));
            dto.setCycleLength(28);
            dto.setPeriodLength(5);

            mockMvc.perform(post("/api/cycles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        // 获取所有记录
        mockMvc.perform(get("/api/cycles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void testUpdateCycleRecord() throws Exception {
        CycleRecordDTO cycleRecordDTO = new CycleRecordDTO();
        cycleRecordDTO.setStartDate(LocalDate.of(2024, 1, 1));
        cycleRecordDTO.setEndDate(LocalDate.of(2024, 1, 5));
        cycleRecordDTO.setCycleLength(28);
        cycleRecordDTO.setPeriodLength(5);

        // 创建记录
        String response = mockMvc.perform(post("/api/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cycleRecordDTO)))
                .andReturn().getResponse().getContentAsString();

        Long createdId = objectMapper.readTree(response).path("data").path("id").asLong();

        // 更新记录
        CycleRecordDTO updatedDTO = new CycleRecordDTO();
        updatedDTO.setStartDate(LocalDate.of(2024, 2, 1));
        updatedDTO.setEndDate(LocalDate.of(2024, 2, 6));
        updatedDTO.setCycleLength(30);
        updatedDTO.setPeriodLength(6);

        mockMvc.perform(put("/api/cycles/" + createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.startDate").value("2024-02-01"));
    }

    @Test
    void testDeleteCycleRecord() throws Exception {
        CycleRecordDTO cycleRecordDTO = new CycleRecordDTO();
        cycleRecordDTO.setStartDate(LocalDate.of(2024, 1, 1));
        cycleRecordDTO.setEndDate(LocalDate.of(2024, 1, 5));
        cycleRecordDTO.setCycleLength(28);
        cycleRecordDTO.setPeriodLength(5);

        String response = mockMvc.perform(post("/api/cycles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cycleRecordDTO)))
                .andReturn().getResponse().getContentAsString();

        Long createdId = objectMapper.readTree(response).path("data").path("id").asLong();

        mockMvc.perform(delete("/api/cycles/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/api/cycles/" + createdId))
                .andExpect(status().isNotFound());
    }
}