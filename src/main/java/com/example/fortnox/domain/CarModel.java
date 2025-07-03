package com.example.fortnox.domain;

import java.util.List;

public record CarModel(
        Long id,
        String brand,
        String model,
        Integer modelYear,
        List<CarPricePeriod> pricePeriods
) {}
