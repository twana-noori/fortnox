package com.example.fortnox.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RentalRequest(
        @JsonProperty("bookingType") String bookingType,
        @JsonProperty("carId") Long carId,
        @JsonProperty("startDate") String startDate,
        @JsonProperty("endDate") String endDate,
        @JsonProperty("user") UserRequest user
) {}
