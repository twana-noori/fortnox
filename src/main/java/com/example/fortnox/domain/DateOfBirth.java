package com.example.fortnox.domain;

import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record DateOfBirth(
        LocalDate value
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final int DRIVER_AGE = 18;

    public static Validation<String, DateOfBirth> validateDateOfBirthAndAge(final String dateOfBirth) {

        if (ObjectUtils.isEmpty(dateOfBirth)) {
            return Validation.invalid("Date of birth cannot be null or empty");
        }
        try {
            LocalDate parsedDate = LocalDate.parse(dateOfBirth, FORMATTER);

            int age = Period.between(parsedDate, LocalDate.now()).getYears();
            if (age < DRIVER_AGE) {
                return Validation.invalid("Driver must be at least " + DRIVER_AGE + " years old");
            }
            return Validation.valid(new DateOfBirth(parsedDate));
        } catch (
                DateTimeParseException e) {
            return Validation.invalid("Date of birth must be yyyy-MM-dd");
        }
    }
}
