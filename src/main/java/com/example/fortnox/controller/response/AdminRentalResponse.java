package com.example.fortnox.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record AdminRentalResponse(
        @JsonProperty("rentals") List<RentalResponse> rentals,
        @JsonProperty("numberOfRentals") int numberOfRentals,
        @JsonProperty("totalRevenue") BigDecimal totalRevenue
) {}
