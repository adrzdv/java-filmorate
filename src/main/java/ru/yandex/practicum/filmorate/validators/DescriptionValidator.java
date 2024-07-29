package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DescriptionValidator implements ConstraintValidator<DescriptionValidation, String> {

    @Override
    public void initialize(DescriptionValidation constraint) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {

        return (!(s.length() >= 200));

    }
}
