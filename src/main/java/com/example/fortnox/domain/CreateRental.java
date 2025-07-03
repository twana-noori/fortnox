package com.example.fortnox.domain;

public record CreateRental(
        BookingType bookingType,
        Id carId,
        User user,
        RentalPeriod rentalPeriod
){
}
