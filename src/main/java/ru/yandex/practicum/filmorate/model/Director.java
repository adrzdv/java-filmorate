package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Director object class
 * <p>
 * id - director's identification number
 * name - director's name, cant be empty
 */

@Data
@Builder
@AllArgsConstructor
public class Director {

    private int id;
    @NotBlank
    private String name;
}
