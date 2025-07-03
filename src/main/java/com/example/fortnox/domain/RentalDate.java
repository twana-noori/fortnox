package com.example.fortnox.domain;

import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record RentalDate(
        LocalDate date
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static Validation<String, RentalDate> validate(final String date) {
        if (ObjectUtils.isEmpty(date)) {
            return Validation.invalid("Rental date can not be empty: " + date);
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date, FORMATTER);
            if (parsedDate.isBefore(LocalDate.now())) {
                return Validation.invalid("Rental date must be in the future: " + date);
            }
            return Validation.valid(new RentalDate(parsedDate));
        } catch (DateTimeParseException e) {
            return Validation.invalid("Rental date format must be yyyy-MM-dd: "  + date);
        }
    }
}
