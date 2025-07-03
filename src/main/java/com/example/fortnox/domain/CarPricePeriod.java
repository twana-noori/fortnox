package com.example.fortnox.domain;

import java.math.BigDecimal;

public record CarPricePeriod(
        Long id,
        BigDecimal pricePerDay,
        PriceType priceType,
        Integer priority
) {}
