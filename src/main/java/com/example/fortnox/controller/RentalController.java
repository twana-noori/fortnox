package com.example.fortnox.controller;

import com.example.fortnox.controller.request.RentalRequest;
import com.example.fortnox.controller.response.CarModelResponse;
import com.example.fortnox.domain.Id;
import com.example.fortnox.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RentalController implements RentalApi {

    final RentalService rentalService;
    final ValidationService validationService;

    public RentalController(final RentalService rentalService, final ValidationService validationService) {
        this.rentalService = rentalService;
        this.validationService = validationService;
    }

    @Override
    public ResponseEntity<List<CarModelResponse>> getAllModels() {
        final List<CarModelResponse> carModels = rentalService.getAllModels()
                .stream()
                .map(ResponseMapper::mapCarModelToResponse)
                .toList();

        return ResponseEntity.ok(carModels);
    }

    @Override
    public ResponseEntity<?> getAllAvailableCars(final String startDate, final String endDate, final Long carModelId) {
        final var rentalPeriods = validationService.validateRentalPeriod(startDate, endDate);
        final Id carId = Id.validateOptional(carModelId).get();

        if (rentalPeriods.isInvalid()) {
            return ResponseEntity.badRequest().body(rentalPeriods.getError());
        }

        return ResponseEntity.ok(
                rentalService.getAvailableCars(rentalPeriods.get(), carId)
                        .stream()
                        .map(ResponseMapper::mapCarToResponse)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<String> rent(final RentalRequest request) {
        final var createRentals = validationService.validateRental(request);
        if (createRentals.isInvalid()) {
            return ResponseEntity.badRequest().body(createRentals.getError());
        }

        final long bookingId = rentalService.createRental(createRentals.get());
        return ResponseEntity.ok("Successfully saved booking. BookingId: " + bookingId);
    }
}
