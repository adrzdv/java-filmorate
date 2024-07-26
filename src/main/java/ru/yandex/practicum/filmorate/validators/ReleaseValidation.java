package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ReleaseValidator.class)
@Documented
public @interface ReleaseValidation {

    String message() default "Release date is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
