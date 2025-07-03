package com.example.fortnox.service;

import com.example.fortnox.controller.ResponseMapper;
import com.example.fortnox.controller.response.AdminRentalResponse;
import com.example.fortnox.controller.response.CarModelResponse;
import com.example.fortnox.controller.response.CarResponse;
import com.example.fortnox.domain.BookingType;
import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.CreateRental;
import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.repositories.CarModelRepository;
import com.example.fortnox.repositories.CarRepository;
import com.example.fortnox.repositories.RentalRepository;
import com.example.fortnox.utils.PriceCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RentalService {

    private final CarModelRepository carModelRepository;
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;
    private final PriceCalculator priceCalculator;

    public RentalService(final CarModelRepository carModelRepository,
                         final CarRepository carRepository,
                         final RentalRepository rentalRepository,
                         final PriceCalculator priceCalculator) {
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
        this.priceCalculator = priceCalculator;
    }

    public List<CarModelResponse> getAllModels() {
        return carModelRepository.findAll().stream()
                .map(ResponseMapper::mapCarModelToResponse)
                .toList();
    }

    public List<CarResponse> getAvailableCars(final RentalPeriod rentalPeriod, final Id carModelId) {
        final List<Car> cars;
        if (carModelId == null) {
            cars = carRepository.findAllAvailableCars(
                    rentalPeriod.startDate().date(),
                    rentalPeriod.endDate().date()
            );
        } else {
            cars = carModelRepository.findById(carModelId.value())
                    .map(carModel -> carRepository.findAvailableCarsByModel(
                            rentalPeriod.startDate().date(),
                            rentalPeriod.endDate().date(),
                            carModel.id()
                    ))
                    .orElse(List.of());
        }
        return cars.stream()
                .map(ResponseMapper::mapCarToResponse)
                .toList();
    }

    @Transactional
    public long createRental(final CreateRental createRental) {
        final CarModel carModel = carRepository.findCarModelByCarId(createRental.carId());
        final BigDecimal revenue = calculateCost(carModel, createRental.rentalPeriod(), createRental.bookingType());
        return rentalRepository.saveRental(createRental, revenue);
    }

    public AdminRentalResponse getAllRentals() {
        return ResponseMapper.mapRentalToResponse(rentalRepository.getAllRentals());
    }

    private BigDecimal calculateCost(final CarModel carModel, final RentalPeriod rentalPeriod, final BookingType bookingType) {
        double onlineDiscount = bookingType == BookingType.ONLINE ? 0.9 : 1.0;
        final CarPricePeriod carPricePeriod = priceCalculator.choosePricePeriod(carModel, rentalPeriod);
        return carPricePeriod.pricePerDay()
                .multiply(BigDecimal.valueOf(RentalPeriod.calculateRentalDays(rentalPeriod)))
                .multiply(BigDecimal.valueOf(onlineDiscount));
    }
}
