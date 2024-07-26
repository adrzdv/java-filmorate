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
        LocalDate earlyBorder = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(earlyBorder)) {
            log.warn("Release date is invalid. Must be after 28/12/1895");
            return false;
        }
        return true;
    }
}
