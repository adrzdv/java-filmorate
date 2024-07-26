package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    private final LocalDate exampleBirth = LocalDate.of(1990, 01, 01);
    private final LocalDate exampleRelease = LocalDate.of(2000, 01, 01);
    private final LocalDate exampleBirthIncorrect = LocalDate.of(2025, 01, 01);
    private final LocalDate exampleReleaseIncorrect = LocalDate.of(1800, 01, 01);
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.usingContext().getValidator();


    @Test
    void shouldValidUser() {
        User newuser = User.builder()
                .id(1L)
                .email("example@mail.com")
                .name("not empty name")
                .login("simple")
                .birthday(exampleBirth)
                .build();
        User newuser2 = User.builder()
                .id(2L)
                .email("another@example.com")
                .login("new")
                .birthday(exampleBirth)
                .build();
        UserController uc = new UserController();
        assertEquals(newuser, uc.addNew(newuser));
        assertEquals(newuser2, uc.addNew(newuser2));
    }

    @Test
    void shouldValidFilm() {
        FilmController fc = new FilmController();
        Film film = Film.builder()
                .id(1L)
                .name("film")
                .description("description")
                .releaseDate(exampleRelease)
                .duration(160L)
                .build();
        assertEquals(film, fc.addNew(film));

    }

    @Test
    void testUserValidation() {

        User userEmail = User.builder()
                .id(1L)
                .email("some strange string")
                .login("login")
                .birthday(exampleBirth)
                .build();

        User userLogin = User.builder()
                .id(2L)
                .email("another@example.com")
                .login("n e w")
                .birthday(exampleBirth)
                .build();

        User userBirth = User.builder()
                .id(3L)
                .email("another@example.com")
                .login("login")
                .birthday(exampleBirthIncorrect)
                .build();

        Set<ConstraintViolation<User>> validates = validator.validate(userEmail);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

        validates = validator.validate(userLogin);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

        validates = validator.validate(userBirth);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

    }

    @Test
    void testFilmValidation() {

        Film filmName = Film.builder()
                .id(1L)
                .name("")
                .description("description")
                .duration(160L)
                .releaseDate(exampleRelease)
                .build();
        StringBuilder description = new StringBuilder();
        description.append("description".repeat(20));
        Film filmDescription = Film.builder()
                .id(2L)
                .name("name")
                .description(description.toString())
                .duration(160L)
                .releaseDate(exampleRelease)
                .build();
        Film filmDuration = Film.builder()
                .id(3L)
                .name("name")
                .description("description")
                .duration(-160L)
                .releaseDate(exampleRelease)
                .build();
        Film filmRelease = Film.builder()
                .id(4L)
                .name("name")
                .description("description")
                .duration(160L)
                .releaseDate(exampleReleaseIncorrect)
                .build();

        Set<ConstraintViolation<Film>> validates = validator.validate(filmName);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

        validates = validator.validate(filmDescription);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

        validates = validator.validate(filmDuration);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);

        validates = validator.validate(filmRelease);
        assertFalse(validates.isEmpty());
        validates.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(System.out::println);
    }
}
