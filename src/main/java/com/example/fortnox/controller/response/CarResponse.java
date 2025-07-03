package com.example.fortnox.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CarResponse (
        @JsonProperty("id") Long id,
        @JsonProperty("registrationNumber") String registrationNumber,
        @JsonProperty("carModel") CarModelResponse carModel
){}
