package com.example.fortnox.controller;

import com.example.fortnox.controller.response.AdminRentalResponse;
import com.example.fortnox.controller.response.CarModelResponse;
import com.example.fortnox.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController implements AdminApi {

    final RentalService rentalService;

    public AdminController(final RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Override
    public ResponseEntity<AdminRentalResponse> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/admin/car-models")
    public ResponseEntity<List<CarModelResponse>> getAllCarModels() {
        List<CarModelResponse> carModels = rentalService.getAllModels();
        return ResponseEntity.ok(carModels);
    }
}