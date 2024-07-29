package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class BirthValidator implements ConstraintValidator<BirthValidation, LocalDate> {
    @Override
    public void initialize(BirthValidation constraint) {

    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate.isAfter(LocalDate.now())) {

            return false;
        }
        return true;
    }
}
