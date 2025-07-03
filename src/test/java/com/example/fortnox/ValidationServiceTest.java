package com.example.fortnox;

import com.example.fortnox.controller.ValidationService;
import com.example.fortnox.controller.request.RentalRequest;
import com.example.fortnox.controller.request.UserRequest;
import io.vavr.control.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ValidationServiceTest {

    private final ValidationService validationService = new ValidationService();

    @Test
    void validateRentalPeriod_validDates_shouldReturnValid() {
        var result = validationService.validateRentalPeriod(
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(2).toString());
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void validateRentalPeriod_invalidDates_shouldReturnInvalid() {
        var invalidFormat = validationService.validateRentalPeriod("20240202", "2024-06-10");
        assertThat(invalidFormat.isInvalid()).isTrue();
        assertThat(invalidFormat.getError()).contains("Rental date format must be yyyy-MM-dd: 20240202, Rental date must be in the future: 2024-06-10");

        var startAfterEndDate = validationService.validateRentalPeriod(LocalDate.now().plusDays(3).toString(),LocalDate.now().plusDays(1).toString());
        assertThat(startAfterEndDate.isInvalid()).isTrue();
        assertThat(startAfterEndDate.getError()).contains("Start date must be before the end date");
    }

    @Test
    void validateRental_validRequest_shouldReturnValid() {
        RentalRequest request = new RentalRequest(
                "MANUAL",
                1L,
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(2).toString(),
                new UserRequest(1L,"Jane", "1979-08-08")
        );
        Validation<String, ?> result = validationService.validateRental(request);
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void validateRental_invalidRequest_shouldReturnInvalid() {
        RentalRequest request = new RentalRequest(
                "INVALID_TYPE",
                null,
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(2).toString(),
                new UserRequest(1L,"", "")
        );
        Validation<String, ?> result = validationService.validateRental(request);
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).contains(
                "Invalid booking type: INVALID_TYPE. " +
                "Allowed values are: ONLINE, MANUAL, Id can not be empty, " +
                "Name cannot be null or empty, " +
                "Date of birth cannot be null or empty"
        );

    }
    @Test
    void validateRental_inValidAge_shouldReturnInValid() {
        RentalRequest request = new RentalRequest(
                "MANUAL",
                1L,
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(2).toString(),
                new UserRequest(1L,"Jane", "2020-08-08")
        );
        Validation<String, ?> result = validationService.validateRental(request);
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).contains("Driver must be at least 18 years old");
    }
}