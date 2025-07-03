package com.example.fortnox.domain;

import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

public record Name(
        String value
) {
    public static Validation<String, Name> validateName(final String name) {
        if (ObjectUtils.isEmpty(name)) {
            return Validation.invalid("Name cannot be null or empty");
        }
        if (!name.trim().matches("\\D+")) {
            return Validation.invalid("Name must not contain digits");
        }
        return Validation.valid(new Name(name.trim()));
    }
}
