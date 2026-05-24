package com.dairy.farm.management.controller;

import com.dairy.farm.management.entity.Cow;
import com.dairy.farm.management.service.CowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Unit test class for CowController.
 */

@WebMvcTest(CowController.class)
public class CowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CowService cowService;

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Test add cow API.
     */
    @Test
    void testAddCow() throws Exception {

        Cow cow = Cow.builder()
                .id(1L)
                .cowName("Lakshmi")
                .breed("Jersey")
                .age(5)
                .tagNumber("COW101")
                .build();

        Mockito.when(cowService.addCow(any(Cow.class)))
                .thenReturn(cow);

        mockMvc.perform(post("/api/cows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cow)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cowName")
                        .value("Lakshmi"));
    }

    /*
     * Test get all cows API.
     */
    @Test
    void testGetAllCows() throws Exception {

        Cow cow = Cow.builder()
                .id(1L)
                .cowName("Lakshmi")
                .breed("Jersey")
                .age(5)
                .tagNumber("COW101")
                .build();

        Mockito.when(cowService.getAllCows())
                .thenReturn(List.of(cow));

        mockMvc.perform(get("/api/cows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cowName")
                        .value("Lakshmi"));
    }

    /*
     * Test get cow by id API.
     */
    @Test
    void testGetCowById() throws Exception {

        Cow cow = Cow.builder()
                .id(1L)
                .cowName("Lakshmi")
                .breed("Jersey")
                .age(5)
                .tagNumber("COW101")
                .build();

        Mockito.when(cowService.getCowById(1L))
                .thenReturn(cow);

        mockMvc.perform(get("/api/cows/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cowName")
                        .value("Lakshmi"));
    }

    /*
     * Test delete cow API.
     */
    @Test
    void testDeleteCow() throws Exception {

        mockMvc.perform(delete("/api/cows/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Cow deleted successfully"));
    }
}