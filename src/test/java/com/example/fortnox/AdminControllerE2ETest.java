package com.example.fortnox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllRentals_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/admin/rentals")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}