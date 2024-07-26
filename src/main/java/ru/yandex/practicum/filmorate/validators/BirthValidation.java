package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = BirthValidator.class)
@Documented
public @interface BirthValidation {

    String message() default "Birth date is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
