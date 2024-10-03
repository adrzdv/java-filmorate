package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Director object class
 * <p>
 * id - director's identification number
 * name - director's name, cant be empty
 */

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Director {

    private int id;
    @NotBlank
    private String name;
}
