package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.yandex.practicum.filmorate.validators.DescriptionValidation;
import ru.yandex.practicum.filmorate.validators.DurationValidation;
import ru.yandex.practicum.filmorate.validators.ReleaseValidation;

import java.time.LocalDate;

/**
 * Film class
 * <p>
 * id - identification number
 * name - film name, cant be null
 * description - film description, max length - 200 symbols
 * releaseDate - film's release date, must be not early 28.12.1895
 * duration - film's duration, must be positive value
 */

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@Builder
@AllArgsConstructor
public class Film {

    private Long id;
    @NotBlank
    private String name;
    @DescriptionValidation
    private String description;
    @ReleaseValidation
    private LocalDate releaseDate;
    @DurationValidation
    private Long duration;


}
