package com.example.fortnox.exceptions;

public class InvalidSQLDataException extends RuntimeException {
    public InvalidSQLDataException(String message) {
        super(message);
    }
}