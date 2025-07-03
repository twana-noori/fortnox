package com.example.fortnox.domain;


public enum PriceType {
    REGULAR,
    WEEKEND,
    HIGH_SEASON,
    WEEK,
    MONTH;

    public static PriceType fromString(String value) {
        try {
            return PriceType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid PriceType value: " + value, e);
        }
    }
}
