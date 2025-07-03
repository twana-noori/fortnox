package com.example.fortnox.controller;

import com.example.fortnox.controller.request.RentalRequest;
import com.example.fortnox.domain.BookingType;
import com.example.fortnox.domain.CreateRental;
import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.RentalDate;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.domain.User;
import io.vavr.Tuple;
import io.vavr.control.Validation;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public Validation<String, RentalPeriod> validateRentalPeriod(final String start, final String end) {
        return Validation
                .combine(
                        RentalDate.validate(start),
                        RentalDate.validate(end)
                )
                .ap(Tuple::of)
                .mapError(errors -> String.join(", ", errors))
                .flatMap(valid -> RentalPeriod.validate(valid._1, valid._2));
    }

    public Validation<String, CreateRental> validateRental(final RentalRequest request) {
        return Validation.combine(
                        BookingType.validate(request.bookingType()),
                        Id.validate(request.carId()),
                        User.validate(request.user()),
                        validateRentalPeriod(request.startDate(), request.endDate()))
                .ap(Tuple::of)
                .mapError(errors -> String.join(", ", errors))
                .map(rental ->
                        new CreateRental(
                                rental._1,
                                rental._2,
                                rental._3,
                                rental._4
                        ));
    }
}
