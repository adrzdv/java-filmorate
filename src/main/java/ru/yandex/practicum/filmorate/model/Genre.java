package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Genre object class
 * <p>
 * id - identification number
 * name - name of genre
 */
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = {"id"})
public class Genre {
    private int id;
    private String name;
}
