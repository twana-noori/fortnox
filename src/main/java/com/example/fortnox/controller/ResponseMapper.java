package com.example.fortnox.controller;

import com.example.fortnox.controller.response.AdminRentalResponse;
import com.example.fortnox.controller.response.CarAdminResponse;
import com.example.fortnox.controller.response.CarModelResponse;
import com.example.fortnox.controller.response.CarPricePeriodResponse;
import com.example.fortnox.controller.response.CarResponse;
import com.example.fortnox.controller.response.RentalResponse;
import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.Rental;

import java.math.BigDecimal;
import java.util.List;

public class ResponseMapper {

    public static CarResponse mapCarToResponse(final Car car) {
        if (car == null) {
            return null;
        }
        return new CarResponse(
                car.id(),
                car.registrationNumber(),
                mapCarModelToResponse(car.carModel())
        );
    }

    public static CarModelResponse mapCarModelToResponse(final CarModel carModel) {
        if (carModel == null) {
            return null;
        }
        return new CarModelResponse(
                carModel.id(),
                carModel.brand(),
                carModel.model(),
                carModel.modelYear(),
                carModel.pricePeriods()
                        .stream()
                        .map(ResponseMapper::mapCarPricePeriodToResponse)
                        .toList()
        );
    }
    public static CarPricePeriodResponse mapCarPricePeriodToResponse(final CarPricePeriod carPricePeriod) {
        if (carPricePeriod == null) {
            return null;
        }
        return new CarPricePeriodResponse(
                carPricePeriod.id(),
                carPricePeriod.pricePerDay(),
                carPricePeriod.priceType(),
                carPricePeriod.priority()
        );
    }


    public static AdminRentalResponse mapRentalToResponse(final List<Rental> rentals) {
        BigDecimal totalRevenue = rentals.stream()
                .map(Rental::revenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RentalResponse> rentalResponses = rentals.stream()
                .map(r -> new RentalResponse(
                        r.bookingType(),
                        mapCarToAdminCarResponse(r.car()),
                        r.user().name().value(),
                        r.startDate(),
                        r.endDate(),
                        r.revenue()
                ))
                .toList();

        return new AdminRentalResponse(
                rentalResponses,
                rentals.size(),
                totalRevenue
        );
    }

    private static CarAdminResponse mapCarToAdminCarResponse(final Car car) {
        if( car == null) {
            return null;
        }
        return new CarAdminResponse(
                car.carModel().brand(),
                car.carModel().model(),
                car.carModel().modelYear(),
                car.registrationNumber()
        );
    }
}
