package com.example.fortnox.controller;

import com.example.fortnox.controller.response.AdminRentalResponse;
import com.example.fortnox.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController implements AdminApi {

    final RentalService rentalService;

    public AdminController(final RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public ResponseEntity<AdminRentalResponse> getAllRentals() {
        final AdminRentalResponse adminRentalResponse = ResponseMapper.mapRentalToResponse(rentalService.getAllRentals());
        return ResponseEntity.ok(adminRentalResponse);
    }
}
