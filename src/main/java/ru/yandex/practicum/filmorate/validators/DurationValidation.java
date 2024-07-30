package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DurationValidator.class)
@Documented
public @interface DurationValidation {

    String message() default "Duration is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
