package com.example.fortnox.domain;

import com.example.fortnox.controller.request.UserRequest;
import io.vavr.control.Validation;
import org.springframework.util.ObjectUtils;

public record User(
        Id id,
        Name name,
        DateOfBirth dateOfBirth
) {

    public static Validation<String, User> validate(final UserRequest userRequest) {
        if (ObjectUtils.isEmpty(userRequest)) {
            return Validation.invalid("User cannot be null or empty");
        }
        return Validation.combine(
                        Id.validate(userRequest.id()),
                        Name.validateName(userRequest.name()),
                        DateOfBirth.validateDateOfBirthAndAge(userRequest.dateOfBirth()))
                .ap(User::new)
                .mapError(errors -> String.join(", ", errors));
    }
}
