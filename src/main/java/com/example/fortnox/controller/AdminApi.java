package com.example.fortnox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(AdminApi.BASE_PATH)
public interface AdminApi {

    String BASE_PATH = "/api/admin";
    String RENTALS = "/rentals";

    @GetMapping(RENTALS)
    ResponseEntity<?> getAllRentals();
}
