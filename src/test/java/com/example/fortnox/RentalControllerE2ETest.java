package com.example.fortnox;

import com.example.fortnox.controller.request.RentalRequest;
import com.example.fortnox.controller.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllModels_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/rental/models"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getAllAvailableCars_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/rental/available")
                        .param("startDate", "2028-06-01")
                        .param("endDate", "2028-06-10")
                        .param("carModelId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void rent_shouldReturnOkOrBadRequest() throws Exception {
        RentalRequest request = new RentalRequest("MANUAL", 1L, "2026-06-01", "2026-06-10", new UserRequest(1L,"Jane","1979-08-08"));

        mockMvc.perform(post("/api/rental/rent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Successfully saved booking. BookingId:")));
    }
}
