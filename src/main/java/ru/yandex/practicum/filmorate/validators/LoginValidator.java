package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginValidator implements ConstraintValidator<LoginValidation, String> {
    @Override
    public void initialize(LoginValidation constraint) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {

        if (s.contains(" ")) {
            log.warn("Login is invalid");
            return false;
        }
        return true;
    }
}
