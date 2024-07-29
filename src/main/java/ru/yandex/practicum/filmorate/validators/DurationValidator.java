package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DurationValidator implements ConstraintValidator<DurationValidation, Long> {
    @Override
    public void initialize(DurationValidation constraint) {

    }

    @Override
    public boolean isValid(Long duration, ConstraintValidatorContext context) {
        if (duration < 0) {

            return false;
        }

        return true;
    }
}
