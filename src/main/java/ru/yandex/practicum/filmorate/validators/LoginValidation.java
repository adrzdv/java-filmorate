package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = LoginValidator.class)
@Documented
public @interface LoginValidation {

    String message() default "Login is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
