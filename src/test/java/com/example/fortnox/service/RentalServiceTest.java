package com.example.fortnox.service;

import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.repositories.CarRepository;
import com.example.fortnox.repositories.RentalRepository;
import com.example.fortnox.controller.response.CarResponse;
import com.example.fortnox.utils.PriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private CarRepository carRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private PriceCalculator priceCalculator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableCarsExcludesVolvo() {
        RentalPeriod rentalPeriod = new RentalPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        Car volvoCar = new Car("Volvo", "V60", 2021);
        Car toyotaCar = new Car("Toyota", "Camry", 2021);

        when(carRepository.findAllAvailableCars(rentalPeriod.startDate().date(), rentalPeriod.endDate().date()))
                .thenReturn(Arrays.asList(volvoCar, toyotaCar));

        List<CarResponse> availableCars = rentalService.getAvailableCars(rentalPeriod, null);

        assertEquals(1, availableCars.size());
        assertEquals("Toyota", availableCars.get(0).getMake());
    }

    @Test
    public void testGetAvailableCarsByModelExcludesVolvo() {
        RentalPeriod rentalPeriod = new RentalPeriod(LocalDate.now(), LocalDate.now().plusDays(5));
        Car volvoCar = new Car("Volvo", "XC90", 2021);
        Car toyotaCar = new Car("Toyota", "RAV4", 2021);

        when(carRepository.findAvailableCarsByModel(rentalPeriod.startDate().date(), rentalPeriod.endDate().date(), 1))
                .thenReturn(Arrays.asList(volvoCar, toyotaCar));

        List<CarResponse> availableCars = rentalService.getAvailableCars(rentalPeriod, new Id(1));

        assertEquals(1, availableCars.size());
        assertEquals("Toyota", availableCars.get(0).getMake());
    }
}