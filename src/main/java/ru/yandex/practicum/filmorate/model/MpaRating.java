package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Mpa object class
 * <p>
 * id - identification number
 * name - MPA name
 */
@AllArgsConstructor
@Builder
@Data
public class MpaRating {
    private int id;
    private String name;
}
