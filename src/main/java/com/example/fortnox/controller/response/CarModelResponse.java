package com.example.fortnox.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CarModelResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("brand") String brand,
        @JsonProperty("model") String model,
        @JsonProperty("modelYear") Integer modelYear,
        @JsonProperty("pricePeriods") List<CarPricePeriodResponse> pricePeriods
){}
