package com.example.fortnox.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CarAdminResponse(
        @JsonProperty("brand") String brand,
        @JsonProperty("model") String model,
        @JsonProperty("year") Integer year,
        @JsonProperty("registrationNumber") String registrationNumber
){}
