package com.example.fortnox.domain;

import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BookingType {
    ONLINE,
    MANUAL;

    public static Validation<String, BookingType> validate(final String type) {
        if (ObjectUtils.isEmpty(type)) {
            return Validation.invalid("Booking type cannot be null or empty");
        }
        try {
            return Validation.valid(BookingType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Validation.invalid(
                    "Invalid booking type: " + type + ". Allowed values are: " + String.join(", ", BookingType.names())
            );
        }
    }
    public static List<String> names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
