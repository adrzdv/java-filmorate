package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class ReleaseValidator implements ConstraintValidator<ReleaseValidation, LocalDate> {
    @Override
    public void initialize(ReleaseValidation constraint) {

    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {

        return (!releaseDate.isBefore(LocalDate.of(1985, 12, 28)));

    }
}
