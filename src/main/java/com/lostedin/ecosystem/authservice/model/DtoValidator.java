package com.lostedin.ecosystem.authservice.model;


import com.lostedin.ecosystem.authservice.exception.ServiceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class DtoValidator {

    //TODO: Should recheck class, cause its copied from UserService

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validateOrThrow(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));

            throw new IllegalArgumentException("DTO validation failed: " + errorMessage);
        }
    }

    public static <T> void validateOrThrow400Exception(T object) {
        try {
            validateOrThrow(object);
        }catch (IllegalArgumentException e){
            throw new ServiceException(401,e.getMessage());
        }
    }

}
