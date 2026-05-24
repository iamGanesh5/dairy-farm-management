package com.dairy.farm.management.controllerTest;

import com.dairy.farm.management.controller.MilkEntryController;
import com.dairy.farm.management.dto.MonthlyOwnerMilkReportDTO;
import com.dairy.farm.management.dto.OwnerSummaryDTO;
import com.dairy.farm.management.entity.MilkEntry;
import com.dairy.farm.management.service.MilkEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Unit test class for MilkEntryController.
 */

@WebMvcTest(MilkEntryController.class)
public class MilkEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MilkEntryService milkEntryService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Test get all milk entries API.
     */
    @Test
    void testGetAllMilkEntries() throws Exception {

        MilkEntry milkEntry = new MilkEntry();

        milkEntry.setId(1L);
        milkEntry.setMorningMilk(5.0);
        milkEntry.setEveningMilk(6.0);
        milkEntry.setTotalMilk(11.0);

        Mockito.when(
                        milkEntryService.getAllMilkEntries())
                .thenReturn(List.of(milkEntry));

        mockMvc.perform(
                        get("/api/milk-entries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalMilk")
                        .value(11.0));
    }

    /*
     * Test get milk entries by cow id API.
     */
    @Test
    void testGetMilkEntriesByCowId()
            throws Exception {

        MilkEntry milkEntry = new MilkEntry();

        milkEntry.setId(1L);
        milkEntry.setTotalMilk(10.0);

        Mockito.when(
                        milkEntryService
                                .getMilkEntriesByCowId(1L))
                .thenReturn(List.of(milkEntry));

        mockMvc.perform(
                        get("/api/milk-entries/cow/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalMilk")
                        .value(10.0));
    }

    /*
     * Test calculate payment API.
     */
    @Test
    void testCalculatePayment()
            throws Exception {

        Mockito.when(
                        milkEntryService.calculatePayment(
                                any(),
                                any(),
                                any()))
                .thenReturn(5000.0);

        mockMvc.perform(
                        get("/api/milk-entries/payment")
                                .param("startDate",
                                        "2026-05-01")
                                .param("endDate",
                                        "2026-05-31")
                                .param("pricePerLiter",
                                        "35"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("5000.0"));
    }

    /*
     * Test owner summary API.
     */
    @Test
    void testGetOwnerSummaryReport()
            throws Exception {

        OwnerSummaryDTO dto =
                OwnerSummaryDTO.builder()
                        .ownerName("Ganesh")
                        .totalCows(2L)
                        .totalMilkLiters(300.0)
                        .totalAmount(9000.0)
                        .build();

        Mockito.when(
                        milkEntryService
                                .getOwnerSummaryReport())
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/milk-entries/owner-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ownerName")
                        .value("Ganesh"));
    }

    /*
     * Test monthly report API.
     */
    @Test
    void testMonthlyMilkReport()
            throws Exception {

        MonthlyOwnerMilkReportDTO dto =
                MonthlyOwnerMilkReportDTO.builder()
                        .ownerName("Ganesh")
                        .month("MAY")
                        .year(2026)
                        .totalMilkLiters(500.0)
                        .totalAmount(15000.0)
                        .build();

        Mockito.when(
                        milkEntryService
                                .getMonthlyMilkReportByOwner(
                                        any(),
                                        any(),
                                        any()))
                .thenReturn(dto);

        mockMvc.perform(
                        get("/api/milk-entries/monthly-report/Ganesh")
                                .param("startDate",
                                        "2026-05-01")
                                .param("endDate",
                                        "2026-05-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ownerName")
                        .value("Ganesh"));
    }
}