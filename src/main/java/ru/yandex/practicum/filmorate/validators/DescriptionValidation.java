package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DescriptionValidator.class)
@Documented
public @interface DescriptionValidation {
    String message() default "Description is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
