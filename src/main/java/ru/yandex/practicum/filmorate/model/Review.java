package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Review object class
 * <p>
 * id - identification number of review
 * content - text of review
 * usefulRate - current review rating
 * userId - id of a review's author
 * filmId - reviewed film's id
 */

@Data
@AllArgsConstructor
@Builder
public class Review {
    private int id;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private int usefulRate;
    @NotNull(message = "User ID cannot be null")
    private int userId;
    @NotNull(message = "Film ID cannot be null")
    private int filmId;
    private boolean isPositive;
}


