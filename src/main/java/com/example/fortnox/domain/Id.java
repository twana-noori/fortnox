package com.example.fortnox.domain;

import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

public record Id(
    Long value
) {
    public static Validation<String, Id> validate(final Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return Validation.invalid("Id can not be empty");
        }
        return Validation.valid(new Id(id));
    }
    public static Validation<String, Id> validateOptional(final Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return Validation.valid(null);
        }
        return validate(id);
    }
}
