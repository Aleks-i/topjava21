package ru.javawebinar.topjava.util;

import javax.validation.*;
import java.util.Set;

public class ValidatorForJdbc {

    private ValidatorForJdbc() {}

    public static <T> void validate(T object) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.size() != 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
