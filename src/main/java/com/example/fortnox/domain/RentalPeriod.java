package com.example.fortnox.domain;

import io.vavr.control.Validation;

public record RentalPeriod(
        RentalDate startDate,
        RentalDate endDate
) {
    public static Validation<String, RentalPeriod> validate(final RentalDate start, final RentalDate end) {
        if (!start.date().isBefore(end.date())) {
            return Validation.invalid("Start date must be before the end date");
        }
        return Validation.valid(new RentalPeriod(start, end));
    }
    public static int calculateRentalDays(RentalPeriod period) {
        return (int) (period.endDate().date().toEpochDay() - period.startDate().date().toEpochDay()) + 1;
    }
}
