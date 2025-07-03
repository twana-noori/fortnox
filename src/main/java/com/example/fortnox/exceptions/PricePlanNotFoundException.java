package com.example.fortnox.exceptions;

public class PricePlanNotFoundException extends RuntimeException {
    public PricePlanNotFoundException(String message) {
        super(message);
    }
}