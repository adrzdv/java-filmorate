package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.yandex.practicum.filmorate.validators.BirthValidation;
import ru.yandex.practicum.filmorate.validators.LoginValidation;

import java.time.LocalDate;

/**
 * User class
 * <p>
 * id - identification number
 * email - user's email, cant be null, must be validated by regex
 * login - user's login, mustn't have space and cant be null
 * name - user's name for representation, if null - use login
 * birthDate - user's date of birth, cant be in the future
 */

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = {"id", "login", "email"})
@AllArgsConstructor
public class User {

    private Long id;
    @NotBlank
    @Email(message = "Email has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
    @NotBlank
    @LoginValidation
    private String login;
    private String name;
    @BirthValidation
    private LocalDate birthday;
}
