package com.example.fortnox.controller;

import com.example.fortnox.controller.request.RentalRequest;
import com.example.fortnox.controller.response.CarModelResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(RentalApi.BASE_PATH)
public interface RentalApi {

    String BASE_PATH = "/api/rental";
    String MODELS = "/models";
    String AVAILABLE_CARS = "/available";
    String RENT = "/rent";

    @GetMapping(MODELS)
    ResponseEntity<List<CarModelResponse>> getAllModels();

    @GetMapping(AVAILABLE_CARS)
    ResponseEntity<?> getAllAvailableCars(
            @RequestParam("startDate") final String startDate,
            @RequestParam("endDate") final String endDate,
            @RequestParam(name = "carModelId", required = false) final Long carModelId);

    @PostMapping(RENT)
    ResponseEntity<String> rent(@RequestBody final RentalRequest request);
}
