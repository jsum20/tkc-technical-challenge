package com.jason.tkc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransportController.class)
@AutoConfigureMockMvc
public class TransportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCalculateCost() throws Exception {
        mockMvc.perform(get("/transport/10")
                        .param("passengers", "2")
                        .param("parking", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportType").value("HSTC Transport"))
                .andExpect(jsonPath("$.totalCost").value(4.5));
    }
}
