package com.example.fortnox.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<?> handleCarNotFoundException(final CarNotFoundException exception) {
        log.info("Car not found: ", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleCarNotFoundException(final UserNotFoundException exception) {
        log.info("User not found: ", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(CarSQLException.class)
    public ResponseEntity<?> handleCarSQLException(final CarSQLException exception) {
        log.info("Database error occurred while processing car data: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("SQL error");
    }

    @ExceptionHandler(RentalException.class)
    public ResponseEntity<?> handleRentalSQLException(final RentalException exception) {
        log.info("Car already rented during this period. ", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidSQLDataException.class)
    public ResponseEntity<?> handleInvalidSQLDataException(final InvalidSQLDataException exception) {
        log.info("Invalid data encountered in the database. ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("SQL error");
    }

    @ExceptionHandler(PricePlanNotFoundException.class)
    public ResponseEntity<?> handlePricePlanNotFoundException(final PricePlanNotFoundException exception) {
        log.info("Invalid data encountered in the database. ", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlePricePlanNotFoundException(final Exception exception) {
        log.info("Something unexpected happened ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
