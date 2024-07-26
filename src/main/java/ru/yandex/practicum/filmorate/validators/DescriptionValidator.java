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

        if (s.length() >= 200) {
            log.warn("Description is invalid. Length more than 200 symbols.");
            return false;
        }
        return true;
    }
}
