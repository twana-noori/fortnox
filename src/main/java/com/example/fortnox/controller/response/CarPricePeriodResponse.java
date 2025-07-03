package com.example.fortnox.controller.response;

import com.example.fortnox.domain.PriceType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record CarPricePeriodResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("pricePerDay") BigDecimal pricePerDay,
        @JsonProperty("priceType") PriceType priceType,
        @JsonProperty("priority") Integer priority
) {}
