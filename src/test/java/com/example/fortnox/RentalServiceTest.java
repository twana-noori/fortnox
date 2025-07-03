package com.example.fortnox;

import com.example.fortnox.domain.BookingType;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.CreateRental;
import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.PriceType;
import com.example.fortnox.domain.RentalDate;
import com.example.fortnox.domain.RentalPeriod;
import com.example.fortnox.repositories.CarModelRepository;
import com.example.fortnox.repositories.CarRepository;
import com.example.fortnox.repositories.RentalRepository;
import com.example.fortnox.service.RentalService;
import com.example.fortnox.utils.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
class RentalServiceTest {

    @Mock
    private CarModelRepository carModelRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private PriceCalculator priceCalculator;

    @Mock
    private RentalService rentalService;

    @Test
    void testGetAvailableCars_WithNoCarModelId() {
        carModelRepository = Mockito.mock(CarModelRepository.class);
        carRepository = Mockito.mock(CarRepository.class);
        rentalService = new RentalService(carModelRepository, carRepository, rentalRepository, priceCalculator);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 1);

        RentalPeriod period = new RentalPeriod(
                new RentalDate(startDate),
                new RentalDate(endDate)
        );
        rentalService.getAvailableCars(period, null);
        verify(carRepository).findAllAvailableCars(startDate, endDate);
        verifyNoInteractions(carModelRepository);
    }

    @Test
    void testGetAvailableCars_WithCarModelId() {
        carModelRepository = Mockito.mock(CarModelRepository.class);
        carRepository = Mockito.mock(CarRepository.class);
        rentalService = new RentalService(carModelRepository, carRepository, rentalRepository, priceCalculator);
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 1);

        RentalPeriod period = new RentalPeriod(
                new RentalDate(startDate),
                new RentalDate(endDate)
        );
        when(carModelRepository.findById(1L)).thenReturn(java.util.Optional.of(new CarModel(1L, "Brand", "Model", 2020, List.of())));
        rentalService.getAvailableCars(period, new Id(1L));
        verify(carModelRepository).findById(1L);
        verify(carRepository).findAvailableCarsByModel(startDate, endDate, 1L);
    }

    @Test
    void testCreateRental() {
        carModelRepository = Mockito.mock(CarModelRepository.class);
        carRepository = Mockito.mock(CarRepository.class);
        rentalRepository = Mockito.mock(RentalRepository.class);
        priceCalculator = Mockito.mock(PriceCalculator.class);
        rentalService = new RentalService(carModelRepository, carRepository, rentalRepository, priceCalculator);

        final CreateRental createRental = new CreateRental(
                BookingType.MANUAL,
                new Id(1L),
                null,
                new RentalPeriod(
                        new RentalDate(LocalDate.of(2024, 1, 1)),
                        new RentalDate(LocalDate.of(2024, 1, 3))
                )
        );
        final CarPricePeriod carPriceMonth= new CarPricePeriod(
                1L,
                BigDecimal.valueOf(10),
                PriceType.MONTH,
                2);

        when(carRepository.findCarModelByCarId(createRental.carId())).thenReturn(new CarModel(1L, "Brand", "Model", 2020, List.of()));
        when(priceCalculator.choosePricePeriod(Mockito.any(), Mockito.any())).thenReturn(carPriceMonth);

        rentalService.createRental(createRental);
        verify(rentalRepository).saveRental(createRental, BigDecimal.valueOf(30.0));
    }
}
