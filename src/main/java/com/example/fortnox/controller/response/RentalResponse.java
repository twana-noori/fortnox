package com.example.fortnox.controller.response;

import com.example.fortnox.domain.BookingType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentalResponse(
        @JsonProperty("bookingType") BookingType bookingType,
        @JsonProperty("car") CarAdminResponse car,
        @JsonProperty("driver") String userName,
        @JsonProperty("startDate") LocalDate startDate,
        @JsonProperty("endDate") LocalDate endDate,
        @JsonProperty("revenue") BigDecimal revenue
) {
}
