package com.example.fortnox.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Rental(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal revenue,
        Car car,
        User user,
        BookingType bookingType
) {
}
