package com.car_rental.util;

import java.util.Set;
import java.util.stream.Collectors;

import com.car_rental.exception.InvalidDataException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidationUtil {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private ValidationUtil() {
    }

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> String.format(
                            "%s: invalid value '%s' — %s",
                            v.getPropertyPath(),
                            v.getInvalidValue(),
                            v.getMessage()
                    ))
                    .collect(Collectors.joining("\n"));

            throw new InvalidDataException(errorMessage);
        }
    }
}
