package com.example.fortnox;

import com.example.fortnox.controller.ResponseMapper;
import com.example.fortnox.controller.response.AdminRentalResponse;
import com.example.fortnox.controller.response.CarResponse;
import com.example.fortnox.domain.BookingType;
import com.example.fortnox.domain.Car;
import com.example.fortnox.domain.CarModel;
import com.example.fortnox.domain.CarPricePeriod;
import com.example.fortnox.domain.DateOfBirth;
import com.example.fortnox.domain.Id;
import com.example.fortnox.domain.Name;
import com.example.fortnox.domain.PriceType;
import com.example.fortnox.domain.Rental;
import com.example.fortnox.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class ResponseMapperTest {

    CarPricePeriod pricePeriod = new CarPricePeriod(
            1L,
            new BigDecimal("80.0"),
            PriceType.MONTH,
            1
    );
    CarPricePeriod pricePeriod2 = new CarPricePeriod(
            2L,
            new BigDecimal("100.0"),
            PriceType.REGULAR,
            2
    );

    CarModel mockedCarModel = new CarModel(
            1L,
            "Volvo",
            "XC60",
            2022,
            List.of(pricePeriod, pricePeriod2)
    );

    Car car = new Car(
            10L,
            "ABC123",
            mockedCarModel
    );
    User user = new User(
            new Id(1L),
            new Name("John Doe"),
            new DateOfBirth(LocalDate.of(1990, 1, 1))
    );

    Rental rental = new Rental(
            1L,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 2, 2),
            new BigDecimal("100.0"),
            car,
            user,
            BookingType.ONLINE
    );
    Rental rental2 = new Rental(
            2L,
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 3, 3),
            new BigDecimal("100.0"),
            car,
            user,
            BookingType.ONLINE
    );

    @Test
    void mapCarToResponseTest() {

        CarResponse response = ResponseMapper.mapCarToResponse(car);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("ABC123", response.registrationNumber());

        assertNotNull(response.carModel());
        assertEquals(1L, response.carModel().id());
        assertEquals("Volvo", response.carModel().brand());
        assertEquals("XC60", response.carModel().model());
        assertEquals(2022, response.carModel().modelYear());

        assertNotNull(response.carModel().pricePeriods());
        assertEquals(2, response.carModel().pricePeriods().size());
        assertEquals(1L, response.carModel().pricePeriods().get(0).id());
        assertEquals(PriceType.MONTH, response.carModel().pricePeriods().get(0).priceType());
        assertEquals(BigDecimal.valueOf(80.0), response.carModel().pricePeriods().get(0).pricePerDay());
        assertEquals(1, response.carModel().pricePeriods().get(0).priority());
        assertEquals(2L, response.carModel().pricePeriods().get(1).id());
        assertEquals(PriceType.REGULAR, response.carModel().pricePeriods().get(1).priceType());
        assertEquals(BigDecimal.valueOf(100.0), response.carModel().pricePeriods().get(1).pricePerDay());
        assertEquals(2, response.carModel().pricePeriods().get(1).priority());

    }

    @Test
    void mapRentalToResponseTest() {
        final AdminRentalResponse adminRentalResponse = ResponseMapper.mapRentalToResponse(List.of(rental, rental2));
        assertEquals(2, adminRentalResponse.rentals().size());
        assertEquals("ABC123", adminRentalResponse.rentals().get(0).car().registrationNumber());
        assertEquals("John Doe", adminRentalResponse.rentals().get(0).userName());
        assertEquals(LocalDate.of(2020, 1, 1), adminRentalResponse.rentals().get(0).startDate());
        assertEquals(LocalDate.of(2020, 2, 2), adminRentalResponse.rentals().get(0).endDate());
        assertEquals(BigDecimal.valueOf(100.0), adminRentalResponse.rentals().get(0).revenue());
        assertEquals(BookingType.ONLINE, adminRentalResponse.rentals().get(0).bookingType());
        assertEquals("ABC123", adminRentalResponse.rentals().get(1).car().registrationNumber());
        assertEquals("John Doe", adminRentalResponse.rentals().get(1).userName());
        assertEquals(LocalDate.of(2023, 1, 1), adminRentalResponse.rentals().get(1).startDate());
        assertEquals(LocalDate.of(2023, 3, 3), adminRentalResponse.rentals().get(1).endDate());
        assertEquals(BigDecimal.valueOf(100.0), adminRentalResponse.rentals().get(1).revenue());
        assertEquals(BookingType.ONLINE, adminRentalResponse.rentals().get(1).bookingType());
        assertEquals(2, adminRentalResponse.numberOfRentals());
        assertEquals(BigDecimal.valueOf(200.0), adminRentalResponse.totalRevenue());
        assertNotNull(rental);
    }
}
