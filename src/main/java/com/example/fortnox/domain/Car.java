package com.example.fortnox.domain;

public record Car(
        Long id,
        String registrationNumber,
        CarModel carModel
) { }
